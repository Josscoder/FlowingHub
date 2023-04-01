package josscoder.flowinghub.commons.protocol.packet.base;

import josscoder.flowinghub.commons.protocol.packet.BasicPacket;
import josscoder.flowinghub.commons.protocol.ProtocolInfo;
import josscoder.flowinghub.commons.utils.PacketSerializer;

public class AuthResponsePacket extends BasicPacket {

    public enum Status {
        SUCCESS,
        INVALID_TOKEN
    }

    public Status status;
    public String serverId;

    public AuthResponsePacket() {
        super(ProtocolInfo.AUTH_RESPONSE_PACKET);
    }

    @Override
    public void encode(PacketSerializer serializer) {
        serializer.writeString(status.name());

        if (status.equals(Status.SUCCESS)) {
            serializer.writeString(serverId);
        }
    }

    @Override
    public void decode(PacketSerializer serializer) {
        status = Status.valueOf(serializer.readString());

        if (status.equals(Status.SUCCESS)) {
            serverId = serializer.readString();
        }
    }
}
