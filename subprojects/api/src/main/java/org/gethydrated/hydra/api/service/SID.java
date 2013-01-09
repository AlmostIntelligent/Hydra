package org.gethydrated.hydra.api.service;

import java.util.concurrent.Future;

/**
 * Unique service identifier.
 * @author Christian Kulpa
 * @since 0.2.0
 */
public interface SID {

    void tell(Object message, SID sender);

    Future<?> ask(Object message);

}
