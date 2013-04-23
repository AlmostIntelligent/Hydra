package org.gethydrated.hydra.actors.dispatch;

import java.util.concurrent.ExecutorService;

import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.node.ActorNode;

/**
 * Dispatcher interface.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public interface Dispatcher {

    /**
     * Creates a new Mailbox for the given actor node.
     * @param node actor node.
     * @return new mailbox.
     */
    Mailbox createMailbox(ActorNode node);

    /**
     * Attaches an actor node to the dispatcher and 
     * starts message processing.
     * @param node actor node.
     */
    void attach(ActorNode node);

    /**
     * Detaches an actor from the dispatcher and
     * stops message processing.
     * @param node actor node.
     */
    void detach(ActorNode node);

    /**
     * Dispatches a message to the actor node.
     * @param node actor node.
     * @param message message.
     */
    void dispatch(ActorNode node, Message message);
    
    /**
     * Dispatches a system message to the actor node.
     * @param node actor node.
     * @param message system message.
     */
    void dispatchSystem(ActorNode node, Message message);

    /**
     * Executes a mailbox. 
     * @param mailbox processed mailbox.
     * @param hasMessages message flag.
     * @param hasSystemMessages system message flag.
     * @return true if the mailbox was scheduled successfully.
     */
    boolean executeMailbox(Mailbox mailbox, boolean hasMessages,
            boolean hasSystemMessages);

    /**
     * Returns the underlying executor.
     * @return executor.
     */
    ExecutorService getExecutor();

    /**
     * Shuts the dispatcher down. This method will return 
     * immediatly.
     */
    void shutdown();

    /**
     * Joins possible dispatcher threads. Blocks until all
     * threads are joined.
     */
    void join();
}
