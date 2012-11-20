package org.gethydrated.hydra.actors.node;

import java.util.concurrent.ExecutorService;

import org.gethydrated.hydra.actors.ActorSource;

public interface ActorContext extends ActorSource {

	String getName();

	ExecutorService getExecutor();
}
