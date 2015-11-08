package gov.hhs.onc.dcdt.service.http.wrapper;

import gov.hhs.onc.dcdt.service.http.HttpService;
import gov.hhs.onc.dcdt.service.http.config.HttpServerConfig;
import gov.hhs.onc.dcdt.service.http.server.HttpServer;
import gov.hhs.onc.dcdt.service.wrapper.ToolServiceWrapper;

public interface HttpServiceWrapper extends ToolServiceWrapper<HttpServerConfig, HttpServer, HttpService> {
}
