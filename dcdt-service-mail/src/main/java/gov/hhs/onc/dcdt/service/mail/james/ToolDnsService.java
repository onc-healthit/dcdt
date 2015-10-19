package gov.hhs.onc.dcdt.service.mail.james;

import gov.hhs.onc.dcdt.dns.lookup.DnsNameService;
import org.apache.james.dnsservice.api.DNSService;

public interface ToolDnsService extends DnsNameService, DNSService {
}
