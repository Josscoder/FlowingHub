package josscoder.flowinghub.commons.packet;

import josscoder.flowinghub.commons.utils.PacketSerializer;
import org.xerial.snappy.Snappy;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;

public class WeightyPacket extends Packet {

    public ByteBuf data;

    public WeightyPacket() {
        super(ProtocolInfo.WEIGHTY_PACKET);
    }

    public void encode(PacketSerializer serializer) {
        byte[] bytesToWrite;

        try {
            byte[] uncompressedData = new byte[data.readableBytes()];
            data.readBytes(uncompressedData);
            byte[] compressedData = Snappy.compress(uncompressedData);
            bytesToWrite = compressedData.length < uncompressedData.length ? compressedData : uncompressedData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            data.release();
        }

        serializer.writeBytes(bytesToWrite);
    }

    public void decode(PacketSerializer serializer) {
        byte[] dataBytes = serializer.readBytes();

        try {
            byte[] uncompressedData = Snappy.uncompress(dataBytes);
            this.data = Unpooled.wrappedBuffer(uncompressedData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
