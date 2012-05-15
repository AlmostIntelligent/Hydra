package org.gethydrated.hydra.test.cli;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
                dut = new CLICommandConfig(ps, null);
        }


        
        /**
         * Test method for "configuration set".
         */
        @Test
        public final void testSet() {
                dut.parseCommand("set key value");
                assertEquals("key=value", getOutput());
        }
        
        /**
         * Test method for "configuration get".
         */
        @Test 
        public final void testGet() {
                dut.parseCommand("get key");
                assertEquals("key", getOutput());
        }

}
