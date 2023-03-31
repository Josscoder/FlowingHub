package josscoder.flowinghub.commons.packet;

import josscoder.flowinghub.commons.packet.annotation.AsyncPacket;
import josscoder.flowinghub.commons.utils.PacketSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Packet {

    @Getter
    protected final byte pid;

    public abstract void encode(PacketSerializer serializer);

    public abstract void decode(PacketSerializer serializer);

    public boolean isAsync() {
        return getClass().isAnnotationPresent(AsyncPacket.class);
    }
}
