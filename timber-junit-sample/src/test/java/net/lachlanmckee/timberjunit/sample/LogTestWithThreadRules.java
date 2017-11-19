package net.lachlanmckee.timberjunit.sample;

import net.lachlanmckee.timberjunit.TimberTestRule;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

@RunWith(Parameterized.class)
public class LogTestWithThreadRules {
    @Rule
    public TimberTestRule mTimberTestRule = TimberTestRule.builder()
            .showThread(true)
            .showTimestamp(false)
            .onlyLogWhenTestFails(false)
            .build();

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

    public LogTestWithThreadRules(LogTester.LogType logType, String message, String expectedOutput) {
        mLogType = logType;
        mMessage = message;
        mExpectedOutput = expectedOutput;
    }

    private String getThreadDetailsPrefix() {
        Thread thread = Thread.currentThread();
        return thread.getId() + "/" + thread.getName();
    }

    @Test
    public void givenOutputStreamSetup_whenLogExecutedOnMainThread_thenVerifyExpectedOutput() {
        // given
        OutputStream outputStream = LogTesterTestUtils.setupConsoleOutputStream();

        // when
        LogTester.log(mLogType, mMessage);

        // then
        LogTesterTestUtils.assertOutput(outputStream, getThreadDetailsPrefix() + " " + mExpectedOutput);
    }

    @Test
    public void givenOutputStreamSetup_whenLogExecutedOnDifferentThread_thenVerifyExpectedOutput() throws InterruptedException {
        // given
        OutputStream outputStream = LogTesterTestUtils.setupConsoleOutputStream();

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        // when
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogTester.log(mLogType, mMessage);
                countDownLatch.countDown();
            }
        }).start();
        countDownLatch.await();

        // then
        String output = outputStream.toString().trim();
        String[] split = output.split("\\s");

        // Ensure the thread id and name are not referring to main.
        Assert.assertNotEquals(getThreadDetailsPrefix(), split[0]);

        // Compare the rest of the string after removing the thread details.
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < split.length; i++) {
            builder.append(split[i]);
        }

        Assert.assertNotEquals(mExpectedOutput, builder.toString());
    }
}
