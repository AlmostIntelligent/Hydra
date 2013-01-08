package org.gethydrated.hydra.api.service;

import org.gethydrated.hydra.api.node.Node;

import java.util.Objects;
import java.util.concurrent.Future;

/**
 * Unique service identifier.
 * @author Christian Kulpa
 * @since 0.2.0
 */
public interface USID {

    void tell(Object message, USID sender);

    Future<?> ask(Object message);

}
