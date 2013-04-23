package org.gethydrated.hydra.core.io.transport;

import java.util.Map;
import java.util.UUID;

/**
 * Flexible POJO that can be used to send messages between hydra nodes.
 */
public class Envelope {

    private final MessageType type;
    private UUID sender;
    private UUID target;
    private String cookie;
    private Map<UUID, NodeAddress> nodes;
    private String reason;
    private NodeAddress connector;
    private SerializedObject sobject;
    private boolean hiddenNode;
    private long timestamp;
    
    /**
     * Constructor.
     * @param type Message type.
     */
    public Envelope(final MessageType type) {
        this.type = type;
    }

    /**
     * Returns the message type.
     * @return message type.
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Returns the timestamp of the envelope.
     * @return timestamp timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets a timestamp for the envelope.
     * @param timestamp timestamp.
     */
    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }   
    
    /**
     * Sets a security cookie.
     * @param cookie security cookie.
     */
    public void setCookie(final String cookie) {
        this.cookie = cookie;
    }

    /**
     * Returns the security cookie.
     * @return security cookie.
     */
    public String getCookie() {
        return cookie;
    }

    /**
     * Sets the envelopes sender.
     * @param sender sender usid.
     */
    public void setSender(final UUID sender) {
        this.sender = sender;
    }

    /**
     * Returns the envelopes sender.
     * @return sender usid.
     */
    public UUID getSender() {
        return sender;
    }

    /**
     * Returns the envelopes target.
     * @return target uuid.
     */
    public UUID getTarget() {
        return target;
    }

    /**
     * Sets the envelopes target.
     * @param target target uuid.
     */
    public void setTarget(final UUID target) {
        this.target = target;
    }

    /**
     * Sets the list of known nodes.
     * @param uuids node list.
     */
    public void setNodes(final Map<UUID, NodeAddress> uuids) {
        nodes = uuids;
    }

    /**
     * Returns a list of known nodes.
     * @return node list.
     */
    public Map<UUID, NodeAddress> getNodes() {
        return nodes;
    }

    @Override
    public String toString() {
        return "Envelope{" + "type=" + type + ", sender=" + sender
                + ", target=" + target + ", cookie='" + cookie + '\''
                + ", nodes=" + nodes + ", reason='" + reason + '\''
                + ", connector=" + connector + ", sobject=" + sobject + '}';
    }

    /**
     * Returns the rejection reason.
     * @return reason.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the rejection reason.
     * @param reason reason.
     */
    public void setReason(final String reason) {
        this.reason = reason;
    }

    /**
     * Sets the node connector.
     * @param connector node connector.
     */
    public void setConnector(final NodeAddress connector) {
        this.connector = connector;
    }

    /**
     * Returns the node connector.
     * @return node connector.
     */
    public NodeAddress getConnector() {
        return connector;
    }

    /**
     * Returns a serialized object.
     * @return serialized object.
     */
    public SerializedObject getSObject() {
        return sobject;
    }

    /**
     * Sets a serialized object.
     * @param sobject serialized object.
     */
    public void setSObject(final SerializedObject sobject) {
        this.sobject = sobject;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Envelope envelope = (Envelope) o;

        if (connector != null ? !connector.equals(envelope.connector)
                : envelope.connector != null) {
            return false;
        }
        if (cookie != null ? !cookie.equals(envelope.cookie)
                : envelope.cookie != null) {
            return false;
        }
        if (nodes != null ? !nodes.equals(envelope.nodes)
                : envelope.nodes != null) {
            return false;
        }
        if (reason != null ? !reason.equals(envelope.reason)
                : envelope.reason != null) {
            return false;
        }
        if (sender != null ? !sender.equals(envelope.sender)
                : envelope.sender != null) {
            return false;
        }
        if (sobject != null ? !sobject.equals(envelope.sobject)
                : envelope.sobject != null) {
            return false;
        }
        if (target != null ? !target.equals(envelope.target)
                : envelope.target != null) {
            return false;
        }
        if (type != envelope.type) {
            return false;
        }

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
        result = 31 * result + (sobject != null ? sobject.hashCode() : 0);
        return result;
    }

    /**
     * Returns if the Envelope is from a hidden node.
     * @return true if hidden.
     */
    public boolean isHiddenNode() {
        return hiddenNode;
    }

    /**
     * Sets the hidden node flag.
     * @param hiddenNode hidden node flag.
     */
    public void setHiddenNode(final boolean hiddenNode) {
        this.hiddenNode = hiddenNode;
    }
}
