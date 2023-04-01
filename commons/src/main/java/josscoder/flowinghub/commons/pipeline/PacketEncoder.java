package josscoder.flowinghub.commons.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import josscoder.flowinghub.commons.packet.Packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        byte[] packetBytes = serialize(msg);
        out.writeShort(packetBytes.length);
        out.writeBytes(packetBytes);
    }

    private byte[] serialize(Packet packet) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(packet);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }
}
