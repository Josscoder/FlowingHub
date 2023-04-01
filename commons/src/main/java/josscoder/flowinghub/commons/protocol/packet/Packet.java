package josscoder.flowinghub.commons.protocol.packet;

import josscoder.flowinghub.commons.protocol.packet.annotation.AsyncPacket;
import josscoder.flowinghub.commons.utils.PacketSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Packet {

    @Getter
    protected final byte pid;

    public abstract void encode(PacketSerializer serializer);

    public abstract void decode(PacketSerializer serializer);

    public boolean isAsyncPacket() {
        return getClass().isAnnotationPresent(AsyncPacket.class);
    }
}
