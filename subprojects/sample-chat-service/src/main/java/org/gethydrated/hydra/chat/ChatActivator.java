package org.gethydrated.hydra.chat;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.ServiceActivator;
import org.gethydrated.hydra.api.service.ServiceContext;

/**
 *
 */
public class ChatActivator implements ServiceActivator {

    private ChatGUI gui;

    private ServiceContext context;

    @Override
    public void start(final ServiceContext context) throws Exception {
        this.context = context;
        this.gui = new ChatGUI(context);
        SID broker;
        try {
            broker = context.getGlobalService("chat-broker");
        } catch (HydraException e) {
            broker = context.startService("chat::broker");
        }
        broker.tell("", context.getSelf());

        gui.setVisible(true);
    }

    @Override
    public void stop(ServiceContext context) throws Exception {
        gui.setVisible(false);
        gui.dispose();
    }

}
