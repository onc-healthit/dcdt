package gov.hhs.onc.dcdt.service.dns;

import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServer;

public interface DnsService extends ToolService<DnsServerConfig, DnsServer> {
}
