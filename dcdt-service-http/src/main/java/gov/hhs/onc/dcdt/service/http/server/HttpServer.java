package gov.hhs.onc.dcdt.service.http.server;

import gov.hhs.onc.dcdt.beans.ToolLifecycleBean;
import gov.hhs.onc.dcdt.service.http.config.HttpServerConfig;

public interface HttpServer extends ToolLifecycleBean {
    public HttpServerConfig getConfig();
}
