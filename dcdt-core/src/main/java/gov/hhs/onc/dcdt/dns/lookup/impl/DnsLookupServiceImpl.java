package gov.hhs.onc.dcdt.dns.lookup.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.dns.DnsCertificateType;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsKeyAlgorithmType;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsServiceProtocol;
import gov.hhs.onc.dcdt.dns.DnsServiceType;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsRecordUtils.CertRecordParameterPredicate;
import javax.annotation.Nullable;
import org.apache.commons.collections4.Predicate;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Cache;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;

public class DnsLookupServiceImpl extends AbstractToolBean implements DnsLookupService {
    private Cache cache;
    private Resolver resolver;

    @Override
    public DnsLookupResult<ARecord> lookupARecords(Name name) throws DnsException {
        return this.lookupRecords(DnsRecordType.A, ARecord.class, name);
    }

    @Override
    public DnsLookupResult<CERTRecord> lookupCertRecords(Name name) throws DnsException {
        return this.lookupCertRecords(null, null, name);
    }

    @Override
    public DnsLookupResult<CERTRecord> lookupCertRecords(@Nullable DnsKeyAlgorithmType keyAlgType, @Nullable DnsCertificateType certType, Name name)
        throws DnsException {
        return this.lookupRecords(DnsRecordType.CERT, CERTRecord.class, name, new CertRecordParameterPredicate(keyAlgType, certType));
    }

    @Override
    public DnsLookupResult<CNAMERecord> lookupCnameRecords(Name name) throws DnsException {
        return this.lookupRecords(DnsRecordType.CNAME, CNAMERecord.class, name);
    }

    @Override
    public DnsLookupResult<MXRecord> lookupMxRecords(Name name) throws DnsException {
        return this.lookupRecords(DnsRecordType.MX, MXRecord.class, name);
    }

    @Override
    public DnsLookupResult<NSRecord> lookupNsRecords(Name name) throws DnsException {
        return this.lookupRecords(DnsRecordType.NS, NSRecord.class, name);
    }

    @Override
    public DnsLookupResult<SOARecord> lookupSoaRecords(Name name) throws DnsException {
        return this.lookupRecords(DnsRecordType.SOA, SOARecord.class, name);
    }

    @Override
    public DnsLookupResult<SRVRecord> lookupSrvRecords(DnsServiceType serviceType, DnsServiceProtocol serviceProtocol, Name name) throws DnsException {
        return this.lookupRecords(DnsRecordType.SRV, SRVRecord.class,
            ToolDnsNameUtils.fromLabels(serviceType.getDnsNameLabel(), serviceProtocol.getDnsNameLabel(), name));
    }

    @Override
    public <T extends Record> DnsLookupResult<T> lookupRecords(DnsRecordType recordType, Class<T> recordClass, Name name) throws DnsException {
        return this.lookupRecords(recordType, recordClass, name, null);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <T extends Record> DnsLookupResult<T>
        lookupRecords(DnsRecordType recordType, Class<T> recordClass, Name name, @Nullable Predicate<T> recordPredicate) throws DnsException {
        Lookup lookup = new Lookup(name, recordType.getType(), recordType.getDclassType().getType());

        if (this.hasCache()) {
            lookup.setCache(this.cache);
        }

        if (this.hasResolver()) {
            lookup.setResolver(this.resolver);
        }

        lookup.run();

        return new DnsLookupResultImpl<>(recordType, recordClass, name, lookup, recordPredicate);
    }

    @Override
    public boolean hasCache() {
        return this.cache != null;
    }

    @Nullable
    @Override
    public Cache getCache() {
        return this.cache;
    }

    @Override
    public void setCache(@Nullable Cache cache) {
        this.cache = cache;
    }

    @Override
    public boolean hasResolver() {
        return this.resolver != null;
    }

    @Nullable
    @Override
    public Resolver getResolver() {
        return this.resolver;
    }

    @Override
    public void setResolver(@Nullable Resolver resolver) {
        this.resolver = resolver;
    }
}
