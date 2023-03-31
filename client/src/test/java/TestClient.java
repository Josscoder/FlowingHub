import josscoder.flowinghub.client.NetClient;
import josscoder.flowinghub.commons.data.ServiceInfo;

public class TestClient {

    public static void main(String[] args) {
        NetClient netClient = new NetClient(ServiceInfo.builder()
                .address("0.0.0.0")
                .port(11111)
                .id("client-1")
                .build());

        try {
            netClient.startup();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            netClient.shutdown();
        }
    }
}
