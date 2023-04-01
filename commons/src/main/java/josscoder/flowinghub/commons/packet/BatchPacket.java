package josscoder.flowinghub.commons.packet;

import io.netty.buffer.ByteBuf;
import josscoder.flowinghub.commons.packet.codec.ProtocolCodec;
import josscoder.flowinghub.commons.utils.PacketSerializer;

import java.util.List;

public class BatchPacket extends Packet {

    public List<BasicPacket> packets;

    public BatchPacket() {
        super(ProtocolInfo.BATCH_PACKET);
    }

    @Override
    public void encode(PacketSerializer serializer) {
        serializer.writeInt(packets.size());
        packets.forEach(packet -> ProtocolCodec.encodePacketIntoSerializer(serializer, packet));
    }

    @Override
    public void decode(PacketSerializer serializer) {
        int size = serializer.readInt();
        for (int i = 0; i < size; i++) {
            ByteBuf buf = serializer.buffer();

            if (buf.readableBytes() < 1) {
                continue;
            }

            BasicPacket packet = (BasicPacket) ProtocolCodec.decodePacketFromBuf(buf);
            if (packet != null) {
                packets.add(packet);
            }
        }
    }
}
