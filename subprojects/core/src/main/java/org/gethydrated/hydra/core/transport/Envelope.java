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
    private SerializedObject sobject;
    private boolean future;
    private Integer futureId;
    private boolean futureResult;
    private boolean hiddenNode;
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

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

    @Override
    public String toString() {
        return "Envelope{" +
                "type=" + type +
                ", sender=" + sender +
                ", target=" + target +
                ", cookie='" + cookie + '\'' +
                ", nodes=" + nodes +
                ", reason='" + reason + '\'' +
                ", connector=" + connector +
                ", sobject=" + sobject +
                '}';
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

    public SerializedObject getSObject() {
        return sobject;
    }

    public void setSObject(SerializedObject sobject) {
        this.sobject = sobject;
    }

    public boolean isFuture() {
        return future;
    }

    public void setFuture(boolean future) {
        this.future = future;
    }

    public void setFutureId(Integer futureId) {
        this.futureId = futureId;
    }

    public Integer getFutureId() {
        return futureId;
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
        if (sobject != null ? !sobject.equals(envelope.sobject) : envelope.sobject != null) return false;
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
        result = 31 * result + (sobject != null ? sobject.hashCode() : 0);
        return result;
    }

    public void setFutureResult(boolean futureResult) {
        this.futureResult = futureResult;
    }

    public boolean isFutureResult() {
        return futureResult;
    }

    public boolean isHiddenNode() {
        return hiddenNode;
    }

    public void setHiddenNode(boolean hiddenNode) {
        this.hiddenNode = hiddenNode;
    }
}
