package josscoder.flowinghub.commons.utils;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

public record PacketSerializer(@Getter ByteBuf buffer) {

    public void writeString(String charset) {
        byte[] bytes = charset.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    public String readString() {
        byte[] bytes = new byte[buffer.readInt()];
        buffer.readBytes(bytes);

        return new String(bytes, StandardCharsets.UTF_8);
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
        return buffer.readBytes(buffer.readInt()).array();
    }
}
