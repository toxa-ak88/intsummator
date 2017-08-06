package com.intsummator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Calculates sum of unsigned integer values from file with specific {@link #path}, starting from {@link #start}
 * position and processes {@link #length} bytes.
 *
 * @author Anton Kuzmin
 * @version 1.0
 */
class RecursiveIntSummator extends RecursiveTask<Long> {

    private final Path path;
    private final long start;
    private final long length;
    private final int forkThreshold;
    private final int forkCount;

    public RecursiveIntSummator(Path path, long start, long length, int forkThreshold, int forkCount) {
        this.path = path;
        this.start = start;
        this.length = length;
        this.forkThreshold = forkThreshold;
        this.forkCount = forkCount;
    }

    @Override
    protected Long compute() {
        if (length > forkThreshold) {
            return getForkJoinResult();
        } else {
            return calculateResult();
        }
    }

    private long getForkJoinResult() {
        List<ForkJoinTask<Long>> tasks = new ArrayList<>(forkCount);
        long partSize = length / forkCount;
        partSize = (partSize / Integer.BYTES) * Integer.BYTES;
        long offset = start;

        for (int i = 0; i < forkCount; i++) {
            long forkLength = i == forkCount - 1 ? (start + length) - offset : partSize;
            if (forkLength % Integer.BYTES != 0) {
                throw new IllegalArgumentException("Illegal fork length value " + length);
            }
            tasks.add(new RecursiveIntSummator(path, offset, forkLength, forkThreshold, forkCount).fork());
            offset += partSize;
        }

        long sum = 0L;
        for (ForkJoinTask<Long> job : tasks) {
            sum += job.join();
        }

        return sum;
    }

    private long calculateResult() {
        long sum = 0L;
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) length).order(ByteOrder.LITTLE_ENDIAN);
            FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
            fileChannel.position(start).read(byteBuffer);
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                int i = byteBuffer.getInt();
                sum += i & Constants.UNSIGNED_MASK_32;
            }
            byteBuffer.flip();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecursiveIntSummator that = (RecursiveIntSummator) o;
        return start == that.start &&
                length == that.length &&
                forkThreshold == that.forkThreshold &&
                forkCount == that.forkCount &&
                Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, start, length, forkThreshold, forkCount);
    }

    @Override
    public String toString() {
        return "RecursiveIntSummator{" + "path=" + path +
                ", start=" + start +
                ", length=" + length +
                ", forkThreshold=" + forkThreshold +
                ", forkCount=" + forkCount +
                '}';
    }
}
