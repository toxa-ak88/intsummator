package com.intsummator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.Random;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * Generates file with random data for test.
 *
 * @author Anton Kuzmin
 * @version 1.0
 */
public class TestDataGenerator {

    public void generate(int amount, Path file) throws IOException {
        try(FileChannel fileChannel = FileChannel.open(file, WRITE, TRUNCATE_EXISTING, CREATE)) {
            final Random random = new Random();
            ByteBuffer byteBuffer = ByteBuffer.allocate(63536).order(ByteOrder.LITTLE_ENDIAN);

            for (int i = 0; i < amount;) {
                while(byteBuffer.hasRemaining()) {
                    byteBuffer.putInt(random.nextInt());
                    i++;
                    if (i >= amount) {
                        break;
                    }
                }
                byteBuffer.flip();
                fileChannel.write(byteBuffer);
                byteBuffer.flip();
            }
        }
    }
}
