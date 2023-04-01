package josscoder.flowinghub.commons.protocol.codec;

import io.netty.buffer.ByteBuf;
import josscoder.flowinghub.commons.protocol.packet.BatchPacket;
import josscoder.flowinghub.commons.protocol.packet.Packet;
import josscoder.flowinghub.commons.protocol.packet.base.AuthRequestPacket;
import josscoder.flowinghub.commons.protocol.packet.base.AuthResponsePacket;
import josscoder.flowinghub.commons.protocol.packet.base.MessagePacket;
import josscoder.flowinghub.commons.utils.PacketSerializer;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProtocolCodec {

    private static final Map<Byte, Class<? extends Packet>> PACKETS = new HashMap<>();

    static {
        registerPacket(
                new AuthRequestPacket(),
                new AuthResponsePacket(),
                new MessagePacket(),
                new BatchPacket()
        );
    }

    public static void registerPacket(Packet ...packets) {
        Arrays.stream(packets).forEach(packet -> PACKETS.put(packet.getPid(), packet.getClass()));
    }

    public static void unregisterPacket(byte pid) {
        PACKETS.remove(pid);
    }

    public static Packet createPacketInstance(byte pid) {
        Class<? extends Packet> clazz = PACKETS.get(pid);
        if (clazz != null) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    public static void encodePacketIntoSerializer(PacketSerializer serializer, Packet packet) {
        serializer.writeByte(packet.getPid());
        packet.encode(serializer);
    }

    public static void encodePacketIntoBuf(ByteBuf buf, Packet packet) {
        PacketSerializer serializer = new PacketSerializer(buf);
        serializer.writeByte(packet.getPid());
        packet.encode(serializer);
    }

    public static Packet decodePacketFromBuf(ByteBuf buf) {
        if (buf.readableBytes() < 1) {
            return null;
        }

        byte pid = buf.readByte();

        Packet packet = ProtocolCodec.createPacketInstance(pid);
        if (packet == null) {
            return null;
        }

        packet.decode(new PacketSerializer(buf));
        return packet;
    }
}
