package josscoder.flowinghub.commons.utils;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

public record PacketSerializer(@Getter ByteBuf buffer) {

    public void writeString(String charset) {
        buffer.writeInt(charset.length());
        buffer.writeCharSequence(charset, StandardCharsets.UTF_8);
    }

    public String readString() {
        return buffer.readCharSequence(buffer.readInt(), StandardCharsets.UTF_8).toString();
    }

    public void writeInt(int i) {
        buffer.writeInt(i);
    }

    public int readInt() {
        return buffer.readInt();
    }

    public void writeFloat(float f) {
        buffer.writeFloat(f);
    }

    public float readFloat() {
        return buffer.readFloat();
    }

    public void writeDouble(double d) {
        buffer.writeDouble(d);
    }

    public double readDouble() {
        return buffer.readDouble();
    }

    public void writeBoolean(boolean b) {
        buffer.writeBoolean(b);
    }

    public boolean readBoolean() {
        return buffer.readBoolean();
    }

    public void writeByte(byte b) {
        buffer.writeByte(b);
    }

    public byte readByte() {
        return buffer.readByte();
    }

    public void writeBytes(byte[] b) {
        buffer.writeInt(b.length);
        buffer.writeBytes(b);
    }

    public byte[] readBytes() {
        int length = buffer.readInt();
        if (length > buffer.readableBytes()) {
            throw new IllegalStateException("Not enough bytes to read");
        } else {
            byte[] data = new byte[length];
            buffer.readBytes(data);
            return data;
        }
    }

}
