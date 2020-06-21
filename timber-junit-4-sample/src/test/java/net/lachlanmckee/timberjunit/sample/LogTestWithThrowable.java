package net.lachlanmckee.timberjunit.sample;

import net.lachlanmckee.timberjunit.Rules;
import net.lachlanmckee.timberjunit.TimberTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class LogTestWithThrowable {
    private static final String EXCEPTION_OUTPUT = "net.lachlanmckee.timberjunit.sample.LogTestWithThrowable$NoStackTraceThrowable";

    @Rule
    public TimberTestRule mTimberTestRule = new TimberTestRule(new Rules()
            .showThread(false)
            .showTimestamp(false)
            .onlyLogWhenTestFails(false));

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Throwable foo = new NoStackTraceThrowable();
        return Arrays.asList(new Object[][]{
                {LogTester.LogType.VERBOSE, foo, "Test", "V/LogTester: Test\n" + EXCEPTION_OUTPUT},
                {LogTester.LogType.DEBUG, foo, "Test", "D/LogTester: Test\n" + EXCEPTION_OUTPUT},
                {LogTester.LogType.INFO, foo, "Test", "I/LogTester: Test\n" + EXCEPTION_OUTPUT},
                {LogTester.LogType.WARN, foo, "Test", "W/LogTester: Test\n" + EXCEPTION_OUTPUT},
                {LogTester.LogType.ERROR, foo, "Test", "E/LogTester: Test\n" + EXCEPTION_OUTPUT}
        });
    }

    private final LogTester.LogType mLogType;
    private final Throwable mThrowable;
    private final String mMessage;
    private final String mExpectedOutput;

    public LogTestWithThrowable(LogTester.LogType logType, Throwable throwable, String message,
                                String expectedOutput) {
        mLogType = logType;
        mThrowable = throwable;
        mMessage = message;
        mExpectedOutput = expectedOutput;
    }

    @Test
    public void givenOutputStreamSetup_whenLogExecuted_thenVerifyExpectedOutput() {
        // given
        OutputStream outputStream = LogTesterTestUtils.setupConsoleOutputStream();

        // when
        LogTester.log(mLogType, mMessage, mThrowable);

        // then
        LogTesterTestUtils.assertOutput(outputStream, mExpectedOutput);
    }

    private static final class NoStackTraceThrowable extends Throwable {
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }
}
