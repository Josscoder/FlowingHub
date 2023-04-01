package josscoder.flowinghub.commons.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import josscoder.flowinghub.commons.packet.Packet;
import josscoder.flowinghub.commons.packet.codec.ProtocolCodec;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 1) {
            return;
        }

        Packet packet = ProtocolCodec.decodePacketFromBuf(in);
        if (packet != null) {
            out.add(packet);
        }
    }
}
