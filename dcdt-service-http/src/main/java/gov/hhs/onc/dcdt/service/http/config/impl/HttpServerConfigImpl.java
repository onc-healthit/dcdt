package gov.hhs.onc.dcdt.service.http.config.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolConnectionBean;
import gov.hhs.onc.dcdt.service.http.config.HttpServerConfig;
import javax.annotation.Nonnegative;

public class HttpServerConfigImpl extends AbstractToolConnectionBean implements HttpServerConfig {
    private int connTimeout;
    private int maxContentLen;
    private int readTimeout;
    private int writeTimeout;

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
