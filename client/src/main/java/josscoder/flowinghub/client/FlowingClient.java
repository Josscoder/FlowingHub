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
import josscoder.flowinghub.client.pipeline.ClientPacketHandler;
import josscoder.flowinghub.commons.FlowingService;
import josscoder.flowinghub.commons.data.ServiceInfo;
import josscoder.flowinghub.commons.packet.Packet;
import josscoder.flowinghub.commons.packet.base.AuthRequestPacket;
import josscoder.flowinghub.commons.packet.codec.ProtocolCodec;
import josscoder.flowinghub.commons.pipeline.PacketDecoder;
import josscoder.flowinghub.commons.pipeline.PacketEncoder;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

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
    public ChannelFuture startup() throws InterruptedException {
        group = new NioEventLoopGroup();
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

        ChannelFuture future = bootstrap.connect();
        future.addListener(addFuture -> {
            if (addFuture.isSuccess()) {
                logger.warn("FlowingClient has successfully connected to FlowingServer-TCP{}, authenticating....", inetSocketAddress);
                channel = future.channel();

                AuthRequestPacket authRequestPacket = new AuthRequestPacket();
                authRequestPacket.authToken = serviceInfo.getAuthToken();
                sendPacket(authRequestPacket);
            } else {
                logger.error("No active FlowingServer found to connect to");
                channel = null;
            }
        });

        return future;
    }

    @Override
    public CompletableFuture<Void> sendPacket(Packet packet) {
        if (channel == null || !channel.isOpen()) {
            return CompletableFuture.completedFuture(null);
        }

        ByteBuf buffer = channel.alloc().buffer();
        ProtocolCodec.encodePacketIntoBuf(buffer, packet);

        CompletableFuture<Void> future = new CompletableFuture<>();

        Runnable runnable = () -> {
            buffer.retain();
            channel.writeAndFlush(buffer).addListener(channelFuture -> {
                try {
                    if (!channelFuture.isSuccess()) {
                        future.completeExceptionally(channelFuture.cause());
                        logger.warn("Error sending the packet {}: ", packet.getClass().getSimpleName(), channelFuture.cause());
                    } else {
                        logger.debug("Packet {} was sent", packet.getClass().getSimpleName());
                        future.complete(null);
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
    public void shutdown() {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }

        if (group != null && !group.isShutdown()) {
            group.shutdownGracefully();
        }
    }
}
