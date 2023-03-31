package josscoder.flowinghub.commons.packet.registry;

import josscoder.flowinghub.commons.packet.Packet;
import josscoder.flowinghub.commons.packet.base.AuthRequestPacket;
import josscoder.flowinghub.commons.packet.base.AuthResponsePacket;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {

    private static final Map<Byte, Class<? extends Packet>> PACKETS = new HashMap<>();

    static {
        registerPacket(
                new AuthRequestPacket(),
                new AuthResponsePacket()
        );
    }

    public static void registerPacket(Packet ...packets) {
        Arrays.stream(packets).forEach(packet -> PACKETS.put(packet.getPid(), packet.getClass()));
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
}
