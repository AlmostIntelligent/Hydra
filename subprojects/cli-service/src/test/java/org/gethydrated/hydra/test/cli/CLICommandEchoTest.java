package org.gethydrated.hydra.test.cli;

import static org.junit.Assert.assertEquals;

import org.gethydrated.hydra.cli.commands.CLICommand;
import org.gethydrated.hydra.cli.commands.CLICommandEcho;
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
        dut = new CLICommandEcho(ctx);
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
        String[] s = new String[1];
        s[0] = "Test Execute Echo";
        dut.executeCommand(s);
        assertEquals(s[0], ctx.getOutput().trim());
    }

    /**
     * Test method for command parsing.
     */
    @Test
    public final void testParseCommandString() {
        String s = "Test Parse Echo";
        dut.parseCommand(s);
        assertEquals(s, ctx.getOutput().trim());
    }
}
