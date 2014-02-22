package gov.hhs.onc.dcdt.nio.utils;

import gov.hhs.onc.dcdt.net.ToolSocketOption;
import gov.hhs.onc.dcdt.nio.channels.ChannelSocketOptions;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolMapUtils;
import java.io.IOException;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public abstract class ToolChannelUtils {
    public static <T extends SelectableChannel & ByteChannel & NetworkChannel> ByteBuffer writeAll(T channel, @Nullable ByteBuffer ... buffers)
        throws IOException {
        return writeAll(channel, ToolArrayUtils.asList(buffers));
    }

    public static <T extends SelectableChannel & ByteChannel & NetworkChannel> ByteBuffer writeAll(T channel, @Nullable Iterable<? extends ByteBuffer> buffers)
        throws IOException {
        if (buffers != null) {
            ByteBuffer writeBuffer = ToolBufferUtils.flip(ToolBufferUtils.putAll(ByteBuffer.allocate(ToolBufferUtils.remaining(buffers)), buffers));
            int writeSize = channel.write(writeBuffer);

            if (writeSize > 0) {
                return ByteBuffer.wrap(ToolBufferUtils.rewind(writeBuffer).array(), 0, writeSize);
            }
        }

        return ByteBuffer.allocate(0);
    }

    public static <T extends SelectableChannel & ByteChannel & NetworkChannel> ByteBuffer readAll(T channel) throws IOException {
        return readAll(channel, null, Integer.MAX_VALUE);
    }

    public static <T extends SelectableChannel & ByteChannel & NetworkChannel> ByteBuffer readAll(T channel, @Nullable ByteBuffer resultBuffer)
        throws IOException {
        return readAll(channel, resultBuffer, Integer.MAX_VALUE);
    }

    public static <T extends SelectableChannel & ByteChannel & NetworkChannel> ByteBuffer readAll(T channel, @Nullable ByteBuffer resultBuffer,
        @Nonnegative int readBufferSize) throws IOException {
        List<ByteBuffer> readBuffers = new ArrayList<>();
        ByteBuffer readBuffer;
        int readSize;

        do {
            if ((readSize = channel.read((readBuffer = ByteBuffer.allocate(readBufferSize)))) > 0) {
                readBuffers.add(ToolBufferUtils.flip(readBuffer));
            }
        } while (readSize != -1);

        return ToolBufferUtils.putAll(((resultBuffer != null) ? resultBuffer : ByteBuffer.allocate(ToolBufferUtils.remaining(readBuffers))), readBuffers);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends SelectableChannel & NetworkChannel> T setOption(T channel, Entry<? extends SocketOption<?>, ?> optEntry) throws IOException {
        return setOptions(channel, ToolMapUtils.putAll(new HashMap<SocketOption<?>, Object>(1), optEntry));
    }

    @SuppressWarnings({ "unchecked" })
    public static <T extends SelectableChannel & NetworkChannel> T setOptions(T channel, Map<? extends SocketOption<?>, ?> optMap) throws IOException {
        for (SocketOption<?> opt : optMap.keySet()) {
            if (ToolClassUtils.isAssignable(opt.getClass(), ToolSocketOption.class)) {
                if (opt == ChannelSocketOptions.CHANNEL_BLOCKING) {
                    channel.configureBlocking(ChannelSocketOptions.CHANNEL_BLOCKING.type().cast(optMap.get(opt)));
                }
            } else {
                channel.setOption(((SocketOption<Object>) opt), optMap.get(opt));
            }
        }

        return channel;
    }
}
