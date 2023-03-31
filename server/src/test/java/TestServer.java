import josscoder.flowinghub.commons.data.ServiceInfo;
import josscoder.flowinghub.server.NetServer;

public class TestServer {

    public static void main(String[] args) {
        NetServer netServer = new NetServer(ServiceInfo.builder()
                .port(11111)
                .id("server-1")
                .build());

        try {
            netServer.startup();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            netServer.shutdown();
        }
    }
}
