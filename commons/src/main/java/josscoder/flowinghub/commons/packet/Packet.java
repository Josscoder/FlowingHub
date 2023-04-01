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

    public boolean isAsyncPacket() {
        return getClass().isAnnotationPresent(AsyncPacket.class);
    }

    public boolean isBasicPacket() {
        return this instanceof BasicPacket;
    }

    public boolean isBatchPacket() {
        return this instanceof BatchPacket;
    }

    public boolean isWeightyPacket() {
        return this instanceof WeightyPacket;
    }
}
