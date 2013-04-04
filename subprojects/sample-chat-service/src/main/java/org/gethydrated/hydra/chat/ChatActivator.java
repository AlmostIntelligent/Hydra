package org.gethydrated.hydra.chat;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.event.NodeDown;
import org.gethydrated.hydra.api.event.ServiceDown;
import org.gethydrated.hydra.api.service.MessageHandler;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.ServiceActivator;
import org.gethydrated.hydra.api.service.ServiceContext;
import org.gethydrated.hydra.chat.messages.Discover;
import org.gethydrated.hydra.chat.messages.Message;
import org.gethydrated.hydra.chat.messages.NewClient;
import org.gethydrated.hydra.chat.messages.Renamed;

import javax.swing.*;

/**
 *
 */
public class ChatActivator implements ServiceActivator {

    private ChatGUI gui;

    private ServiceContext context;

    private SID broker;

    @Override
    public void start(final ServiceContext context) throws Exception {
        this.context = context;
        gui = new ChatGUI(context);
        context.registerMessageHandler(NewClient.class, new MessageHandler<NewClient>() {
            @Override
            public void handle(NewClient message, SID sender) {
                gui.addClient(context.getService(message.getUSID()));
            }
        });
        context.registerMessageHandler(ServiceDown.class, new MessageHandler<ServiceDown>() {
            @Override
            public void handle(ServiceDown message, SID sender) {
                if(message.getUSID().equals(broker.getUSID())) {
                    try {
                        broker = getBroker();
                        broker.tell(new Discover(), context.getSelf());
                    } catch (HydraException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    gui.removeClient(context.getService(message.getUSID()));
                }
            }
        });
        context.registerMessageHandler(NodeDown.class, new MessageHandler<NodeDown>() {
            @Override
            public void handle(final NodeDown message, final SID sender) {
                if(broker.getUSID().getNodeId().equals(message.getUuid())) {
                    try {
                        broker = getBroker();
                        broker.tell(new Discover(), context.getSelf());
                    } catch (HydraException e) {
                        throw new RuntimeException(e);
                    }
                }
                gui.removeAll(message.getUuid());
            }
        });
        context.registerMessageHandler(Renamed.class, new MessageHandler<Renamed>() {
            @Override
            public void handle(Renamed message, SID sender) {
                gui.renameUser(context.getService(message.getUsid()), message.getName());
            }
        });
        context.registerMessageHandler(Message.class, new MessageHandler<Message>() {
            @Override
            public void handle(Message message, SID sender) {
                gui.handleInput(context.getService(message.getUsid()), message.getName());
            }
        });
        context.subscribeEvent(NodeDown.class);
        gui.setVisible(true);
        if(broker == null) {
            broker = getBroker();
        }
        broker.tell(new Discover(), context.getSelf());

    }

    private SID getBroker() throws HydraException {
        SID b;
        try {
            b =  context.getGlobalService("chat-broker");
        } catch (HydraException e) {
            b =  context.startService("chat::broker");
        }
        context.monitor(context.getSelf(), b);
        return b;
    }

    @Override
    public void stop(ServiceContext context) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.setVisible(false);
                gui.dispose();
            }
        });

    }

}
