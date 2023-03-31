package josscoder.flowinghub.server.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import josscoder.flowinghub.commons.data.ServiceInfo;
import josscoder.flowinghub.commons.packet.Packet;
import josscoder.flowinghub.commons.packet.base.AuthRequestPacket;
import josscoder.flowinghub.commons.packet.base.AuthResponsePacket;
import josscoder.flowinghub.commons.pipeline.PacketHandler;

import java.net.InetSocketAddress;

public class ServerPacketHandler extends PacketHandler<josscoder.flowinghub.server.FlowingServer> {

    private static final String AUTH_TOKEN_ATTRIBUTE_KEY = "auth_token";

    public ServerPacketHandler(josscoder.flowinghub.server.FlowingServer service) {
        super(service);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Packet packet) {
            InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();

            ServiceInfo serviceInfo = service.getServiceInfo();
            String authToken = serviceInfo.getAuthToken();

            if (packet instanceof AuthRequestPacket authRequestPacket) {
                AuthResponsePacket authResponsePacket = new AuthResponsePacket();

                if (authRequestPacket.authToken.equalsIgnoreCase(authToken)) {
                    authResponsePacket.status = AuthResponsePacket.Status.SUCCESS;
                    authResponsePacket.serverId = serviceInfo.getId();
                } else {
                    authResponsePacket.status = AuthResponsePacket.Status.INVALID_TOKEN;
                }

                service.sendPacket(address, authResponsePacket);
                return;
            }

            String clientAuthToken = (String) ctx.channel().attr(AttributeKey.valueOf(AUTH_TOKEN_ATTRIBUTE_KEY)).get();

            if (clientAuthToken == null || !clientAuthToken.equalsIgnoreCase(authToken)) {
                ctx.close();
                return;
            }
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        service.getClientSessions().put(remoteAddress, ctx.channel());

        super.channelActive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        service.getClientSessions().remove(remoteAddress);

        super.channelUnregistered(ctx);
    }

    @Override
    public void handlePacket(Packet packet) {

    }
}
