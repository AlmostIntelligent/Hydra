package org.gethydrated.hydra.test.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.Future;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.service.MessageHandler;
import org.gethydrated.hydra.api.service.SIDFactory;
import org.gethydrated.hydra.api.service.ServiceContext;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.config.ConfigurationImpl;

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
    public final SID getOutput() {
        return new SID() {

            @Override
            public void tell(Object message, SID sender) {
                ps.print(message.toString());
            }

            @Override
            public Future<?> ask(Object message) {
                return null;
            }
        };
    }

    public String getResult() {
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
    public SIDFactory getSIDFactory() {
        return null;
    }

    @Override
    public final SID startService(final String name) throws HydraException {
        return null;
    }

    @Override
    public final void stopService(final SID id) throws HydraException {

    }

    @Override
    public SID getSelf() {
        return null;
    }

    @Override
    public final Configuration getConfiguration() {
        
        return testConfig;
    }

    @Override
    public <T> void registerMessageHandler(Class<T> classifier, MessageHandler<T> messageHandler) {

    }

    @Override
    public void subscribeEvent(Class<?> classifier) {

    }

}
