package josscoder.flowinghub.commons.utils;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class ByteBufUtils {

    public static void writeString(ByteBuf buffer, String charset) {
        byte[] bytes = charset.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    public static String readString(ByteBuf buffer) {
        byte[] bytes = new byte[buffer.readInt()];
        buffer.readBytes(bytes);

        return new String(bytes, StandardCharsets.UTF_8);
    }
}
