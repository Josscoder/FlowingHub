import io.netty.channel.ChannelFuture;
import josscoder.flowinghub.client.FlowingClient;
import josscoder.flowinghub.commons.data.ServiceInfo;
import josscoder.flowinghub.commons.packet.base.MessagePacket;

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
                System.out.println("Sending message");
                flowingClient.sendPacket(new MessagePacket(){{
                    message = "Hello world";
                }});
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
