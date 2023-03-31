package josscoder.flowinghub.commons;

import io.netty.channel.ChannelFuture;
import josscoder.flowinghub.commons.data.ServiceInfo;
import josscoder.flowinghub.commons.packet.Packet;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;

@Getter
public abstract class FlowingService {

    protected final ServiceInfo serviceInfo;
    protected final Logger logger;

    public FlowingService(ServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
        this.logger = LogManager.getLogger(FlowingService.class);
    }

    public abstract ChannelFuture startup() throws InterruptedException;

    public abstract CompletableFuture<Void> sendPacket(Packet packet);

    public abstract void shutdown();
}
