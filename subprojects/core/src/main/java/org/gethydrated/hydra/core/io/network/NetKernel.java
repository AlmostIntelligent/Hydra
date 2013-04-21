package org.gethydrated.hydra.core.io.network;

import java.io.IOException;

public interface NetKernel extends NodeController {

    void bind(int port) throws IOException;

    void close();

    void connect(String ip, int port) throws IOException;
}
