package org.gethydrated.hydra.test.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.service.Service;
import org.gethydrated.hydra.api.service.ServiceContext;
import org.gethydrated.hydra.core.configuration.ConfigurationImpl;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 */
public class CLITestContext implements ServiceContext {

    /**
         * 
         */
    private final PrintStream ps;

    /**
         * 
         */
    private final ByteArrayOutputStream output;

    /**
         * 
         */
    private final ConfigurationImpl testConfig;

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
    public CLITestContext() {
        output = new ByteArrayOutputStream();
        ps = new PrintStream(output);
        testConfig = new ConfigurationImpl();
        testConfig.setString("Name", "Test");
        testConfig.setInteger("Network.Port", 1337);
        testConfig.setString("Network.Host", "local");
    }

    @Override
    public final void registerLocal(final String name, final Long id) {

    }

    @Override
    public final void registerGlobal(final String name, final Long id) {

    }

    @Override
    public final Long getLocalService(final String name) {
        return null;

    }

    @Override
    public final Long getGlobalService(final String name) {
        return null;

    }

    @Override
    public final Long startService(final String name) throws HydraException {
        return null;
    }

    @Override
    public final void stopService(final Long id) throws HydraException {

    }

    @Override
    public final Service getService() {
        return null;
    }

    @Override
    public final Configuration getConfiguration() {
        
        return testConfig;
    }

}
