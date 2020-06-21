package net.lachlanmckee.timberjunit.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class LogTestWithNoRules {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {LogTester.LogType.VERBOSE, "Test", ""},
                {LogTester.LogType.DEBUG, "Test", ""},
                {LogTester.LogType.INFO, "Test", ""},
                {LogTester.LogType.WARN, "Test", ""},
                {LogTester.LogType.ERROR, "Test", ""},
        });
    }

    private final LogTester.LogType mLogType;
    private final String mMessage;
    private final String mExpectedOutput;

    public LogTestWithNoRules(LogTester.LogType logType, String message, String expectedOutput) {
        mLogType = logType;
        mMessage = message;
        mExpectedOutput = expectedOutput;
    }

    @Test
    public void givenOutputStreamSetup_whenLogExecuted_thenVerifyExpectedOutput() {
        // given
        OutputStream outputStream = LogTesterTestUtils.setupConsoleOutputStream();

        // when
        LogTester.log(mLogType, mMessage);

        // then
        LogTesterTestUtils.assertOutput(outputStream, mExpectedOutput);
    }
}
