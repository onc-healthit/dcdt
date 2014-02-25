package gov.hhs.onc.dcdt.testcases.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.dns.DnsLookupService;
import gov.hhs.onc.dcdt.dns.DnsRecordType;

public interface ToolTestcaseDnsResultStep extends ToolTestcaseResultStep {
    @JsonProperty("dnsRecordType")
    public DnsRecordType getDnsRecordType();

    public void setDnsRecordType(DnsRecordType dnsRecordType);

    public DnsLookupService getDnsLookupService();

    public void setDnsLookupService(DnsLookupService dnsLookupService);
}
