package gov.hhs.onc.dcdt.nio.channels;

import gov.hhs.onc.dcdt.net.ToolSocketOption;
import java.net.SocketOption;

public interface ChannelSocketOptions {
    public final static SocketOption<Boolean> CHANNEL_BLOCKING = new ToolSocketOption<>("CHANNEL_BLOCKING", Boolean.class);
}
