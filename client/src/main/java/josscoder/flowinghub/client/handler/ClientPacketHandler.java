package josscoder.flowinghub.client.handler;

import josscoder.flowinghub.commons.packet.Packet;
import josscoder.flowinghub.commons.pipeline.PacketHandler;

public class ClientPacketHandler extends PacketHandler<josscoder.flowinghub.client.FlowingClient> {

    public ClientPacketHandler(josscoder.flowinghub.client.FlowingClient service) {
        super(service);
    }

    @Override
    public void handlePacket(Packet packet) {

    }
}