package org.gethydrated.hydra.http;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.ServiceActivator;
import org.gethydrated.hydra.api.service.ServiceContext;

/**
 *
 */
public class SampleActivator implements ServiceActivator {
    @Override
    public void start(ServiceContext context) throws Exception {
        publishSelf(context);
    }

    @Override
    public void stop(ServiceContext context) throws Exception {
    }

    @SuppressWarnings("unchecked")
    private void publishSelf(ServiceContext context) throws Exception {
        SID broker;
        try {
            broker = context.getGlobalService("http-broker");
        } catch (HydraException e) {
            broker = context.startService("http::broker");
        }
        broker.tell("http", context.getSelf());
        context.monitor(broker);
    }
}
