package gov.hhs.onc.dcdt.nio.channels;

import gov.hhs.onc.dcdt.net.InetProtocol;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;

public interface SelectionAttachment {
    public InetProtocol getProtocol();

    public void setProtocol(InetProtocol protocol);

    public ByteBuffer getRequestBuffer();

    public void setRequestBuffer(ByteBuffer reqBuffer);

    public ByteBuffer getResponseBuffer();

    public void setResponseBuffer(ByteBuffer respBuffer);

    @Nullable
    public SocketAddress getSocketAddress();

    @Nullable
    public SocketAddress setSocketAddress(@Nullable SocketAddress socketAddr);
}
