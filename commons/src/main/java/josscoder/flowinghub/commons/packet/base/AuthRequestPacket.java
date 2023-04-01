package josscoder.flowinghub.commons.packet.base;

import josscoder.flowinghub.commons.packet.BasicPacket;
import josscoder.flowinghub.commons.packet.ProtocolInfo;
import josscoder.flowinghub.commons.utils.PacketSerializer;

public class AuthRequestPacket extends BasicPacket {

    public String authToken;

    public AuthRequestPacket() {
        super(ProtocolInfo.AUTH_REQUEST_PACKET);
    }

    @Override
    public void encode(PacketSerializer serializer) {
        serializer.writeString(authToken);
    }

    @Override
    public void decode(PacketSerializer serializer) {
        authToken = serializer.readString();
    }
}
