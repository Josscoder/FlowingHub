package josscoder.flowinghub.client.pipeline;

import josscoder.flowinghub.client.FlowingClient;
import josscoder.flowinghub.commons.protocol.packet.Packet;
import josscoder.flowinghub.commons.protocol.packet.base.AuthResponsePacket;
import josscoder.flowinghub.commons.pipeline.PacketHandler;

public class ClientPacketHandler extends PacketHandler<FlowingClient> {

    public ClientPacketHandler(FlowingClient service) {
        super(service);
    }

    @Override
    public void handlePacket(Packet packet) {
        if (!(packet instanceof AuthResponsePacket authResponsePacket)) {
            return;
        }

        if (authResponsePacket.status.equals(AuthResponsePacket.Status.SUCCESS)) {
            service.getLogger().info("You have authenticated with a server ({}) and now you can use the communication bridge perfectly", authResponsePacket.serverId);
            return;
        }

        if (authResponsePacket.status.equals(AuthResponsePacket.Status.INVALID_TOKEN)) {
            service.shutdown();
            service.getLogger().error("The 'authToken' is not the same as the server you sent the request to, you will not be able to use the communication bridge");
        }
    }
}