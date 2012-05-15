package org.gethydrated.hydra.test.cli;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
                dut = new CLICommandEcho(ps, null);
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
                assertEquals(getOutput().trim(), s[0]);
        }
        
        /**
         * Test method for command parsing.
         */
        @Test
        public final void testParseCommandString() {
                String s = "Test Parse Echo";
                dut.parseCommand(s);
                assertEquals(getOutput().trim(), s);
        }
}
