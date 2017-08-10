package com.intsummator;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link MultiThreadedIntSummator} through {@link SimpleIntSummator}.
 *
 * @author Anton Kuzmin
 * @version 1.0
 */
public class MultiThreadedIntSummatorTest {

    private static final String TEST_FILE_NAME = "test.data";
    private static final int TEST_DATA_SIZE = 1_000_000_000;
    private static final int TEST_VALUES_PER_TASK = 10_000_000;
    private static final int TEST_THREAD_COUNT = 4;

    @Test
    public void calculateSum() throws Exception {
        Path path = Paths.get(TEST_FILE_NAME);

        new TestDataGenerator().generate(TEST_DATA_SIZE, path);

        SimpleIntSummator simpleIntSummator = new SimpleIntSummator();
        MultiThreadedIntSummator intSummator = new MultiThreadedIntSummator(TEST_THREAD_COUNT, TEST_VALUES_PER_TASK);

        long start = System.nanoTime();
        long expectedResult = simpleIntSummator.calculateSum(path);
        long end1 = System.nanoTime();
        long actualResult = intSummator.calculateSum(path);
        long end2 = System.nanoTime();

        System.out.println("Simple summator time: " + (end1 - start) / 1_000_000_000f + "s");
        System.out.println("Multi threaded summator time: " + (end2 - end1) / 1_000_000_000f + "s");

        assertEquals(expectedResult, actualResult);

        Files.delete(path);
    }

}