package org.gethydrated.hydra.test.core.cli;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.core.cli.CLIService;
import org.junit.After;
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
public class CLIServiceTest {

    /**
         * 
         */
    private CLIService dut;

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
        dut = new CLIService(null);
    }

    /**
     * 
     * @throws Exception .
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
         * 
         */
    @Test
    public final void testEcho() {
        assertEquals("Hallo", dut.handleInputString("echo Hallo").trim());
    }

    /**
         *
         */
    @Test
    public final void testEchoQuote() {
        String str = null;
        str = dut.handleInputString("echo \"Hallo Welt\"");
        assertEquals("Hallo Welt", str.trim());
        str = dut.handleInputString("echo Some String, \"more Strings\" and even more \"Strings here with content\"");
        assertEquals("Some String, more Strings and even more Strings here with content", str.trim());
        str = dut.handleInputString("echo Some String, \"more Strings\" and even more \"Strings here with content\" all done");
        assertEquals("Some String, more Strings and even more Strings here with content all done", str.trim());
    }

    /**
     * 
     */
    @Test
    public final void testEchoEmpty() {
        assertEquals("", dut.handleInputString("echo").trim());
    }
    
    /**
     * 
     */
    @Test
    public final void testEchoBlank() {
        assertEquals("", dut.handleInputString("echo ").trim());
    }   

    /**
         * 
         */
    @Test
    public final void testConfigSet() {
        assertEquals("Network.Host = localhost", dut.handleInputString("configuration set Network.Host localhost").trim());
        try {
            assertEquals("localhost",
                    ctx.getConfiguration().getString("Network.Host"));
        } catch (ConfigItemNotFoundException e) {
            fail("Configuration Item not found");
        }
    }

    /**
         * 
         */
    @Test
    public final void testConfigGet() {
        assertEquals("1337", dut.handleInputString("configuration get Network.Port").trim());
    }

    /**
     * Test method for "configuration list".
     */
    @Test
    public final void testList() {
        assertEquals("Port" + System.getProperty("line.separator") + "Host"
                + System.getProperty("line.separator"), dut.handleInputString("configuration list Network"));
    }

    @Test
    public final void testVariables() {

        assertEquals("hallo", dut.handleInputString("$var = echo hallo").trim());
        assertEquals("hallo", dut.handleInputString("echo $var").trim());

    }



}
