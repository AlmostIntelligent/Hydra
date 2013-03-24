package org.gethydrated.hydra.core.transport;

import java.util.Map;
import java.util.UUID;

/**
 * Flexible POJO that can be used to send
 * messages between hydra nodes.
 */
public class Envelope {

    private MessageType type;
    private UUID sender;
    private UUID target;
    private String cookie;
    private Map<UUID,NodeAddress> nodes;
    private String reason;
    private NodeAddress connector;

    public Envelope(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookie() {
        return cookie;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getTarget() {
        return target;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }

    public void setNodes(Map<UUID,NodeAddress> uuids) {
        nodes = uuids;
    }

    public Map<UUID,NodeAddress> getNodes() {
        return nodes;
    }

    public String toString() {
        return "Envelope: type="+type+" sender="+sender+" target="+target+" cookie:"+cookie;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setConnector(NodeAddress connector) {
        this.connector = connector;
    }

    public NodeAddress getConnector() {
        return connector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Envelope envelope = (Envelope) o;

        if (connector != null ? !connector.equals(envelope.connector) : envelope.connector != null) return false;
        if (cookie != null ? !cookie.equals(envelope.cookie) : envelope.cookie != null) return false;
        if (nodes != null ? !nodes.equals(envelope.nodes) : envelope.nodes != null) return false;
        if (reason != null ? !reason.equals(envelope.reason) : envelope.reason != null) return false;
        if (sender != null ? !sender.equals(envelope.sender) : envelope.sender != null) return false;
        if (target != null ? !target.equals(envelope.target) : envelope.target != null) return false;
        if (type != envelope.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (cookie != null ? cookie.hashCode() : 0);
        result = 31 * result + (nodes != null ? nodes.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (connector != null ? connector.hashCode() : 0);
        return result;
    }
}
