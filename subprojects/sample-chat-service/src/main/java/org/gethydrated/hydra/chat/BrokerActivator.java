package org.gethydrated.hydra.chat;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.event.NodeDown;
import org.gethydrated.hydra.api.event.ServiceDown;
import org.gethydrated.hydra.api.service.MessageHandler;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.ServiceActivator;
import org.gethydrated.hydra.api.service.ServiceContext;
import org.gethydrated.hydra.chat.messages.Discover;
import org.gethydrated.hydra.chat.messages.NewClient;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class BrokerActivator implements ServiceActivator {

    private List<SID> clients = new LinkedList<>();

    private ServiceContext context;

    @Override
    public void start(ServiceContext context) throws Exception {
        this.context = context;
        context.registerGlobal("chat-broker", context.getSelf());
        context.registerMessageHandler(Discover.class, new MessageHandler<Discover>() {
            @Override
            public void handle(Discover message, SID sender) {
                handleDiscover(message, sender);
            }
        });
        context.registerMessageHandler(ServiceDown.class, new MessageHandler<ServiceDown>() {
            @Override
            public void handle(final ServiceDown message, final SID sender) {
                Iterator<SID> it = clients.iterator();
                while(it.hasNext()) {
                    SID s = it.next();
                    if(s.getUSID().equals(message.getUSID())) {
                        it.remove();
                    }
                }
                checkEmpty();
            }
        });
        context.registerMessageHandler(NodeDown.class, new MessageHandler<NodeDown>() {
            @Override
            public void handle(final NodeDown message, final SID sender) {
                Iterator<SID> it = clients.iterator();
                while (it.hasNext()) {
                    SID s = it.next();
                    if(s.getUSID().getNodeId().equals(message.getUuid())) {
                        it.remove();
                    }
                }
                checkEmpty();
            }
        });
        context.subscribeEvent(NodeDown.class);
    }

    @Override
    public void stop(ServiceContext context) throws Exception {
        System.out.println("Broker stopped.");
    }

    public void handleDiscover(Discover message, SID sender) {
        try {
            for(SID s : clients) {
                s.tell(new NewClient(sender.getUSID()), sender);
            }
            clients.add(sender);
            context.monitor(context.getSelf(), sender);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkEmpty() {
        if (clients.isEmpty()) {
            try {
                context.stopService(context.getSelf());
            } catch (HydraException e) {
                e.printStackTrace();
            }
        }
    }
}
