package net.lachlanmckee.timberjunit.sample;

import timber.log.Timber;

class LogTester {
    static void log(LogType logType, String message) {
        switch (logType) {
            case VERBOSE:
                Timber.v(message);
                break;

            case DEBUG:
                Timber.d(message);
                break;

            case INFO:
                Timber.i(message);
                break;

            case WARN:
                Timber.w(message);
                break;

            case ERROR:
                Timber.e(message);
                break;
        }
    }

    enum LogType {
        VERBOSE, DEBUG, INFO, WARN, ERROR
    }
}
