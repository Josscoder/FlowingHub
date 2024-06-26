package josscoder.flowinghub.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import josscoder.flowinghub.commons.FlowingService;
import josscoder.flowinghub.commons.data.ServiceInfo;
import josscoder.flowinghub.commons.protocol.packet.Packet;
import josscoder.flowinghub.commons.protocol.codec.ProtocolCodec;
import josscoder.flowinghub.commons.pipeline.PacketDecoder;
import josscoder.flowinghub.server.pipeline.ServerPacketHandler;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<Void> sendPacket(InetSocketAddress address, Packet packet) {
        Channel connection = clientSessions.get(address);

        if (channel == null || !channel.isOpen() || connection == null) {
            return CompletableFuture.completedFuture(null);
        }

        ByteBuf buffer = channel.alloc().buffer();
        ProtocolCodec.encodePacketIntoBuf(buffer, packet);

        CompletableFuture<Void> future = new CompletableFuture<>();

        Runnable runnable = () -> {
            buffer.retain();

            connection.writeAndFlush(buffer).addListener(channelFuture -> {
                try {
                    if (!channelFuture.isSuccess()) {
                        future.completeExceptionally(channelFuture.cause());
                        logger.warn("Error sending the packet {}: ", packet.getClass().getSimpleName(), channelFuture.cause());
                    } else {
                        future.complete(null);
                        logger.debug("Packet {} was sent", packet.getClass().getSimpleName());
                    }
                } finally {
                    buffer.release();
                }
            });
        };

        if (packet.isAsyncPacket()) {
            CompletableFuture.runAsync(runnable);
        } else {
            runnable.run();
        }

        return future;
    }

    @Override
    public CompletableFuture<Void> sendPacket(Packet packet) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        clientSessions.keySet().forEach(address -> {
            CompletableFuture<Void> future = new CompletableFuture<>();

            CompletableFuture<Void> sendFuture = sendPacket(address, packet);
            sendFuture.whenComplete((v, ex) -> {
                if (ex != null) {
                    future.completeExceptionally(ex);
                } else {
                    future.complete(null);
                }
            });

            futures.add(future);
        });

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
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
