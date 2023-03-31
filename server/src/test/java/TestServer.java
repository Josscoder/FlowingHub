import josscoder.flowinghub.commons.data.ServiceInfo;
import josscoder.flowinghub.server.FlowingServer;

public class TestServer {

    public static void main(String[] args) {
        FlowingServer flowingServer = new FlowingServer(ServiceInfo.builder()
                .port(11111)
                .id("server-1")
                .authToken("123")
                .build());

        try {
            flowingServer.startup();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            flowingServer.shutdown();
        }

        while (true) {}
    }
}
