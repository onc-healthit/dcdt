package gov.hhs.onc.dcdt.net.sockets;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.net.InetProtocol;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;

public interface SocketRequest extends ToolBean {
    public InetProtocol getProtocol();

    @Nullable
    public SocketAddress getRemoteAddress();

    public void setRemoteAddress(@Nullable SocketAddress remoteAddr);

    public ByteBuffer getRequestBuffer();

    public ByteBuffer getResponseBuffer();
}
