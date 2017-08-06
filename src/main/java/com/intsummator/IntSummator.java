package com.intsummator;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Provides API for int sum calculation.
 *
 * @author Anton Kuzmin
 * @version 1.0
 */
public interface IntSummator {

    long calculateSum(Path path) throws IOException;

}
