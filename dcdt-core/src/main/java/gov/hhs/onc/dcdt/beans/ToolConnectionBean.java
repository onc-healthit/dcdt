package gov.hhs.onc.dcdt.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.net.TransportProtocol;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public interface ToolConnectionBean<T extends TransportProtocol> extends ToolNamedBean {
    @Nullable
    public InetSocketAddress toSocketAddress();

    @Nullable
    public InetSocketAddress toSocketAddress(boolean forConn);

    public boolean hasHost();

    @JsonProperty("host")
    @Nullable
    public InetAddress getHost();

    @Nullable
    public InetAddress getHost(boolean forConn);

    public void setHost(@Nullable InetAddress host);

    @JsonProperty("port")
    @Nonnegative
    public int getPort();

    @Nonnegative
    public int getPort(boolean useDefault);

    public void setPort(@Nonnegative int port);

    public T getTransportProtocol();

    public void setTransportProtocol(T transportProtocol);
}
