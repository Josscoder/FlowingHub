package josscoder.flowinghub.commons.data;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ServiceInfo {

    private final String address; //client only
    private final int port;
    private final String id;
    private final String authToken;
}
