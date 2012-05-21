package org.gethydrated.hydra.test.cli;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.configuration.ConfigurationGetter;
import org.gethydrated.hydra.api.configuration.ConfigurationSetter;
import org.gethydrated.hydra.api.platform.Platform;
import org.gethydrated.hydra.api.service.Service;
import org.gethydrated.hydra.api.service.ServiceContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.gethydrated.hydra.cli.CLIService;
import org.gethydrated.hydra.core.config.Configuration;
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
                dut.handleInputString("echo Hallo");
                assertEquals("Hallo", ctx.getOutput().trim());
        }
        
        /**
         * 
         */
        @Test
        public final void testConfigSet() {
                dut.handleInputString("configuration set Network.Host localhost");
                assertEquals("Network.Host = localhost", ctx.getOutput());
                try {
                        assertEquals("localhost", ctx.getConfigurationGetter()
                                        .getString("Network.Host"));
                } catch (ConfigItemNotFoundException e) {
                        fail("Configuration Item not found");
                }
        }
        
        /**
         * 
         */
        @Test
        public final void testConfigGet() {
                dut.handleInputString("configuration get Network.Port");
                assertEquals("1337", ctx.getOutput());
        }
        
        /**
         * Test method for "configuration list".
         */
        @Test
        public final void testList() {
                dut.handleInputString("configuration list Network");
                assertEquals("Port" + System.getProperty("line.separator")
                                + "Host" + System.getProperty("line.separator"),
                                ctx.getOutput());
        }

}