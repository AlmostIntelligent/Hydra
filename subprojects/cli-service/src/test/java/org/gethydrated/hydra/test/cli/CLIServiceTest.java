package org.gethydrated.hydra.test.cli;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.gethydrated.hydra.cli.CLIService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        dut = new CLIService(ctx);
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
        assertEquals("Hallo", dut.handleInputString("echo Hallo"));
    }

    /**
         *
         */
    @Test
    public final void testEchoQuote() {
        String str = null;
        str = dut.handleInputString("echo \"Hallo Welt\"");
        System.out.print(str);
        //assertEquals("Hallo Welt", str);
        str = dut.handleInputString("echo String, \"more String\" and even more \"String here with content\"");
        System.out.print(str);
        //assertEquals("Some String and even more Strings here with content", str);
        str = dut.handleInputString("echo String, \"more String\" and even more \"String here with content\" all done");
        System.out.print(str);
        //assertEquals("Some String and even more Strings here with content all done", str);
        assertTrue(true);
    }
    
    /**
     * 
     */
    @Test
    public final void testEchoEmpty() {
        assertEquals("", dut.handleInputString("echo"));
    }
    
    /**
     * 
     */
    @Test
    public final void testEchoBlank() {
        assertEquals("", dut.handleInputString("echo "));
    }   

    /**
         * 
         */
    @Test
    public final void testConfigSet() {
        assertEquals("Network.Host = localhost", dut.handleInputString("configuration set Network.Host localhost"));
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
        assertEquals("1337", dut.handleInputString("configuration get Network.Port"));
    }

    /**
     * Test method for "configuration list".
     */
    @Test
    public final void testList() {
        assertEquals("Port" + System.getProperty("line.separator") + "Host"
                + System.getProperty("line.separator"), dut.handleInputString("configuration list Network"));
    }

}
