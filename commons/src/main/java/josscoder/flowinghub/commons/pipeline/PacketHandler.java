package josscoder.flowinghub.commons.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import josscoder.flowinghub.commons.FlowingService;
import josscoder.flowinghub.commons.packet.Packet;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PacketHandler<T extends FlowingService> extends ChannelInboundHandlerAdapter {

    protected final T service;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Packet) {
            handlePacket((Packet) msg);
        } else {
            super.channelRead(ctx, msg);
        }
    }

    public abstract void handlePacket(Packet packet);
}
