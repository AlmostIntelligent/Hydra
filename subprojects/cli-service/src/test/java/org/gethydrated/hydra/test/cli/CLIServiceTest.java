package org.gethydrated.hydra.test.cli;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
        private ByteArrayOutputStream output;
        
        /**
         * 
         * @return OutputStream.
         */
        public final String getOutput() {
                return output.toString();
        }
        
        /**
         * 
         * @throws Exception .
         */
        @Before
        public final void setUp() throws Exception {
                output = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(output);
                dut = new CLIService(null, ps);
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
                assertEquals("Hallo", getOutput().trim());
        }
        
        /**
         * 
         */
        @Test
        public final void testConfigSet() {
                dut.handleInputString("configuration set key value");
                assertEquals("key=value", getOutput());
        }
        
        /**
         * 
         */
        @Test
        public final void testConfigGet() {
                dut.handleInputString("configuration get key");
                assertEquals("key", getOutput());
        }

}
