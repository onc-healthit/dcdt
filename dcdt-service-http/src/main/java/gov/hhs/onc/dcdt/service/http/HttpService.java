package gov.hhs.onc.dcdt.service.http;

import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.http.server.HttpServer;
import java.util.List;
import javax.annotation.Nullable;

public interface HttpService extends ToolService {
    public boolean hasServers();

    @Nullable
    public List<HttpServer> getServers();
}
