package josscoder.flowinghub.commons.protocol;

public interface ProtocolInfo {

    byte AUTH_REQUEST_PACKET = 0x01;
    byte AUTH_RESPONSE_PACKET = 0x02;
    byte MESSAGE_PACKET = 0x03;
    byte BATCH_PACKET = 0x04;
}
