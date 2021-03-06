package org.gethydrated.hydra.actors;

/**
 * Standard ActorFactory implementation for a given class.
 * 
 * The factory will always return a new instance of the class.
 * 
 * @author Christian Kulpa
 * 
 */
public class DefaultActorFactory implements ActorFactory {

    /**
     * class.
     */
    private final Class<? extends Actor> actorClass;

    /**
     * Constructor.
     * 
     * @param actorClass
     *            The actor class.
     */
    public DefaultActorFactory(final Class<? extends Actor> actorClass) {
        this.actorClass = actorClass;
    }

    @Override
    public Actor create() throws Exception {
        return actorClass.getConstructor().newInstance();
    }

}
