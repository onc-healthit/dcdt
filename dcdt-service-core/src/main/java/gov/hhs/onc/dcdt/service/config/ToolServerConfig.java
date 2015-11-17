package gov.hhs.onc.dcdt.service.config;

import gov.hhs.onc.dcdt.beans.ToolConnectionBean;
import gov.hhs.onc.dcdt.net.TransportProtocol;
import javax.annotation.Nonnegative;

public interface ToolServerConfig<T extends TransportProtocol> extends ToolConnectionBean<T> {
    @Nonnegative
    public int getBacklog();

    public void setBacklog(@Nonnegative int backlog);

    @Nonnegative
    public int getConnectTimeout();

    public void setConnectTimeout(@Nonnegative int connTimeout);

    public String getProtocol();
}
