package org.gethydrated.hydra.test.core.cli;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.service.*;
import org.gethydrated.hydra.config.ConfigurationImpl;
import org.gethydrated.hydra.core.sid.LocalSID;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.Future;

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
        return new LocalSID(null,null, null) {

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
    public void registerLocal(String name, SID id) throws HydraException {

    }

    @Override
    public void registerGlobal(String name, SID id) throws HydraException {

    }

    @Override
    public void unregisterLocal(String name) throws HydraException {

    }

    @Override
    public void unregisterGlobal(String name) throws HydraException {

    }

    @Override
    public SID getLocalService(String name) throws HydraException {
        return null;
    }

    @Override
    public SID getGlobalService(String name) throws HydraException {
        return null;
    }

    @Override
    public SIDFactory getSIDFactory() {
        return null;
    }

    @Override
    public void link(SID sid1, SID sid2) {

    }

    @Override
    public void unlink(SID sid1, SID sid2) {

    }

    @Override
    public void monitor(SID sid1, SID sid2) {

    }

    @Override
    public void unmonitor(SID sid1, SID sid2) {

    }

    @Override
    public final SID startService(final String name) throws HydraException {
        return null;
    }

    @Override
    public SID getService(String name) {
        return null;
    }

    @Override
    public SID getService(USID usid) {
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
