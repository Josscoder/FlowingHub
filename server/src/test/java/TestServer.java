import josscoder.flowinghub.commons.data.ServiceInfo;

public class TestServer {

    public static void main(String[] args) {
        josscoder.flowinghub.server.FlowingServer flowingServer = new josscoder.flowinghub.server.FlowingServer(ServiceInfo.builder()
                .port(11111)
                .id("server-1")
                .build());

        try {
            flowingServer.startup();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            flowingServer.shutdown();
        }
    }
}
