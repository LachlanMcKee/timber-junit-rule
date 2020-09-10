package net.lachlanmckee.timberjunit.sample;

import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

class LogTesterTestUtils {

    static OutputStream setupConsoleOutputStream() {
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        return outputStream;
    }

    static void assertOutput(OutputStream outputStream, String expected) {
        Assert.assertEquals(expected, outputStream.toString().trim());
    }

}
