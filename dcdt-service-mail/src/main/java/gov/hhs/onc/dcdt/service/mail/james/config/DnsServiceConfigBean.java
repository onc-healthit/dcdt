package gov.hhs.onc.dcdt.service.mail.james.config;

import gov.hhs.onc.dcdt.config.ConfigurationNode;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;

@ConfigurationNode(name = "dnsservice")
public interface DnsServiceConfigBean extends JamesConfigBean {
    public DnsLookupService getExternalLookupService();

    public void setExternalLookupService(DnsLookupService extLookupService);

    public DnsLookupService getLocalLookupService();

    public void setLocalLookupService(DnsLookupService localLookupService);
}
