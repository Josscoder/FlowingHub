import josscoder.flowinghub.commons.data.ServiceInfo;

public class TestClient {

    public static void main(String[] args) {
        josscoder.flowinghub.client.FlowingClient flowingClient = new josscoder.flowinghub.client.FlowingClient(ServiceInfo.builder()
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
