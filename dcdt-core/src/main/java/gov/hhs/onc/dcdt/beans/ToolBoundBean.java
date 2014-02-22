package gov.hhs.onc.dcdt.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import javax.annotation.Nonnegative;

public interface ToolBoundBean extends ToolNamedBean {
    public InetSocketAddress getBindSocketAddress();

    @JsonProperty("bindSocketAddr")
    public InetAddress getBindAddress();

    public void setBindAddress(InetAddress bindAddr);

    @JsonProperty("bindPort")
    @Nonnegative
    public int getBindPort();

    public void setBindPort(@Nonnegative int bindPort);
}
