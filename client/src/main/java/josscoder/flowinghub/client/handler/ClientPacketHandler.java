package josscoder.flowinghub.client.handler;

import josscoder.flowinghub.client.FlowingClient;
import josscoder.flowinghub.commons.packet.Packet;
import josscoder.flowinghub.commons.pipeline.PacketHandler;

public class ClientPacketHandler extends PacketHandler<FlowingClient> {

    public ClientPacketHandler(FlowingClient service) {
        super(service);
    }

    @Override
    public void handlePacket(Packet packet) {

    }
}