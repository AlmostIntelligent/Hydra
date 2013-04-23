package org.gethydrated.hydra.actors.mailbox;

import org.gethydrated.hydra.actors.ActorRef;

/**
 * Mailbox interface.
 * @author Christian Kulpa
 * @since 0.2.0
 */
public interface Mailbox extends Runnable {
    
    /**
     * Enqueues a message to the mailbox.
     * @param sender sender reference.
     * @param m message.
     */
    void enqueue(ActorRef sender, Message m);

    /**
     * Enqueues a system message to the mailbox.
     * @param sender sender reference.
     * @param m message.
     */
    void enqueueSystem(ActorRef sender, Message m);

    /**
     * Returns if the mailbox has pending messages.
     * @return true if there are messages.
     */
    boolean hasMessages();

    /**
     * Returns if the mailbox has pending system messages.
     * @return true if there are system messages.
     */
    boolean hasSystemMessages();

    /**
     * Sets the mailbox state to closed.
     */
    void setClosed();

    /**
     * Sets the mailbox state to idle.
     */
    void setIdle();

    /**
     * Sets the mailbox state to scheduled.
     * @return true, if scheduled successfully.
     */
    boolean setScheduled();

    /**
     * Sets the mailbox state to suspended.
     */
    void suspend();

    /**
     * Resumes a suspended mailbox.
     */
    void resume();

    /**
     * Returns true if the mailbox has pending messages.
     * @param hasMessages message flag.
     * @param hasSystemMessages system message flag.
     * @return true if the mailbox can be scheduled.
     */
    boolean isSchedulable(boolean hasMessages, boolean hasSystemMessages);

}
