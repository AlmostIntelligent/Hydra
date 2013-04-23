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
 * Chat broker.
 * 
 * The broker is used to discover other chat service instances.
 * Every chat client sends a discover message to a broker. The broker
 * will broadcast the message to other known chat clients.
 * 
 * All known chat clients are watched by the broker to receive a
 * ServiceDown message on client shutdown.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class BrokerActivator implements ServiceActivator {

    private final List<SID> clients = new LinkedList<>();

    private ServiceContext context;

    @Override
    public void start(final ServiceContext context) throws Exception {
        this.context = context;
        try {
            context.registerGlobal("chat-broker", context.getSelf());
        } catch (Exception e) {
            context.stopService(context.getSelf());
        }
        context.registerMessageHandler(Discover.class,
                new MessageHandler<Discover>() {
                    @Override
                    public void handle(final Discover message, final SID sender) {
                        handleDiscover(message, sender);
                    }
                });
        context.registerMessageHandler(ServiceDown.class,
                new MessageHandler<ServiceDown>() {
                    @Override
                    public void handle(final ServiceDown message,
                            final SID sender) {
                        final Iterator<SID> it = clients.iterator();
                        while (it.hasNext()) {
                            final SID s = it.next();
                            if (s.getUSID().equals(message.getSource())) {
                                it.remove();
                            }
                        }
                        checkEmpty();
                    }
                });
        context.registerMessageHandler(NodeDown.class,
                new MessageHandler<NodeDown>() {
                    @Override
                    public void handle(final NodeDown message, final SID sender) {
                        final Iterator<SID> it = clients.iterator();
                        while (it.hasNext()) {
                            final SID s = it.next();
                            if (s.getUSID().getNodeId()
                                    .equals(message.getUuid())) {
                                it.remove();
                            }
                        }
                        checkEmpty();
                    }
                });
        context.subscribeEvent(NodeDown.class);
    }

    @Override
    public void stop(final ServiceContext context) throws Exception {
    }

    private void handleDiscover(final Discover message, final SID sender) {
        try {
            for (final SID s : clients) {
                s.tell(new NewClient(sender.getUSID()), sender);
            }
            clients.add(sender);
            context.monitor(context.getSelf(), sender);
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkEmpty() {
        if (clients.isEmpty()) {
            try {
                context.stopService(context.getSelf());
            } catch (final HydraException e) {
                e.printStackTrace();
            }
        }
    }
}
