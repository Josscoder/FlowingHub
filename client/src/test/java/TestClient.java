import io.netty.channel.ChannelFuture;
import josscoder.flowinghub.client.FlowingClient;
import josscoder.flowinghub.commons.data.ServiceInfo;
import josscoder.flowinghub.commons.protocol.packet.BasicPacket;
import josscoder.flowinghub.commons.protocol.packet.BatchPacket;
import josscoder.flowinghub.commons.protocol.packet.base.MessagePacket;

import java.util.ArrayList;
import java.util.List;

public class TestClient {

    public static void main(String[] args) {
        FlowingClient flowingClient = new FlowingClient(ServiceInfo.builder()
                .address("0.0.0.0")
                .port(11111)
                .id("client-1")
                .authToken("123")
                .build());

        try {
            ChannelFuture future = flowingClient.startup();

            Thread.sleep(5000);

            if (future.isSuccess()) {
                List<BasicPacket> packets = new ArrayList<>();

                for (int i = 0; i < 75; i++) {
                    MessagePacket packet = new MessagePacket();
                    packet.message = "Hello world = " + i;
                    packets.add(packet);
                }

                BatchPacket batchPacket = new BatchPacket();
                batchPacket.packets = packets;
                flowingClient.sendPacket(batchPacket);
            }

            Thread.sleep(500);

            System.out.println("Shutting down...");
            flowingClient.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            flowingClient.shutdown();
        }
    }
}
