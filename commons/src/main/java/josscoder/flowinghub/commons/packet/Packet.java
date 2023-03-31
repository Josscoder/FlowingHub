package josscoder.flowinghub.commons.packet;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Packet {

    @Getter
    protected final byte pid;

    public abstract void encode(ByteBuf buf);

    public abstract void decode(ByteBuf buf);
}
