package josscoder.flowinghub.server.pipeline;

import io.netty.channel.ChannelHandlerContext;
import josscoder.flowinghub.commons.packet.Packet;
import josscoder.flowinghub.commons.pipeline.PacketHandler;

import java.net.InetSocketAddress;

public class ServerPacketHandler extends PacketHandler<josscoder.flowinghub.server.FlowingServer> {

    public ServerPacketHandler(josscoder.flowinghub.server.FlowingServer service) {
        super(service);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        service.getClientSessions().put(remoteAddress, ctx.channel());

        super.channelActive(ctx);
    }

    @Override
    public void handlePacket(Packet packet) {

    }
}
