package gov.hhs.onc.dcdt.beans.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolConnectionBean;
import gov.hhs.onc.dcdt.net.utils.ToolInetAddressUtils;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import javax.annotation.Nullable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AbstractToolConnectionBean extends AbstractToolNamedBean implements ToolConnectionBean {
    protected InetAddress host;
    protected int port;

    @Nullable
    @Override
    public InetSocketAddress toSocketAddress() {
        return this.toSocketAddress(false);
    }

    @Nullable
    @Override
    public InetSocketAddress toSocketAddress(boolean forConn) {
        return ((this.hasHost() && this.hasPort()) ? new InetSocketAddress(this.getHost(forConn), this.port) : null);
    }

    @Override
    public boolean hasHost() {
        return (this.host != null);
    }

    @JsonProperty("host")
    @Nullable
    @Override
    @Transient
    public InetAddress getHost() {
        return this.getHost(false);
    }

    @Nullable
    @Override
    @Transient
    public InetAddress getHost(boolean forConn) {
        return ((this.hasHost() && forConn) ? ToolInetAddressUtils.getConnectionAddress(this.host) : this.host);
    }

    @Override
    public void setHost(@Nullable InetAddress host) {
        this.host = host;
    }

    @Override
    public boolean hasPort() {
        return ToolNumberUtils.isPositive(this.port);
    }

    @JsonProperty("port")
    @Override
    @Transient
    public int getPort() {
        return port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    @Transient
    public boolean isSsl() {
        return false;
    }
}
