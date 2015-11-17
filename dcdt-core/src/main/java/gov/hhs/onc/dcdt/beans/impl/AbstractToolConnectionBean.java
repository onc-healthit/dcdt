package gov.hhs.onc.dcdt.beans.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolConnectionBean;
import gov.hhs.onc.dcdt.net.TransportProtocol;
import gov.hhs.onc.dcdt.net.utils.ToolInetAddressUtils;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AbstractToolConnectionBean<T extends TransportProtocol> extends AbstractToolNamedBean implements ToolConnectionBean<T> {
    protected InetAddress host;
    protected int port;
    protected T transportProtocol;

    @Nullable
    @Override
    public InetSocketAddress toSocketAddress() {
        return this.toSocketAddress(false);
    }

    @Nullable
    @Override
    public InetSocketAddress toSocketAddress(boolean forConn) {
        return (this.hasHost() ? new InetSocketAddress(this.getHost(forConn), this.getPort()) : null);
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

    @JsonProperty("port")
    @Nonnegative
    @Override
    @Transient
    public int getPort() {
        return this.getPort(true);
    }

    @Nonnegative
    @Override
    @Transient
    public int getPort(boolean useDefault) {
        return (ToolNumberUtils.isPositive(this.port) ? this.port : (useDefault ? this.transportProtocol.getDefaultPort() : this.port));
    }

    @Override
    public void setPort(@Nonnegative int port) {
        this.port = port;
    }

    @Override
    @Transient
    public T getTransportProtocol() {
        return this.transportProtocol;
    }

    @Override
    public void setTransportProtocol(T transportProtocol) {
        this.transportProtocol = transportProtocol;
    }
}
