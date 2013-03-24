package org.gethydrated.hydra.core.transport;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.core.sid.IdMatcher;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
public class TCPConnection implements Connection {

    private UUID nodeid;

    private final Socket socket;

    private final ObjectMapper objectMapper;

    private final IdMatcher idMatcher;

    private boolean connected = false;

    private NodeAddress nodeAddress;

    public TCPConnection(Socket socket, IdMatcher idMatcher) {
        this.socket = socket;
        this.idMatcher = idMatcher;
        objectMapper = new ObjectMapper();
        objectMapper.configure(Feature.AUTO_CLOSE_TARGET, false);
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        objectMapper.registerModule(new EnvelopeModule());
    }

    @Override
    public synchronized Map<UUID, NodeAddress> connect(NodeAddress connectorAddress) throws IOException {
        if(connected) {
            throw new IllegalStateException("Connection handshake already done.");
        }
        Envelope connect = new Envelope(MessageType.CONNECT);
        connect.setCookie("nocookie");
        connect.setSender(idMatcher.getLocal());
        connect.setConnector(connectorAddress);
        objectMapper.writeValue(socket.getOutputStream(), connect);
        Envelope handShake = objectMapper.readValue(socket.getInputStream(), Envelope.class);
        if(handShake.getType() == MessageType.DECLINE) {
            throw new IllegalArgumentException(handShake.getReason());
        }
        nodeid = handShake.getSender();
        return handShake.getNodes();
    }

    @Override
    public synchronized boolean handshake(Map<UUID, NodeAddress> nodes) throws IOException {
        if(connected) {
            throw new IllegalStateException("Connection handshake already done.");
        }
        Envelope connect = objectMapper.readValue(socket.getInputStream(), Envelope.class);
        nodeid = connect.getSender();
        nodeAddress = connect.getConnector();
        Envelope response;
        if (idMatcher.contains(connect.getSender())) {
            response = new Envelope(MessageType.DECLINE);
            response.setReason("Node UUID already in use. Please check your address input or restart hydra.");
            response.setSender(idMatcher.getLocal());
            response.setTarget(connect.getSender());
            objectMapper.writeValue(socket.getOutputStream(), response);
            socket.close();
            return false;
        }
        response = new Envelope(MessageType.ACCEPT);
        response.setSender(idMatcher.getLocal());
        response.setTarget(connect.getSender());
        response.setNodes(nodes);
        objectMapper.writeValue(socket.getOutputStream(),response);
        return true;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void setReceiveCallback(ActorRef target) {

    }

    @Override
    public UUID getUUID() {
        return nodeid;
    }

    @Override
    public InetAddress getIp() {
        return socket.getInetAddress();
    }

    @Override
    public int getPort() {
        return socket.getPort();
    }

    @Override
    public void setConnector(NodeAddress addr) {
        this.nodeAddress = addr;
    }

    @Override
    public NodeAddress getConnector() {
        return nodeAddress;
    }
}
