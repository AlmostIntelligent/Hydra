package org.gethydrated.hydra.test.cli;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.configuration.ConfigurationGetter;
import org.gethydrated.hydra.api.configuration.ConfigurationSetter;
import org.gethydrated.hydra.api.platform.Platform;
import org.gethydrated.hydra.api.service.Service;
import org.gethydrated.hydra.api.service.ServiceContext;

import static org.junit.Assert.assertEquals;

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
         * @author Hanno Sternberg
         *
         */
        private class TestContext implements ServiceContext {
                
                /**
                 * 
                 */
                private PrintStream ps;
                
                /**
                 * 
                 */
                private ByteArrayOutputStream output;
                
                /**
                 * 
                 */
                private Configuration testConfig;
                
                /**
                 * 
                 * @return OutputStream.
                 */
                public final String getOutput() {
                        return output.toString();
                }
                
                /**
                 * 
                 */
                public TestContext() {
                        output = new ByteArrayOutputStream();
                        ps = new PrintStream(output);
                        testConfig = new Configuration();
                }

                @Override
                public void registerLocal() {
                        // TODO Auto-generated method stub
                        
                }

                @Override
                public void registerGlobal() {
                        // TODO Auto-generated method stub
                        
                }

                @Override
                public void getLocalService() {
                        // TODO Auto-generated method stub
                        
                }

                @Override
                public void getGlobalService() {
                        // TODO Auto-generated method stub
                        
                }

                @Override
                public void startService() throws HydraException {
                        // TODO Auto-generated method stub
                        
                }

                @Override
                public void stopService() throws HydraException {
                        // TODO Auto-generated method stub
                        
                }

                @Override
                public Platform getPlatform() {
                        // TODO Auto-generated method stub
                        return null;
                }

                @Override
                public Service getService() {
                        // TODO Auto-generated method stub
                        return null;
                }

                @Override
                public PrintStream getOutputStream() {
                        return ps;
                }

                @Override
                public InputStream getInputStream() {
                        return System.in;
                }

                @Override
                public ConfigurationGetter getConfigurationGetter() {
                        // TODO Auto-generated method stub
                        return testConfig;
                }

                @Override
                public ConfigurationSetter getConfigurationSetter() {
                        // TODO Auto-generated method stub
                        return testConfig;
                }
        }
        
        /**
         * 
         */
        private CLIService dut;
        
        /**
         * 
         */
        private TestContext ctx;
        
        /**
         * 
         * @throws Exception .
         */
        @Before
        public final void setUp() throws Exception {
                ctx = new TestContext();
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
                dut.handleInputString("configuration set key value");
                assertEquals("key=value", ctx.getOutput());
        }
        
        /**
         * 
         */
        @Test
        public final void testConfigGet() {
                dut.handleInputString("configuration get key");
                assertEquals("key", ctx.getOutput());
        }

}
