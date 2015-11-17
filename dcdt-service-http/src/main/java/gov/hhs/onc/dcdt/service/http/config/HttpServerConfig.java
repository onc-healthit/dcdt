package gov.hhs.onc.dcdt.service.http.config;

import gov.hhs.onc.dcdt.http.HttpTransportProtocol;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import javax.annotation.Nonnegative;

public interface HttpServerConfig extends ToolServerConfig<HttpTransportProtocol> {
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
