package josscoder.flowinghub.commons.data;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServiceInfo {

    private final String address; //client only
    private final int port; //client-server
    private final String id; //client-server
}
