package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolBoundBean;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import javax.annotation.Nonnegative;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class AbstractToolBoundBean extends AbstractToolNamedBean implements ToolBoundBean {
    protected InetAddress bindAddr;
    protected int bindPort;

    @Override
    @Transient
    public InetSocketAddress getBindSocketAddress() {
        return new InetSocketAddress(this.bindAddr, this.bindPort);
    }

    @Override
    @Transient
    public InetAddress getBindAddress() {
        return this.bindAddr;
    }

    @Override
    public void setBindAddress(InetAddress bindAddr) {
        this.bindAddr = bindAddr;
    }

    @Nonnegative
    @Override
    @Transient
    public int getBindPort() {
        return bindPort;
    }

    @Override
    public void setBindPort(@Nonnegative int bindPort) {
        this.bindPort = bindPort;
    }
}
