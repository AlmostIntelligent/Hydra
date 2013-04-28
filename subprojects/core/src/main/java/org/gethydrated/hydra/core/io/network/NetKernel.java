package org.gethydrated.hydra.core.io.network;

import java.io.IOException;

/**
 * Net kernel interface.
 * @author Christian Kulpa
 * @since 0.2.0
 */
public interface NetKernel extends NodeController {

    /**
     * Binds Hydra to the given port.
     * @param port port number.
     * @throws IOException on failure.
     */
    void bind(int port) throws IOException;

    /**
     * Closes all io activities.
     */
    void close();

    /**
     * Connects to a new Hydra node.
     * @param ip ip address.
     * @param port port number
     * @throws IOException on failure.
     */
    void connect(String ip, int port) throws IOException;
}
