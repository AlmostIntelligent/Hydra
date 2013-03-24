package org.gethydrated.hydra.core.transport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.Objects;

/**
 * A jackson serializable immutable representation
 * of a network address with ip and port.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class NodeAddress {

    private String ip;

    private int port;

    public NodeAddress(String ip, int port) {
        this.ip = Objects.requireNonNull(ip);
        this.port = port;
    }

    private NodeAddress() {
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeAddress that = (NodeAddress) o;

        if (port != that.port) return false;
        if (!ip.equals(that.ip)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ip.hashCode();
        result = 31 * result + port;
        return result;
    }
}
