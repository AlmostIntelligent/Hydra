package org.gethydrated.hydra.chat;

import org.gethydrated.hydra.api.service.MessageHandler;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.ServiceActivator;
import org.gethydrated.hydra.api.service.ServiceContext;
import org.gethydrated.hydra.chat.messages.Discover;
import org.gethydrated.hydra.chat.messages.NewClient;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class BrokerActivator implements ServiceActivator {

    private List<SID> clients = new LinkedList<>();

    @Override
    public void start(ServiceContext context) throws Exception {
        context.registerGlobal("chat-broker", context.getSelf());
        context.registerMessageHandler(Discover.class, new MessageHandler<Discover>() {
            @Override
            public void handle(Discover message, SID sender) {
                handleDiscover(message, sender);
            }
        });
    }

    @Override
    public void stop(ServiceContext context) throws Exception {
        System.out.println("Broker down " + context.getSelf().getUSID());
    }

    public void handleDiscover(Discover message, SID sender) {
        try {
            for(SID s : clients) {
                s.tell(new NewClient(sender.getUSID()), sender);
            }
            clients.add(sender);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
