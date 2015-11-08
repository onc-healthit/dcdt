package gov.hhs.onc.dcdt.service.config;

import gov.hhs.onc.dcdt.beans.ToolConnectionBean;
import javax.annotation.Nonnegative;

public interface ToolServerConfig extends ToolConnectionBean {
    @Nonnegative
    public int getBacklog();

    public void setBacklog(@Nonnegative int backlog);

    @Nonnegative
    public int getConnectTimeout();

    public void setConnectTimeout(@Nonnegative int connTimeout);

    public String getProtocol();
}
