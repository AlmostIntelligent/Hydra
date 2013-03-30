package org.gethydrated.hydra.chat;

import org.gethydrated.hydra.api.service.ServiceActivator;
import org.gethydrated.hydra.api.service.ServiceContext;

/**
 *
 */
public class BrokerActivator implements ServiceActivator {
    @Override
    public void start(ServiceContext context) throws Exception {
        System.out.println("broker started");
        context.registerGlobal("chat-broker", context.getSelf());
    }

    @Override
    public void stop(ServiceContext context) throws Exception {
        context.unregisterGlobal("chat-broker");
    }
}
