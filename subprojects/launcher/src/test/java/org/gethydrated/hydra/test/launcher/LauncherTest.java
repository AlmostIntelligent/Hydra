package org.gethydrated.hydra.test.launcher;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.gethydrated.hydra.launcher.Launcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Launcher tests.
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public class LauncherTest {

    /**
     * System.out buffer.
     */
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    /**
     * Line seperator.
     */
    private static final String LINESEP = System.getProperty("line.separator");

    /**
     * Reset System.out to buffer.
     */
    @Before
    public final void setup() {
        System.setOut(new PrintStream(output));
    }

    /**
     * Test Usage.
     */
    @Test
    public final void testUsage() {
        Launcher.printUsage();
        assertTrue(output.toString().equals("Hydra Usage:" + LINESEP));
    }

    /**
     * Unregister buffer.
     */
    @After
    public final void teardown() {
        System.setOut(null);
    }
}
