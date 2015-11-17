package gov.hhs.onc.dcdt.service.config.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolConnectionBean;
import gov.hhs.onc.dcdt.net.TransportProtocol;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import javax.annotation.Nonnegative;

public abstract class AbstractToolServerConfig<T extends TransportProtocol> extends AbstractToolConnectionBean<T> implements ToolServerConfig<T> {
    protected String protocol;
    protected int backlog;
    protected int connTimeout;

    protected AbstractToolServerConfig(String protocol) {
        this.protocol = protocol;
    }

    @Nonnegative
    @Override
    public int getBacklog() {
        return this.backlog;
    }

    @Override
    public void setBacklog(@Nonnegative int backlog) {
        this.backlog = backlog;
    }

    @Nonnegative
    @Override
    public int getConnectTimeout() {
        return this.connTimeout;
    }

    @Override
    public void setConnectTimeout(@Nonnegative int connTimeout) {
        this.connTimeout = connTimeout;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }
}
