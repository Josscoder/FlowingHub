package josscoder.flowinghub.commons.packet.base;

import josscoder.flowinghub.commons.packet.BasicPacket;
import josscoder.flowinghub.commons.packet.ProtocolInfo;
import josscoder.flowinghub.commons.utils.PacketSerializer;

public class MessagePacket extends BasicPacket {

    public String message;

    public MessagePacket() {
        super(ProtocolInfo.MESSAGE_PACKET);
    }

    @Override
    public void encode(PacketSerializer serializer) {
        serializer.writeString(message);
    }

    @Override
    public void decode(PacketSerializer serializer) {
        message = serializer.readString();
    }
}
