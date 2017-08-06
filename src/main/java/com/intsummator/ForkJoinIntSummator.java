package com.intsummator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;

/**
 * Calculates sum of unsigned integers from specific file using {@link ForkJoinPool}.
 *
 * @author Anton Kuzmin
 * @version 1.0
 */
public class ForkJoinIntSummator implements IntSummator {

    private final int forkThreshold;
    private final int forkCount;

    public ForkJoinIntSummator(int forkThreshold, int forkCount) {
        this.forkThreshold = forkThreshold;
        this.forkCount = forkCount;
    }

    @Override
    public long calculateSum(Path path) throws IOException {
        return ForkJoinPool.commonPool()
                .invoke(new RecursiveIntSummator(path, 0L, Files.size(path), forkThreshold, forkCount));
    }
}
