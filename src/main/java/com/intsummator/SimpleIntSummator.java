package com.intsummator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Calculates sum of unsigned integers from specific file in single thread.
 *
 * @author Anton Kuzmin
 * @version 1.0
 */
public class SimpleIntSummator implements IntSummator {

    private static final int DEFAULT_READ_BUFFER_SIZE = 65536;

    private final int readBufferSize;

    public SimpleIntSummator(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    public SimpleIntSummator() {
        this(DEFAULT_READ_BUFFER_SIZE);
    }

    @Override
    public long calculateSum(Path path) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(readBufferSize).order(ByteOrder.LITTLE_ENDIAN);

        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {
            long sum = 0L;

            while (fileChannel.read(byteBuffer) >= 0) {
                fileChannel.read(byteBuffer);
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    sum += byteBuffer.getInt() & Constants.UNSIGNED_MASK_32;
                }
                byteBuffer.flip();
            }
            fileChannel.close();

            return sum;
        }
    }
}
