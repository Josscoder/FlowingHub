package josscoder.flowinghub.commons.packet;

public interface ProtocolInfo {

    byte AUTH_REQUEST_PACKET = 0x01;
    byte AUTH_RESPONSE_PACKET = 0x02;
    byte MESSAGE_PACKET = 0x03;
    byte BATCH_PACKET = 0x04;
    byte WEIGHTY_PACKET = 0x05;
}
