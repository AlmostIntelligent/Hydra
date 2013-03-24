package org.gethydrated.hydra.test.core.cli;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.core.cli.commands.CLICommand;
import org.gethydrated.hydra.core.cli.commands.CLICommandConfig;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
        dut = new CLICommandConfig(null, null);
    }

    /**
     * Test method for "configuration set".
     */
    @Test
    public final void testSet() {
        dut.parse("set Network.Host localhost");
        assertEquals("Network.Host = localhost" + System.getProperty("line.separator"), ctx.getOutput());
        try {
            assertEquals("localhost",
                    ctx.getConfiguration().getString("Network.Host"));
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
        assertEquals("1337" + System.getProperty("line.separator"), ctx.getOutput());
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
        assertEquals("No key given." + System.getProperty("line.separator"), ctx.getOutput());
    }

    /**
         * 
         */
    @Test
    public final void testListEmptyKey() {
        dut.parse("list");
        assertEquals("No key given." + System.getProperty("line.separator"), ctx.getOutput());
    }

    /**
         * 
         */
    @Test
    public final void testSetEmptyKey() {
        dut.parse("set");
        assertEquals("Not enough parameters." + System.getProperty("line.separator"), ctx.getOutput());
    }

}
