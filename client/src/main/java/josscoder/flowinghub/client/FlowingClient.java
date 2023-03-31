package josscoder.flowinghub.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import josscoder.flowinghub.client.handler.ClientPacketHandler;
import josscoder.flowinghub.commons.FlowingService;
import josscoder.flowinghub.commons.data.ServiceInfo;
import josscoder.flowinghub.commons.packet.Packet;
import josscoder.flowinghub.commons.pipeline.PacketDecoder;
import josscoder.flowinghub.commons.pipeline.PacketEncoder;
import lombok.Getter;

import java.net.InetSocketAddress;

public class FlowingClient extends FlowingService {

    @Getter
    private static FlowingClient instance;

    private Channel channel;
    private NioEventLoopGroup group;

    public FlowingClient(ServiceInfo serviceInfo) {
        super(serviceInfo);

        instance = this;
    }

    @Override
    public void startup() throws InterruptedException {
        group = new NioEventLoopGroup();
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(serviceInfo.getAddress(), serviceInfo.getPort());

            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(inetSocketAddress)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new PacketEncoder());
                            pipeline.addLast(new PacketDecoder());
                            pipeline.addLast(new ClientPacketHandler(FlowingClient.getInstance()));
                        }
                    });

            ChannelFuture future = bootstrap.connect().await();
            if (future.isSuccess()) {
                logger.info("NetClient has successfully connected to NetServer-TCP{}", inetSocketAddress);
            } else {
                logger.error("No active NetServer found to connect to");
                return;
            }

            channel = future.channel();
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void sendPacket(Packet packet) {
        ByteBuf buffer = channel.alloc().buffer();
        buffer.writeByte(packet.getPid());
        packet.encode(buffer);

        channel.writeAndFlush(buffer).addListener(future -> {
            if (!future.isSuccess()) {
                logger.warn("Error sending the packet {}: ", packet.getClass().getSimpleName(), future.cause());
            } else {
                logger.debug("Packet {} was sent", packet.getClass().getSimpleName());
            }
        });
    }

    @Override
    public void shutdown() {
        if (channel != null && channel.isActive()) {
            channel.close();
        }

        if (group != null && !group.isShutdown()) {
            group.shutdownGracefully();
        }
    }
}
