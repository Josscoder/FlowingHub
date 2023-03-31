package josscoder.flowinghub.commons.packet;

import josscoder.flowinghub.commons.utils.PacketSerializer;
import org.xerial.snappy.Snappy;

import java.io.IOException;

public abstract class WeightyPacket extends Packet {

    public byte[] data;

    public WeightyPacket(byte pid) {
        super(pid);
    }

    @Override
    public void encode(PacketSerializer serializer) {
        byte[] compressedData;

        try {
            compressedData = Snappy.compress(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        serializer.writeBoolean(compressedData.length < data.length);
        serializer.writeBytes(compressedData.length < data.length ? compressedData : data);
    }

    @Override
    public void decode(PacketSerializer serializer) {
        boolean isCompressed = serializer.readBoolean();
        byte[] data = serializer.readBytes();

        try {
            this.data = isCompressed ? Snappy.uncompress(data) : data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
