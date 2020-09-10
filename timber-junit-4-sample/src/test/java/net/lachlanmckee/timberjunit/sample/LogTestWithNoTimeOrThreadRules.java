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
public class LogTestWithNoTimeOrThreadRules {
    @Rule
    public TimberTestRule mTimberTestRule = TimberTestRule.customRules(new Rules()
            .showThread(false)
            .showTimestamp(false)
            .onlyLogWhenTestFails(false));

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {LogTester.LogType.VERBOSE, "Test", "V/LogTester: Test"},
                {LogTester.LogType.DEBUG, "Test", "D/LogTester: Test"},
                {LogTester.LogType.INFO, "Test", "I/LogTester: Test"},
                {LogTester.LogType.WARN, "Test", "W/LogTester: Test"},
                {LogTester.LogType.ERROR, "Test", "E/LogTester: Test"},
        });
    }

    private final LogTester.LogType mLogType;
    private final String mMessage;
    private final String mExpectedOutput;

    public LogTestWithNoTimeOrThreadRules(LogTester.LogType logType, String message, String expectedOutput) {
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
