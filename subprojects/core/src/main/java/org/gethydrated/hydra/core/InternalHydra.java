package org.gethydrated.hydra.core;

import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.api.Hydra;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.service.SIDFactory;

/**
 * Internal hydra representation.
 */
public interface InternalHydra extends Hydra {

    ActorSystem getActorSystem();

    Configuration getConfiguration();

    SIDFactory getDefaultSIDFactory();
}
