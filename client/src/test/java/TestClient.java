import io.netty.channel.ChannelFuture;
import josscoder.flowinghub.client.FlowingClient;
import josscoder.flowinghub.commons.data.ServiceInfo;

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
                /*System.out.println("Enviando mensaje al servidor");
                flowingClient.sendPacket(new MessagePacket(){{
                    message = "Hola";
                }});*/
            }

            Thread.sleep(500);

            System.out.println("Apagando sistemas! buena noches");
            flowingClient.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            flowingClient.shutdown();
        }
    }
}
