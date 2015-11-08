package gov.hhs.onc.dcdt.service.http;

import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.http.config.HttpServerConfig;
import gov.hhs.onc.dcdt.service.http.server.HttpServer;

public interface HttpService extends ToolService<HttpServerConfig, HttpServer> {
}
