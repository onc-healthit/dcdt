package gov.hhs.onc.dcdt.discovery.steps.dns;

import gov.hhs.onc.dcdt.discovery.steps.LookupStep;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import org.xbill.DNS.Record;

public interface DnsLookupStep<T extends Record> extends LookupStep<DnsLookupResult<T>, DnsLookupService> {
    public Class<T> getRecordClass();

    public DnsRecordType getRecordType();
}
