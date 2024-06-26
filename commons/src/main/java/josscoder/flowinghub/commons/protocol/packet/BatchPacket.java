package josscoder.flowinghub.commons.protocol.packet;

import io.netty.buffer.ByteBuf;
import josscoder.flowinghub.commons.protocol.codec.ProtocolCodec;
import josscoder.flowinghub.commons.protocol.ProtocolInfo;
import josscoder.flowinghub.commons.utils.PacketSerializer;

import java.util.ArrayList;
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
        if (packets == null) {
            packets = new ArrayList<>();
        }
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
