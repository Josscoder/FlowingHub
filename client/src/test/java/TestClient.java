import josscoder.flowinghub.client.FlowingClient;
import josscoder.flowinghub.commons.data.ServiceInfo;

public class TestClient {

    public static void main(String[] args) {
        FlowingClient flowingClient = new FlowingClient(ServiceInfo.builder()
                .address("0.0.0.0")
                .port(11111)
                .id("client-1")
                .build());

        try {
            flowingClient.startup();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            flowingClient.shutdown();
        }
    }
}
