package net.lachlanmckee.timberjunit.sample;

import net.lachlanmckee.timberjunit.Rules;
import net.lachlanmckee.timberjunit.TimberTestRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.RuleChain;

import java.io.OutputStream;

import static org.junit.Assert.fail;

public class LogTestWithOnlyOnTestFailure {

    private ExpectedException expectedException = ExpectedException.none();

    @Rule
    public RuleChain chain = RuleChain
            .outerRule(expectedException)
            .around(TimberTestRule.customRules(new Rules()
                    .showThread(false)
                    .showTimestamp(false)
                    .onlyLogWhenTestFails(true)));

    private static OutputStream outputStream;

    @BeforeClass
    public static void setupConsoleOutput() {
        outputStream = LogTesterTestUtils.setupConsoleOutputStream();
    }

    @Test
    public void deliberatelyFailingUnitTest() throws InterruptedException {
        LogTester.log(LogTester.LogType.ERROR, "Test");

        expectedException.expect(AssertionError.class);
        // given

        // when

        // then
        fail();
    }

    @AfterClass
    public static void verifyErrorIsOutput() {
        LogTesterTestUtils.assertOutput(outputStream, "E/LogTester: Test");
    }
}
