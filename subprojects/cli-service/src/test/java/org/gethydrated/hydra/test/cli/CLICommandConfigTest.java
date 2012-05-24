package org.gethydrated.hydra.test.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.cli.commands.CLICommand;
import org.gethydrated.hydra.cli.commands.CLICommandConfig;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class CLICommandConfigTest {

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
        dut = new CLICommandConfig(ctx);
    }

    /**
     * Test method for "configuration set".
     */
    @Test
    public final void testSet() {
        dut.parse("set Network.Host localhost");
        assertEquals("Network.Host = localhost", ctx.getOutput());
        try {
            assertEquals("localhost",
                    ctx.getConfigurationGetter().getString("Network.Host"));
        } catch (ConfigItemNotFoundException e) {
            fail("Configuration Item not found");
        }
    }

    /**
     * Test method for "configuration get".
     */
    @Test
    public final void testGet() {
        dut.parse("get Network.Port");
        assertEquals("1337", ctx.getOutput());
    }

    /**
     * Test method for "configuration list".
     */
    @Test
    public final void testList() {
        dut.parse("list Network");
        assertEquals("Port" + System.getProperty("line.separator") + "Host"
                + System.getProperty("line.separator"), ctx.getOutput());
    }

    /**
         * 
         */
    @Test
    public final void testGetEmptyKey() {
        dut.parse("get");
        assertEquals("No key given.", ctx.getOutput());
    }

    /**
         * 
         */
    @Test
    public final void testListEmptyKey() {
        dut.parse("list");
        assertEquals("No key given.", ctx.getOutput());
    }

    /**
         * 
         */
    @Test
    public final void testSetEmptyKey() {
        dut.parse("set");
        assertEquals("Not enough parameters.", ctx.getOutput());
    }

}
