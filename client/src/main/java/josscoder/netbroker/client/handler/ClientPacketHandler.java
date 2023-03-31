package josscoder.flowinghub.client.handler;

import josscoder.flowinghub.client.NetClient;
import josscoder.flowinghub.commons.packet.Packet;
import josscoder.flowinghub.commons.pipeline.PacketHandler;

public class ClientPacketHandler extends PacketHandler<NetClient> {

    public ClientPacketHandler(NetClient service) {
        super(service);
    }

    @Override
    public void handlePacket(Packet packet) {

    }
}