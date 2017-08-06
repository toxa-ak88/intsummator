package com.intsummator;

/**
 * Constants holder.
 *
 * @author Anton Kuzmin
 * @version 1.0
 */
public final class Constants {

    /**
     * Allows to convert signed int values to unsigned.
     */
    public static final long UNSIGNED_MASK_32 = 0xffffffffL;
    /**
     * Max length(in bytes) that task can process by itself, otherwise it will be forked into {@link #FORK_COUNT} sub
     * tasks.
     */
    public static final int FORK_THRESHOLD = 2 * 1024 * 1024;
    /**
     * Amount of sub tasks that will be created from huge task.
     */
    public static final int FORK_COUNT = 4;

    private Constants() {
    }
}
