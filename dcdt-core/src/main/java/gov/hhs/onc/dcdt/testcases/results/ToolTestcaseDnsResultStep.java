package gov.hhs.onc.dcdt.testcases.results;

import gov.hhs.onc.dcdt.dns.DnsRecordType;

public interface ToolTestcaseDnsResultStep extends ToolTestcaseResultStep {
    public DnsRecordType getDnsRecordType();

    public void setDnsRecordType(DnsRecordType dnsRecordType);
}
