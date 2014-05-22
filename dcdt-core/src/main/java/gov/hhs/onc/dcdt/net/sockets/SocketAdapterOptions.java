package gov.hhs.onc.dcdt.net.sockets;

import java.net.SocketOption;

public final class SocketAdapterOptions {
    public final static SocketOption<Integer> TIMEOUT = new ToolSocketOption<>("SO_TIMEOUT", Integer.class);

    private SocketAdapterOptions() {
    }
}
