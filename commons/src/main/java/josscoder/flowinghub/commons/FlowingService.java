package josscoder.flowinghub.commons;

import io.netty.channel.ChannelFuture;
import josscoder.flowinghub.commons.data.ServiceInfo;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
public abstract class FlowingService {

    protected final ServiceInfo serviceInfo;
    protected final Logger logger;

    public FlowingService(ServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
        this.logger = LogManager.getLogger(FlowingService.class);
    }

    public abstract ChannelFuture startup() throws InterruptedException;

    public abstract void shutdown();
}
