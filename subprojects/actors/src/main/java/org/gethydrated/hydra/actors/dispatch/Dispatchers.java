package org.gethydrated.hydra.actors.dispatch;

/**
 *
 */
public class Dispatchers {

    public static Dispatcher newSharedDispatcher() {
        return new SharedDispatcher();
    }

    public static Dispatcher newExclusiveDispatcher() {
        return new ExclusiveDispatcher();
    }

    public static Dispatcher newBalancingDispatcher() {
        return new BalancingDispatcher();
    }

}
