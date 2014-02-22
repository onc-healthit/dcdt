package gov.hhs.onc.dcdt.nio.utils;

import gov.hhs.onc.dcdt.net.ToolSocketOption;
import gov.hhs.onc.dcdt.nio.channels.ChannelSocketOptions;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolMapUtils;
import java.io.IOException;
import java.net.SocketOption;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SelectableChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class ToolChannelUtils {
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
