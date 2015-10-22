package gov.hhs.onc.dcdt.service.http.config;

import gov.hhs.onc.dcdt.beans.ToolConnectionBean;
import javax.annotation.Nonnegative;

public interface HttpServerConfig extends ToolConnectionBean {
    @Nonnegative
    public int getConnectTimeout();

    public void setConnectTimeout(@Nonnegative int connTimeout);

    @Nonnegative
    public int getMaxContentLength();

    public void setMaxContentLength(@Nonnegative int maxContentLen);

    @Nonnegative
    public int getReadTimeout();

    public void setReadTimeout(@Nonnegative int readTimeout);

    @Nonnegative
    public int getWriteTimeout();

    public void setWriteTimeout(@Nonnegative int writeTimeout);
}
