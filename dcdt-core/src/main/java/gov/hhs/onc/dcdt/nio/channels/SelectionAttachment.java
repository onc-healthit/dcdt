package gov.hhs.onc.dcdt.nio.channels;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;

public interface SelectionAttachment {
    public ByteBuffer getRequestBuffer();

    public void setRequestBuffer(ByteBuffer reqBuffer);

    public ByteBuffer getResponseBuffer();

    public void setResponseBuffer(ByteBuffer respBuffer);

    @Nullable
    public SocketAddress getSocketAddress();

    @Nullable
    public SocketAddress setSocketAddress(@Nullable SocketAddress socketAddr);
}
