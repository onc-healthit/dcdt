package gov.hhs.onc.dcdt.discovery.steps.dns;

import gov.hhs.onc.dcdt.discovery.steps.LookupStep;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsResultType;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import javax.annotation.Nullable;
import org.apache.commons.collections4.Predicate;
import org.xbill.DNS.Record;

public interface DnsLookupStep<T extends Record> extends LookupStep<T, DnsResultType, DnsLookupResult<T>, DnsLookupService> {
    public Class<T> getRecordClass();

    public boolean hasRecordPredicate();

    @Nullable
    public Predicate<T> getRecordPredicate();

    public DnsRecordType getRecordType();
}
