package org.gethydrated.hydra.test.core.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.Future;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.service.MessageHandler;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.SIDFactory;
import org.gethydrated.hydra.api.service.ServiceContext;
import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.config.ConfigurationImpl;
import org.gethydrated.hydra.core.sid.InternalSID;

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
    @Override
    public final SID getOutput() {
        return new InternalSID() {

            @Override
            public void tell(final Object message, final SID sender) {
                ps.print(message.toString());
            }

            @Override
            public Future<?> ask(final Object message) {
                return null;
            }

            @Override
            public USID getUSID() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public ActorRef getRef() {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

    /**
     * 
     * @return test result.
     */
    public final String getResult() {
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
    public void registerLocal(final String name, final SID id)
            throws HydraException {

    }

    @Override
    public void registerGlobal(final String name, final SID id)
            throws HydraException {

    }

    @Override
    public void unregisterLocal(final String name) throws HydraException {

    }

    @Override
    public void unregisterGlobal(final String name) throws HydraException {

    }

    @Override
    public final SID getLocalService(final String name) throws HydraException {
        return null;
    }

    @Override
    public final SID getGlobalService(final String name) throws HydraException {
        return null;
    }

    @Override
    public final SIDFactory getSIDFactory() {
        return null;
    }

    @Override
    public void link(final SID sid1, final SID sid2) {

    }

    @Override
    public void unlink(final SID sid1, final SID sid2) {

    }

    @Override
    public void monitor(final SID sid1, final SID sid2) {

    }

    @Override
    public void unmonitor(final SID sid1, final SID sid2) {

    }

    @Override
    public final SID startService(final String name) throws HydraException {
        return null;
    }

    @Override
    public final SID getService(final String name) {
        return null;
    }

    @Override
    public final SID getService(final USID usid) {
        return null;
    }

    @Override
    public final void stopService(final SID id) throws HydraException {

    }

    @Override
    public final SID getSelf() {
        return null;
    }

    @Override
    public final Configuration getConfiguration() {

        return testConfig;
    }

    @Override
    public <T> void registerMessageHandler(final Class<T> classifier,
            final MessageHandler<T> messageHandler) {

    }

    @Override
    public void subscribeEvent(final Class<?> classifier) {

    }

}
