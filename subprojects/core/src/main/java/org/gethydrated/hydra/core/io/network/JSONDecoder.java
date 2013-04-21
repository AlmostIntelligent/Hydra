package org.gethydrated.hydra.core.io.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.gethydrated.hydra.core.io.transport.Envelope;

public class JSONDecoder extends ByteToMessageDecoder {

    ObjectMapper mapper;

    public JSONDecoder(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object decode(final ChannelHandlerContext ctx, final ByteBuf in) throws Exception {
        if(in.readableBytes() < 4) {
            return null;
        }
        in.markReaderIndex();
        int length = in.readInt();
        if (in.readableBytes() < length){
            in.resetReaderIndex();
            return null;
        }
        byte[] buffer = new byte[length];
        in.readBytes(buffer);
        return mapper.readValue(buffer, Envelope.class);
    }
}