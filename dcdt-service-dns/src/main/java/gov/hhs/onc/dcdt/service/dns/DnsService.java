package gov.hhs.onc.dcdt.service.dns;

import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.dns.server.DnsServer;
import java.util.List;
import javax.annotation.Nullable;

public interface DnsService extends ToolService {
    public boolean hasServers();

    @Nullable
    public List<DnsServer> getServers();
}
