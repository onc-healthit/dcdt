package gov.hhs.onc.dcdt.discovery.steps.dns;

import gov.hhs.onc.dcdt.discovery.steps.CertificateLookupStep;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import org.xbill.DNS.CERTRecord;

public interface DnsCertRecordLookupStep extends CertificateLookupStep<DnsLookupResult<CERTRecord>, DnsLookupService>, DnsLookupStep<CERTRecord> {
}
