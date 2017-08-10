package com.intsummator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Calculates sum of unsigned integer values from file with specific {@link #path}, starting from {@link #start}
 * position and processes {@link #length} bytes.
 *
 * @author Anton Kuzmin
 * @version 1.0
 */
class IntSummatorTask implements Callable<Long> {

    private final Path path;
    private final long start;
    private final long length;

    public IntSummatorTask(Path path, long start, long length) {
        this.path = path;
        this.start = start;
        this.length = length;
    }

    @Override
    public Long call() throws Exception {
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
        IntSummatorTask that = (IntSummatorTask) o;
        return start == that.start &&
                length == that.length &&
                Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, start, length);
    }

    @Override
    public String toString() {
        return "RecursiveIntSummator{" + "path=" + path +
                ", start=" + start +
                ", length=" + length +
                '}';
    }
}
