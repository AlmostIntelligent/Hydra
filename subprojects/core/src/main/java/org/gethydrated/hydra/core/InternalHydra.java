package org.gethydrated.hydra.core;

import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.api.Hydra;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.service.SIDFactory;
import org.gethydrated.hydra.core.io.network.NetKernel;

/**
 * Internal hydra representation.
 */
public interface InternalHydra extends Hydra {

    /**
     * Returns the actor system used by Hydra.
     * @return actor system.
     */
    ActorSystem getActorSystem();

    /**
     * Returns Hydras root configuration entry.
     * @return root configuration entry.
     */
    Configuration getConfiguration();

    /**
     * Returns a service id factory.
     * @return service id factory.
     */
    SIDFactory getDefaultSIDFactory();

    /**
     * Returns the network kernel.
     * @return network kernel.
     */
    NetKernel getNetKernel();
}
