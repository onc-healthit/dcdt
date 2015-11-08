package gov.hhs.onc.dcdt.service.dns.wrapper;

import gov.hhs.onc.dcdt.service.dns.DnsService;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServer;
import gov.hhs.onc.dcdt.service.wrapper.ToolServiceWrapper;

public interface DnsServiceWrapper extends ToolServiceWrapper<DnsServerConfig, DnsServer, DnsService> {
}
