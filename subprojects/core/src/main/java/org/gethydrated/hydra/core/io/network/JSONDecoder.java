package org.gethydrated.hydra.core.io.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.gethydrated.hydra.core.io.transport.Envelope;

/**
 * JSON decoder for message processing.
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class JSONDecoder extends ByteToMessageDecoder {

    private ObjectMapper mapper;

    /**
     * Constructor.
     * @param mapper object mapper.
     */
    public JSONDecoder(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object decode(final ChannelHandlerContext ctx, final ByteBuf in)
            throws Exception {
        if (in.readableBytes() < 4) {
            return null;
        }
        in.markReaderIndex();
        final int length = in.readInt();
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return null;
        }
        final byte[] buffer = new byte[length];
        in.readBytes(buffer);
        Envelope env = mapper.readValue(buffer, Envelope.class);
        return env;
    }
}