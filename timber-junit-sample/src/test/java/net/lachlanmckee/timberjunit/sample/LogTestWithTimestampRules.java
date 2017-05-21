package net.lachlanmckee.timberjunit.sample;

import net.lachlanmckee.timberjunit.TimberTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.TimeZone;

@RunWith(PowerMockRunner.class)
@PrepareForTest({System.class, TimberTestRule.class})
@PowerMockRunnerDelegate(Parameterized.class)
public class LogTestWithTimestampRules {
    @Rule
    public TimberTestRule mTimberTestRule = TimberTestRule.builder()
            .showTimestamp(true)
            .build();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {LogTester.LogType.VERBOSE, "Test", "02:40:00:0000000 V/LogTester: Test"},
                {LogTester.LogType.DEBUG, "Test", "02:40:00:0000000 D/LogTester: Test"},
                {LogTester.LogType.INFO, "Test", "02:40:00:0000000 I/LogTester: Test"},
                {LogTester.LogType.WARN, "Test", "02:40:00:0000000 W/LogTester: Test"},
                {LogTester.LogType.ERROR, "Test", "02:40:00:0000000 E/LogTester: Test"},
        });
    }

    private final LogTester.LogType mLogType;
    private final String mMessage;
    private final String mExpectedOutput;

    public LogTestWithTimestampRules(LogTester.LogType logType, String message, String expectedOutput) {
        mLogType = logType;
        mMessage = message;
        mExpectedOutput = expectedOutput;
    }

    @Test
    public void givenOutputStreamSetup_whenLogExecuted_thenVerifyExpectedOutput() {
        // given
        PowerMockito.mockStatic(System.class);
        PowerMockito.when(System.currentTimeMillis()).thenReturn(1500000000000L);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

        OutputStream outputStream = LogTesterTestUtils.setupConsoleOutputStream();

        // when
        LogTester.log(mLogType, mMessage);

        // then
        LogTesterTestUtils.assertOutput(outputStream, mExpectedOutput);
    }
}
