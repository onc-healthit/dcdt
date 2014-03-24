package gov.hhs.onc.dcdt.nio.utils;

import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public abstract class ToolBufferUtils {
    public static byte[] get(ByteBuffer buffer) {
        byte[] data = new byte[buffer.limit()];

        buffer.get(data);

        return data;
    }

    public static ByteBuffer putAll(ByteBuffer resultBuffer, @Nullable ByteBuffer ... buffers) {
        return putAll(resultBuffer, ToolArrayUtils.asList(buffers));
    }

    public static ByteBuffer putAll(ByteBuffer resultBuffer, @Nullable Iterable<? extends ByteBuffer> buffers) {
        if (buffers != null) {
            for (ByteBuffer buffer : buffers) {
                if (!resultBuffer.hasRemaining()) {
                    break;
                }

                if (buffer.hasRemaining()) {
                    resultBuffer.put(ByteBuffer.wrap(get(buffer)));
                }
            }
        }

        return resultBuffer;
    }

    public static ByteBuffer compact(ByteBuffer buffer) {
        return buffer.compact();
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Buffer> T clear(T buffer) {
        return ((T) buffer.clear());
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Buffer> T flip(T buffer) {
        return ((T) buffer.flip());
    }

    public static <T extends Buffer> T limit(T buffer, @Nonnegative int limit) {
        return limit(buffer, limit, false);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Buffer> T limit(T buffer, int limit, boolean adjustToBounds) {
        return ((T) buffer.limit((adjustToBounds ? Math.min(Math.max(limit, 0), buffer.capacity()) : limit)));
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Buffer> T mark(T buffer) {
        return ((T) buffer.mark());
    }

    public static <T extends Buffer> T position(T buffer, @Nonnegative int pos) {
        return position(buffer, pos, false);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Buffer> T position(T buffer, int pos, boolean adjustToBounds) {
        return ((T) buffer.position((adjustToBounds ? Math.min(Math.max(pos, 0), buffer.limit()) : pos)));
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Buffer> T reset(T buffer) {
        return ((T) buffer.reset());
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends Buffer> T rewind(T buffer) {
        return ((T) buffer.rewind());
    }
}
