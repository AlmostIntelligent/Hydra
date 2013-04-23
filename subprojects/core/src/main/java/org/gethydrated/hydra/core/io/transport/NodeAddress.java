package org.gethydrated.hydra.core.io.transport;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * A jackson serializable immutable representation of a network address with ip
 * and port.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class NodeAddress {

    private String ip;

    private int port;

    /**
     * Constructor.
     * @param ip node ip address.
     * @param port node port.
     */
    public NodeAddress(final String ip, final int port) {
        this.ip = Objects.requireNonNull(ip);
        this.port = port;
    }

    @SuppressWarnings("unused")
    private NodeAddress() {
    }

    /**
     * Returns the ip address.
     * @return ip address.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Returns the port.
     * @return port.
     */
    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final NodeAddress that = (NodeAddress) o;

        if (port != that.port) {
            return false;
        }
        if (!ip.equals(that.ip)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = ip.hashCode();
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return "NodeAddress{" + "ip='" + ip + '\'' + ", port=" + port + '}';
    }
}
