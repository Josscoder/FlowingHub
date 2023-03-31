package josscoder.flowinghub.commons.packet;

import josscoder.flowinghub.commons.packet.registry.PacketRegistry;
import josscoder.flowinghub.commons.utils.PacketSerializer;

import java.util.List;

public abstract class BatchPacket extends Packet {

    public List<Packet> packets;

    public BatchPacket(byte pid) {
        super(pid);
    }

    @Override
    public void encode(PacketSerializer serializer) {
        serializer.writeInt(packets.size());
        packets.forEach(packet -> {
            serializer.writeByte(packet.getPid());
            packet.encode(serializer);
        });
    }

    @Override
    public void decode(PacketSerializer serializer) {
        int size = serializer.readInt();
        for (int i = 0; i < size; i++) {
            byte pid = serializer.readByte();

            Packet packet = PacketRegistry.createPacketInstance(pid);
            if (packet != null) {
                packet.decode(serializer);
                packets.add(packet);
            }
        }
    }
}
