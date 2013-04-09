package org.gethydrated.hydra.core.io.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.gethydrated.hydra.core.transport.Envelope;

public class JSONEncoder extends MessageToByteEncoder<Envelope> {

    ObjectMapper mapper;

    public JSONEncoder(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void encode(final ChannelHandlerContext ctx, final Envelope msg, final ByteBuf out) throws Exception {
        System.out.println("here");
        byte[] bytes = mapper.writeValueAsBytes(msg);
        int length = bytes.length;
        System.out.println(length);
        out.writeInt(length);
        out.writeBytes(bytes);
        ctx.flush();
    }
}