package gov.hhs.onc.dcdt.nio.utils;

import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Collection;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;

public abstract class ToolBufferUtils {
    public static class ByteBufferDataTransformer implements Transformer<ByteBuffer, byte[]> {
        public final static ByteBufferDataTransformer INSTANCE = new ByteBufferDataTransformer();

        @Override
        public byte[] transform(ByteBuffer buffer) {
            return get(buffer);
        }
    }

    public static class ByteBufferWrappingTransformer implements Transformer<byte[], ByteBuffer> {
        public final static ByteBufferWrappingTransformer INSTANCE = new ByteBufferWrappingTransformer();

        @Override
        public ByteBuffer transform(byte[] data) {
            return ByteBuffer.wrap(data);
        }
    }

    public static Collection<byte[]> getAll(@Nullable ByteBuffer ... buffers) {
        return getAll(ToolArrayUtils.asList(buffers));
    }

    public static Collection<byte[]> getAll(@Nullable Iterable<? extends ByteBuffer> buffers) {
        return CollectionUtils.collect(buffers, ByteBufferDataTransformer.INSTANCE);
    }

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

    public static Collection<ByteBuffer> wrapAll(@Nullable byte[] ... dataItems) {
        return wrapAll(ToolArrayUtils.asList(dataItems));
    }

    public static Collection<ByteBuffer> wrapAll(@Nullable Iterable<byte[]> dataItems) {
        return CollectionUtils.collect(dataItems, ByteBufferWrappingTransformer.INSTANCE);
    }

    @Nonnegative
    public static int remaining(@Nullable Buffer ... buffers) {
        return remaining(ToolArrayUtils.asList(buffers));
    }

    @Nonnegative
    public static int remaining(@Nullable Iterable<? extends Buffer> buffers) {
        int remainingTotal = 0;

        if (buffers != null) {
            for (Buffer buffer : buffers) {
                remainingTotal += buffer.remaining();
            }
        }

        return remainingTotal;
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
