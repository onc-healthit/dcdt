package gov.hhs.onc.dcdt.service.http.config.impl;

import gov.hhs.onc.dcdt.service.config.impl.AbstractToolServerConfig;
import gov.hhs.onc.dcdt.service.http.config.HttpServerConfig;
import javax.annotation.Nonnegative;

public class HttpServerConfigImpl extends AbstractToolServerConfig implements HttpServerConfig {
    private int maxContentLen;
    private int readTimeout;
    private int writeTimeout;

    public HttpServerConfigImpl() {
        super("HTTP");
    }

    @Override
    public int getMaxContentLength() {
        return this.maxContentLen;
    }

    @Override
    public void setMaxContentLength(@Nonnegative int maxContentLen) {
        this.maxContentLen = maxContentLen;
    }

    @Nonnegative
    @Override
    public int getReadTimeout() {
        return this.readTimeout;
    }

    @Override
    public void setReadTimeout(@Nonnegative int readTimeout) {
        this.readTimeout = readTimeout;
    }

    @Nonnegative
    @Override
    public int getWriteTimeout() {
        return this.writeTimeout;
    }

    @Override
    public void setWriteTimeout(@Nonnegative int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }
}
