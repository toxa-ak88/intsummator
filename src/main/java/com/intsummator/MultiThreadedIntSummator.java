package com.intsummator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Calculates sum of unsigned integers from specific file in parallel.
 *
 * @author Anton Kuzmin
 * @version 1.0
 */
public class MultiThreadedIntSummator implements IntSummator {

    private final int threadCount;
    private final int valuesPerTask;

    public MultiThreadedIntSummator(int threadCount, int valuesPerTask) {
        this.threadCount = threadCount;
        this.valuesPerTask = valuesPerTask;
    }

    @Override
    public long calculateSum(Path path) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        try {
            long size = Files.size(path);
            long partSize = (valuesPerTask / Integer.BYTES) * Integer.BYTES;

            List<Future<Long>> results = new ArrayList<>();
            for (long offset = 0; offset < size; offset += partSize) {
                Future<Long> future = executorService.submit(new IntSummatorTask(path, offset, partSize));
                results.add(future);
            }

            long sum = 0L;
            for (Future<Long> result : results) {
                sum += result.get();
            }
            return sum;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1L;
        } finally {
            executorService.shutdown();
        }
    }
}
