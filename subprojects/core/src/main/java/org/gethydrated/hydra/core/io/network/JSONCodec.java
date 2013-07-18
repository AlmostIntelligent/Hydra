package org.gethydrated.hydra.core.io.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.gethydrated.hydra.core.io.transport.Envelope;

import java.util.List;

/**
 * JSON encoder for message processing.
 * @author Chris
 *
 */
public class JSONCodec extends ByteToMessageCodec<Envelope> {

    private final ObjectMapper mapper;

    private static final byte[] LENGTH_FIELD = new byte[4];

    /**
     * Constructor.
     * @param mapper object mapper.
     */
    public JSONCodec(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Envelope msg, ByteBuf out) throws Exception {
        int startIdx = out.writerIndex();
        ByteBufOutputStream bout = new ByteBufOutputStream(out);
        bout.write(LENGTH_FIELD);
        mapper.writeValue(bout, msg);
        bout.flush();
        bout.close();
        int endIdx = out.writerIndex();
        out.setInt(startIdx, endIdx - startIdx - 4);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        final int length = in.readInt();
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        ByteBufInputStream bin = new ByteBufInputStream(in);
        Envelope env = mapper.readValue(bin, Envelope.class);
        bin.close();
        out.add(env);
    }
}