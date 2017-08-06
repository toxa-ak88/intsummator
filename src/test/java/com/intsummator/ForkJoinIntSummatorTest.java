package com.intsummator;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link ForkJoinIntSummator} through {@link SimpleIntSummator}.
 *
 * @author Anton Kuzmin
 * @version 1.0
 */
public class ForkJoinIntSummatorTest {

    private static final String TEST_FILE_NAME = "test.data";
    private static final int TEST_DATA_SIZE = 10_000_000;
    private static final int TEST_FORK_THRESHOLD = 50_000;
    private static final int TEST_FORK_COUNT = 4;

    @Test
    public void calculateSum() throws Exception {
        Path path = Paths.get(TEST_FILE_NAME);

        new TestDataGenerator().generate(TEST_DATA_SIZE, path);

        SimpleIntSummator simpleIntSummator = new SimpleIntSummator();
        ForkJoinIntSummator intSummator = new ForkJoinIntSummator(TEST_FORK_THRESHOLD, TEST_FORK_COUNT);

        long expectedResult = simpleIntSummator.calculateSum(path);
        assertEquals(expectedResult, intSummator.calculateSum(path));

        Files.delete(path);
    }

}