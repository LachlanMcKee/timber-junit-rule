package net.lachlanmckee.timberjunit.sample;

import android.util.Log;

import net.lachlanmckee.timberjunit.Rules;
import net.lachlanmckee.timberjunit.TimberTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Enclosed.class)
public class LogTestWithMinPriorityRules {

    @RunWith(Parameterized.class)
    public static class DebugMin {
        @Rule
        public TimberTestRule mTimberTestRule = TimberTestRule.customRules(new Rules()
                .minPriority(Log.DEBUG)
                .showTimestamp(false)
                .onlyLogWhenTestFails(false));

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {LogTester.LogType.VERBOSE, "Test", ""},
                    {LogTester.LogType.DEBUG, "Test", "D/LogTester: Test"},
                    {LogTester.LogType.INFO, "Test", "I/LogTester: Test"},
                    {LogTester.LogType.WARN, "Test", "W/LogTester: Test"},
                    {LogTester.LogType.ERROR, "Test", "E/LogTester: Test"},
            });
        }

        private final LogTester.LogType mLogType;
        private final String mMessage;
        private final String mExpectedOutput;

        public DebugMin(LogTester.LogType logType, String message, String expectedOutput) {
            mLogType = logType;
            mMessage = message;
            mExpectedOutput = expectedOutput;
        }

        @Test
        public void givenOutputStreamSetup_whenLogExecutedOnMainThread_thenVerifyExpectedOutput() {
            // given
            OutputStream outputStream = LogTesterTestUtils.setupConsoleOutputStream();

            // when
            LogTester.log(mLogType, mMessage);

            // then
            LogTesterTestUtils.assertOutput(outputStream, mExpectedOutput);
        }
    }

    @RunWith(Parameterized.class)
    public static class InfoMin {
        @Rule
        public TimberTestRule mTimberTestRule = TimberTestRule.customRules(new Rules()
                .minPriority(Log.INFO)
                .showTimestamp(false)
                .onlyLogWhenTestFails(false));

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {LogTester.LogType.VERBOSE, "Test", ""},
                    {LogTester.LogType.DEBUG, "Test", ""},
                    {LogTester.LogType.INFO, "Test", "I/LogTester: Test"},
                    {LogTester.LogType.WARN, "Test", "W/LogTester: Test"},
                    {LogTester.LogType.ERROR, "Test", "E/LogTester: Test"},
            });
        }

        private final LogTester.LogType mLogType;
        private final String mMessage;
        private final String mExpectedOutput;

        public InfoMin(LogTester.LogType logType, String message, String expectedOutput) {
            mLogType = logType;
            mMessage = message;
            mExpectedOutput = expectedOutput;
        }

        @Test
        public void givenOutputStreamSetup_whenLogExecutedOnMainThread_thenVerifyExpectedOutput() {
            // given
            OutputStream outputStream = LogTesterTestUtils.setupConsoleOutputStream();

            // when
            LogTester.log(mLogType, mMessage);

            // then
            LogTesterTestUtils.assertOutput(outputStream, mExpectedOutput);
        }
    }

    @RunWith(Parameterized.class)
    public static class WarnMin {
        @Rule
        public TimberTestRule mTimberTestRule = TimberTestRule.customRules(new Rules()
                .minPriority(Log.WARN)
                .showTimestamp(false)
                .onlyLogWhenTestFails(false));

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {LogTester.LogType.VERBOSE, "Test", ""},
                    {LogTester.LogType.DEBUG, "Test", ""},
                    {LogTester.LogType.INFO, "Test", ""},
                    {LogTester.LogType.WARN, "Test", "W/LogTester: Test"},
                    {LogTester.LogType.ERROR, "Test", "E/LogTester: Test"},
            });
        }

        private final LogTester.LogType mLogType;
        private final String mMessage;
        private final String mExpectedOutput;

        public WarnMin(LogTester.LogType logType, String message, String expectedOutput) {
            mLogType = logType;
            mMessage = message;
            mExpectedOutput = expectedOutput;
        }

        @Test
        public void givenOutputStreamSetup_whenLogExecutedOnMainThread_thenVerifyExpectedOutput() {
            // given
            OutputStream outputStream = LogTesterTestUtils.setupConsoleOutputStream();

            // when
            LogTester.log(mLogType, mMessage);

            // then
            LogTesterTestUtils.assertOutput(outputStream, mExpectedOutput);
        }
    }

    @RunWith(Parameterized.class)
    public static class ErrorMin {
        @Rule
        public TimberTestRule mTimberTestRule = TimberTestRule.customRules(new Rules()
                .minPriority(Log.ERROR)
                .showTimestamp(false)
                .onlyLogWhenTestFails(false));

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {LogTester.LogType.VERBOSE, "Test", ""},
                    {LogTester.LogType.DEBUG, "Test", ""},
                    {LogTester.LogType.INFO, "Test", ""},
                    {LogTester.LogType.WARN, "Test", ""},
                    {LogTester.LogType.ERROR, "Test", "E/LogTester: Test"},
            });
        }

        private final LogTester.LogType mLogType;
        private final String mMessage;
        private final String mExpectedOutput;

        public ErrorMin(LogTester.LogType logType, String message, String expectedOutput) {
            mLogType = logType;
            mMessage = message;
            mExpectedOutput = expectedOutput;
        }

        @Test
        public void givenOutputStreamSetup_whenLogExecutedOnMainThread_thenVerifyExpectedOutput() {
            // given
            OutputStream outputStream = LogTesterTestUtils.setupConsoleOutputStream();

            // when
            LogTester.log(mLogType, mMessage);

            // then
            LogTesterTestUtils.assertOutput(outputStream, mExpectedOutput);
        }
    }
}
