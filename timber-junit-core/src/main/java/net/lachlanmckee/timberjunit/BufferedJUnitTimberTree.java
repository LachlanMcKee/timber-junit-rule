package net.lachlanmckee.timberjunit;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

/**
 * A Timber tree that logs to the Java System.out rather than using the Android logger.
 */
public final class BufferedJUnitTimberTree extends Timber.DebugTree {
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
    public void flushLogs() {
        synchronized (bufferLock) {
            Iterator<String> iterator = mLogMessageBuffer.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
                iterator.remove();
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
                    return new SimpleDateFormat("HH:mm:ss:SSSSSSS", Locale.ENGLISH);
                }
            };
}
