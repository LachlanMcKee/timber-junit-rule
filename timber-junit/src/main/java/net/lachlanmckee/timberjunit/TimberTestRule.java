package net.lachlanmckee.timberjunit;

import android.util.Log;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

/**
 * A JUnit {@link TestRule} that plants a {@link Timber.Tree} before a test is executed,
 * and uproots the {@link Timber.Tree} afterwards.
 * <p>
 * This is useful as a planted {@link Timber.Tree} would exist between test classes, which may be
 * undesirable.
 */
public class TimberTestRule implements TestRule {
    private final Rules mRules;

    private TimberTestRule(Rules rules) {
        mRules = rules;
    }

    /**
     * @return a {@link TimberTestRule} that logs messages of any priority regardless of the
     * test outcome
     */
    public static TimberTestRule logAllAlways() {
        return new Rules()
                .onlyLogWhenTestFails(false)
                .build();
    }

    /**
     * @return a {@link TimberTestRule} that logs all messages, regardless of their priority.
     */
    public static TimberTestRule logAllWhenTestFails() {
        return new Rules()
                .build();
    }

    /**
     * @return a {@link TimberTestRule} that only logs error messages regardless of the test
     * outcome.
     */
    public static TimberTestRule logErrorsAlways() {
        return new Rules()
                .onlyLogWhenTestFails(false)
                .minPriority(Log.ERROR)
                .build();
    }

    /**
     * @return a {@link TimberTestRule} that only logs error messages when a unit test fails.
     */
    public static TimberTestRule logErrorsWhenTestFails() {
        return new Rules()
                .minPriority(Log.ERROR)
                .build();
    }

    /**
     * @return a {@link Rules} class which is used as a builder to create a {@link TimberTestRule}.
     */
    public static Rules builder() {
        return new Rules();
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new TimberStatement(base, mRules);
    }

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
    public static final class Rules {
        private int mMinPriority;
        private boolean mShowThread;
        private boolean mShowTimestamp;
        private boolean mOnlyLogWhenTestFails;

        Rules() {
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

        /**
         * Builds the JUnit test rule based on the defined rules.
         *
         * @return a new JUnit test rule instance.
         */
        public TimberTestRule build() {
            return new TimberTestRule(this);
        }
    }

    /**
     * The JUnit statement that plants before the unit test, and uproots it after completion.
     */
    private static class TimberStatement extends Statement {
        private final Statement mNext;
        private final BufferedJUnitTimberTree mTree;

        TimberStatement(Statement base, Rules rules) {
            mNext = base;
            mTree = new BufferedJUnitTimberTree(rules);
        }

        @Override
        public void evaluate() throws Throwable {
            Timber.plant(mTree);
            try {
                mNext.evaluate();

            } catch (Throwable t) {
                mTree.flushLogs();
                throw t;

            } finally {
                // Ensure the tree is removed to avoid duplicate logging.
                Timber.uproot(mTree);
            }
        }
    }

    /**
     * A Timber tree that logs to the Java System.out rather than using the Android logger.
     */
    private static final class BufferedJUnitTimberTree extends Timber.DebugTree {
        private final Rules mRules;
        private final List<String> mLogMessageBuffer;
        private final Object bufferLock = new Object();

        BufferedJUnitTimberTree(Rules rules) {
            mRules = rules;
            mLogMessageBuffer = new ArrayList<>();
        }

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            String logMessage = createLogMessage(mRules, priority, tag, message);
            if (logMessage == null) {
                return;
            }

            if (mRules.mOnlyLogWhenTestFails) {
                synchronized (bufferLock) {
                    mLogMessageBuffer.add(logMessage);
                }

            } else {
                System.out.println(logMessage);
            }
        }

        /**
         * Flushes all the previously stored log messages.
         */
        private void flushLogs() {
            synchronized (bufferLock) {
                Iterator<String> iterator = mLogMessageBuffer.iterator();
                while (iterator.hasNext()) {
                    System.out.println(iterator.next());
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Creates a log message based on the rules and Timber log details.
     *
     * @param rules    the rules used to construct the message.
     * @param priority the priority of the log.
     * @param tag      the tag of the log.
     * @param message  the message of the log.
     * @return a log message (may be null).
     */
    private static String createLogMessage(Rules rules, int priority, String tag, String message) {
        // Avoid logging if the priority is too low.
        if (priority < rules.mMinPriority) {
            return null;
        }

        // Obtain the correct log type prefix.
        final char type;
        switch (priority) {
            case Log.VERBOSE:
                type = 'V';
                break;

            case Log.DEBUG:
                type = 'D';
                break;

            case Log.INFO:
                type = 'I';
                break;

            case Log.WARN:
                type = 'W';
                break;

            case Log.ERROR:
            default:
                type = 'E';
                break;
        }

        StringBuilder logBuilder = new StringBuilder();

        if (rules.mShowTimestamp) {
            logBuilder
                    .append(THREAD_LOCAL_FORMAT.get().format(System.currentTimeMillis()))
                    .append(" ");
        }

        if (rules.mShowThread) {
            Thread thread = Thread.currentThread();
            logBuilder
                    .append(thread.getId())
                    .append("/")
                    .append(thread.getName())
                    .append(" ");
        }

        logBuilder
                .append(type)
                .append("/")
                .append(tag)
                .append(": ")
                .append(message);

        return logBuilder.toString();
    }

    /**
     * A thread local is used as the {@link DateFormat} class is not thread-safe.
     */
    private static final ThreadLocal<DateFormat> THREAD_LOCAL_FORMAT =
            new ThreadLocal<DateFormat>() {
                @Override
                protected DateFormat initialValue() {
                    return new SimpleDateFormat("HH:mm:ss.SSS", Locale.ENGLISH);
                }
            };

}
