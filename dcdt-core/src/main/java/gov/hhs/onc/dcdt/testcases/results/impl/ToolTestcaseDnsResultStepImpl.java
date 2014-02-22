package gov.hhs.onc.dcdt.testcases.results.impl;

import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseDnsResultStep;

public class ToolTestcaseDnsResultStepImpl extends AbstractToolTestcaseResultStep implements ToolTestcaseDnsResultStep {
    private DnsRecordType dnsRecordType;

    @Override
    public DnsRecordType getDnsRecordType() {
        return this.dnsRecordType;
    }

    @Override
    public void setDnsRecordType(DnsRecordType dnsRecordType) {
        this.dnsRecordType = dnsRecordType;
    }
}
