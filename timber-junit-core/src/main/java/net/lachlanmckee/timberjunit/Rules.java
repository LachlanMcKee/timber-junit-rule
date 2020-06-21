package net.lachlanmckee.timberjunit;

import android.util.Log;

/**
 * Defines a set of rules in which the {@link TimberTestRule}'s internal Timber tree must
 * adhere to when intercepting log messages.
 * <p>
 * The types of rules applied are:
 * <ol>
 * <li>Min priority - What is the lowest level of log type that should be logged.</li>
 * <li>Show thread - Whether the Thread ID and name should be logged.</li>
 * <li>Show timestamp - Whether the current time should be logged.</li>
 * </ol>
 */
public final class Rules {
    int mMinPriority;
    boolean mShowThread;
    boolean mShowTimestamp;
    boolean mOnlyLogWhenTestFails;

    public Rules() {
        mMinPriority = Log.VERBOSE;
        mShowThread = false;
        mShowTimestamp = true;
        mOnlyLogWhenTestFails = true;
    }

    /**
     * Defines what the lowest level of log type that should be logged.
     * <p>
     * This can be:
     * <ol>
     * <li>{@link Log#VERBOSE}</li>
     * <li>{@link Log#DEBUG}</li>
     * <li>{@link Log#INFO}</li>
     * <li>{@link Log#WARN}</li>
     * <li>{@link Log#ERROR}</li>
     * </ol>
     *
     * @param minPriority the Android log priority.
     * @return the mutated {@link Rules}
     */
    public Rules minPriority(int minPriority) {
        mMinPriority = minPriority;
        return this;
    }

    /**
     * Defines whether the Thread ID and name should be logged
     *
     * @param showThread whether the thread details are shown.
     * @return the mutated {@link Rules}
     */
    public Rules showThread(boolean showThread) {
        mShowThread = showThread;
        return this;
    }

    /**
     * Defines whether the timestamp should be logged
     *
     * @param showTimestamp whether the timestamp is shown.
     * @return the mutated {@link Rules}
     */
    public Rules showTimestamp(boolean showTimestamp) {
        mShowTimestamp = showTimestamp;
        return this;
    }

    /**
     * Defines whether the logs are only output if the unit test fails.
     *
     * @param onlyLogWhenTestFails whether the logs are only output when a test fails.
     * @return the mutated {@link Rules}
     */
    public Rules onlyLogWhenTestFails(boolean onlyLogWhenTestFails) {
        mOnlyLogWhenTestFails = onlyLogWhenTestFails;
        return this;
    }
}
