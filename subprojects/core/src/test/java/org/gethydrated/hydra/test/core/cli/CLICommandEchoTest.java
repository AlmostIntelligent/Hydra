package org.gethydrated.hydra.test.core.cli;

import static org.junit.Assert.assertEquals;

import org.gethydrated.hydra.core.cli.commands.CLICommand;
import org.gethydrated.hydra.core.cli.commands.CLICommandEcho;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class CLICommandEchoTest {

    /**
         * 
         */
    private CLICommand dut;

    /**
         * 
         */
    private CLITestContext ctx;

    /**
     * 
     * @throws Exception .
     */
    @Before
    public final void setUp() throws Exception {
        ctx = new CLITestContext();
        dut = new CLICommandEcho(null, null);
    }

    /**
     * 
     * @throws Exception .
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for command execution.
     */
    @Test
    public final void testExecuteCommand() {
        final String[] s = new String[1];
        s[0] = "Test Execute Echo";
        dut.execute(s);
        assertEquals(s[0], ctx.getResult().trim());
    }

    /**
     * Test method for command parsing.
     */
    @Test
    public final void testParseCommandString() {
        final String s = "Test Parse Echo";
        dut.parse(s);
        assertEquals(s, ctx.getResult().trim());
    }

    /**
     * Test method for empty string.
     */
    @Test
    public final void testParseEmptyString() {
        final String s = "";
        dut.parse(s);
        assertEquals(s, ctx.getResult().trim());
    }

    /**
     * Test method for empty string.
     */
    @Test
    public final void testExecuteEmptyString() {
        final String[] s = new String[1];
        s[0] = "";
        dut.execute(s);
        assertEquals("", ctx.getResult().trim());
    }

    /**
     * Test method for empty string.
     */
    @Test
    public final void testParseBlankString() {
        final String s = " ";
        dut.parse(s);
        assertEquals("", ctx.getResult().trim());
    }

    /**
     * Test method for empty string.
     */
    @Test
    public final void testExecuteBlankString() {
        final String[] s = new String[1];
        s[0] = " ";
        dut.execute(s);
        assertEquals("", ctx.getResult().trim());
    }
}
