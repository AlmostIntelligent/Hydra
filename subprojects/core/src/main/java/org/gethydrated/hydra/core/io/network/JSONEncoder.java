package org.gethydrated.hydra.core.io.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.gethydrated.hydra.core.io.transport.Envelope;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gethydrated.hydra.core.io.transport.MessageType;

/**
 * JSON encoder for message processing.
 * @author Chris
 *
 */
public class JSONEncoder extends MessageToByteEncoder<Envelope> {

    private ObjectMapper mapper;

    /**
     * Constructor.
     * @param mapper object mapper.
     */
    public JSONEncoder(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void encode(final ChannelHandlerContext ctx, final Envelope msg,
            final ByteBuf out) throws Exception {
        if (msg.getType() == MessageType.SYSTEM) {
            //System.out.println("out " + msg);
        }
        final byte[] bytes = mapper.writeValueAsBytes(msg);
        final int length = bytes.length;
        out.writeInt(length);
        out.writeBytes(bytes);
        ctx.flush();
    }
}