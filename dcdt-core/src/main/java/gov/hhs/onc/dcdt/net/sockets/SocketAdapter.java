package gov.hhs.onc.dcdt.net.sockets;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.net.InetProtocol;
import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.util.Map;
import javax.annotation.Nonnegative;

public interface SocketAdapter<T extends Closeable> extends Closeable, ToolBean {
    public void bind(SocketAddress bindSocketAddr) throws IOException;

    public void setOptions(Map<? extends SocketOption<?>, ?> optMap) throws IOException;

    public <U> void setOption(SocketOption<U> optKey, U optValue) throws IOException;

    public boolean isBound();

    public boolean isClosed();

    public InetProtocol getProtocol();

    @Nonnegative
    public int getReceiveBufferSize() throws IOException;

    public void setReceiveBufferSize(@Nonnegative int receiveBufferSize) throws IOException;

    public boolean getReuseAddress() throws IOException;

    public void setReuseAddress(boolean reuseAddr) throws IOException;

    public T getSocket();

    @Nonnegative
    public int getTimeout() throws IOException;

    public void setTimeout(@Nonnegative int timeout) throws IOException;
}
