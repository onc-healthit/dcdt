package gov.hhs.onc.dcdt.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import javax.annotation.Nullable;

public interface ToolConnectionBean extends ToolNamedBean {
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

    public boolean hasPort();

    @JsonProperty("port")
    public int getPort();

    public void setPort(int port);

    @JsonProperty("ssl")
    public boolean isSsl();
}
