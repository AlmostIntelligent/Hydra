package org.gethydrated.hydra.actors.mailbox;


import org.gethydrated.hydra.actors.ActorRef;

public interface Mailbox extends Runnable {

    void enqueue(ActorRef self, Message m);

    void enqueueSystem(ActorRef self, Message m);

    boolean hasMessages();

    boolean hasSystemMessages();

    void setIdle();

    boolean setScheduled();

    boolean isSchedulable(boolean hasMessages, boolean hasSystemMessages);

    void setClosed();
}
