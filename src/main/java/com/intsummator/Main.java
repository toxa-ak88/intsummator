package com.intsummator;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Application entry point.
 *
 * @author Anton Kuzmin
 * @version 1.0
 */
public class Main {

    public static void main(String... args) throws IOException {
        if (args.length < 1) {
            System.err.println("Please specify path to the valid target file");
            System.exit(-1);
        }

        IntSummator intSummator = new ForkJoinIntSummator(Constants.FORK_THRESHOLD, Constants.FORK_COUNT);
        System.out.println(intSummator.calculateSum(Paths.get(args[0])));
    }
}
