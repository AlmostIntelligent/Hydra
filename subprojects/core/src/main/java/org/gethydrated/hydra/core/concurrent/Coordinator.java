package org.gethydrated.hydra.core.concurrent;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.event.NodeDown;
import org.gethydrated.hydra.api.event.NodeUp;
import org.gethydrated.hydra.core.concurrent.messages.Election;
import org.gethydrated.hydra.core.concurrent.messages.Leader;
import org.gethydrated.hydra.core.io.network.NodeController;

import java.util.*;

/**
 *
 */
public class Coordinator extends Actor {

    private final NodeController controller;
    private final UUID local;
    private UUID leader;
    private final SortedSet<UUID> nodes = new TreeSet<>();
    private LockRequest localRequest;
    private final Map<UUID, LockRequest> globalQueue = new HashMap<>();
    private LockRequest current;
    private int electionId = 0;
    private boolean electing = false;

    public Coordinator(NodeController controller) {
        this.controller = controller;
        this.local = controller.getLocal();
        this.nodes.add(local);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof NodeUp) {
            nodes.add(((NodeUp) message).getUuid());
            startElection();
        }
        if (message instanceof NodeDown) {
            nodes.remove(((NodeDown) message).getUuid());
            if (((NodeDown) message).getUuid().equals(leader)) {
                startElection();
            }
        }
        if (message instanceof Election) {
            if (((Election) message).getUuid().compareTo(local) > 0) {
                sendNext((Election) message);
                electing = true;
            }
            if (((Election) message).getUuid().equals(local) && ((Election) message).getId() == electionId) {
                System.out.println("im leader!");
                for (UUID uuid : nodes) {
                    getContext().getActor("/app/nodes/" + controller.getID(uuid)).tell(new Leader(local), getSelf());
                }
                electing = false;
                newLeader();
            }
            if (((Election) message).getUuid().compareTo(local)< 0 && !electing) {
                startElection();
            }
        }
        if (message instanceof Leader) {
            electing = false;
            if (((Leader) message).getNode().compareTo(local) < 0) {
                startElection();
            }
            leader = ((Leader) message).getNode();
            newLeader();
        }
        if (message instanceof LockRequest) {
            if (((LockRequest) message).getNodeId().equals(local) && localRequest == null) {
                localRequest = (LockRequest) message;
            } else {
                System.out.println("request not null");
            }
            if (!local.equals(leader)) {
                getContext().getActor("/app/nodes/" + controller.getID(leader)).tell(localRequest, getSelf());
            } else {
                handleRequest((LockRequest) message);
            }
        }
        if (message instanceof LockRelease) {
            if (((LockRelease) message).getNodeId().equals(local) && localRequest != null) {
                localRequest = null;
            }

            if (!local.equals(leader)) {
                getContext().getActor("/app/nodes/" + controller.getID(leader)).tell(message, getSelf());
            } else {
                handleRelease((LockRelease) message);
            }
        }
    }

    private void startElection() {
        if (nodes.size() == 1) {
            leader = local;
            newLeader();
        } else {
            electionId++;
            electing = true;
            Election e = new Election();
            e.setId(electionId);
            e.setUuid(local);
            sendNext(e);
        }
    }

    private void sendNext(Election election) {
        SortedSet<UUID> followers = new TreeSet<>(nodes.tailSet(local));
        followers.remove(local);
        UUID follower = (followers.isEmpty()) ? nodes.first() : followers.first();
        getContext().getActor("/app/nodes/" + controller.getID(follower)).tell(election, getSelf());
    }

    private void newLeader() {
        globalQueue.clear();
        current = null;
        if (localRequest != null) {
            if (!local.equals(leader)) {
                getContext().getActor("/app/nodes/" + controller.getID(leader)).tell(localRequest, getSelf());
            } else {
                handleRequest(localRequest);
            }
        }
    }

    private void handleRequest(LockRequest request) {
        globalQueue.put(request.getNodeId(), request);
        lockNext();
    }

    private void handleRelease(LockRelease release) {
        if (isLeader()) {
            globalQueue.remove(release.getNodeId());
        }
        if (current != null && current.getNodeId().equals(release.getNodeId())) {
            current = null;
            lockNext();
        }
    }

    private void lockNext() {
        if (current == null && globalQueue.size() > 0) {
            UUID index = globalQueue.keySet().iterator().next();
            LockRequest r = globalQueue.remove(index);
            current = r;
            System.out.println("lock: " + r.getNodeId());
            if (r.getNodeId().equals(local)) {
                getContext().getActor("/app/locking").tell(new LockGranted(), getSelf());
            } else {
                getContext().getActor("/app/nodes/" + controller.getID(r.getNodeId())).tell(new LockGranted(), getSelf());
            }
        }
    }

    private boolean isLeader() {
        return local.equals(leader);
    }

    @Override
    public void onStart() {
        nodes.addAll(controller.getNodes());
        startElection();
        getSystem().getEventStream().subscribe(getSelf(), NodeDown.class);
        getSystem().getEventStream().subscribe(getSelf(), NodeUp.class);
    }

    @Override
    public void onStop() {
        getSystem().getEventStream().unsubscribe(getSelf());
    }
}
