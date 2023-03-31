package josscoder.flowinghub.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import josscoder.flowinghub.commons.FlowingService;
import josscoder.flowinghub.commons.data.ServiceInfo;
import josscoder.flowinghub.commons.packet.Packet;
import josscoder.flowinghub.commons.pipeline.PacketDecoder;
import josscoder.flowinghub.commons.pipeline.PacketEncoder;
import josscoder.flowinghub.commons.utils.PacketSerializer;
import josscoder.flowinghub.server.pipeline.ServerPacketHandler;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class FlowingServer extends FlowingService {

    @Getter
    private static FlowingServer instance;

    private Channel channel;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;

    @Getter
    private final Map<InetSocketAddress, Channel> clientSessions = new HashMap<>();

    public FlowingServer(ServiceInfo serviceInfo) {
        super(serviceInfo);

        instance = this;
    }

    @Override
    public ChannelFuture startup() throws InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ChannelFuture future;

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new PacketDecoder());
                            pipeline.addLast(new PacketEncoder());
                            pipeline.addLast(new ServerPacketHandler(FlowingServer.getInstance()));
                        }
                    });

            future = bootstrap.bind(serviceInfo.getPort());
            future.addListener((ChannelFutureListener) addFuture-> {
                if (addFuture.isSuccess()) {
                    channel = addFuture.channel();
                    logger.info("A FlowingServer connection was created on TCP/{}", new InetSocketAddress(serviceInfo.getPort()));
                } else {
                    logger.error("Could not create a FlowingServer connection, try using a different port");
                }
            });

            channel = future.channel();
            channel.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

        return future;
    }


    public void sendPacket(InetSocketAddress inetSocketAddress, Packet packet) {
        if (channel == null || !channel.isOpen()) {
            return;
        }

        PacketSerializer serializer = new PacketSerializer(channel.alloc().buffer());
        serializer.writeByte(packet.getPid());

        packet.encode(serializer);

        Channel connection = clientSessions.get(inetSocketAddress);
        connection.writeAndFlush(serializer.buffer()).addListener(future -> {
            if (!future.isSuccess()) {
                logger.warn("Error sending the packet {}: ", packet.getClass().getSimpleName(), future.cause());
            } else {
                logger.debug("Packet {} was sent", packet.getClass().getSimpleName());
            }
        });
    }

    public void sendPacket(Packet packet) {
        clientSessions.keySet().forEach(clientSession -> sendPacket(clientSession, packet));
    }

    @Override
    public void shutdown() {
        if (channel != null && channel.isActive()) {
            channel.close();
        }

        if (workerGroup != null && !workerGroup.isShutdown()) {
            workerGroup.shutdownGracefully();
        }

        if (bossGroup != null && !bossGroup.isShutdown()) {
            bossGroup.shutdownGracefully();
        }
    }
}
