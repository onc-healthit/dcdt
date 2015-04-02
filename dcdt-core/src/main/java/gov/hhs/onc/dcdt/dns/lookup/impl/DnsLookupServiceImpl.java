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
import gov.hhs.onc.dcdt.dns.utils.ToolDnsRecordUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Cache;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.PTRRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.TXTRecord;

public class DnsLookupServiceImpl extends AbstractToolBean implements DnsLookupService {
    private Cache cache;
    private Resolver resolver;
    private Set<Name> searchPaths;

    @Override
    public DnsLookupResult<ARecord> lookupARecords(Name name) throws DnsException {
        return this.lookupRecords(DnsRecordType.A, ARecord.class, name);
    }

    @Override
    public DnsLookupResult<CERTRecord> lookupCertRecords(Name name) throws DnsException {
        return this.lookupCertRecords(null, null, name);
    }

    @Override
    public DnsLookupResult<CERTRecord> lookupCertRecords(@Nullable DnsCertificateType certType, @Nullable Set<DnsKeyAlgorithmType> keyAlgTypes, Name name)
        throws DnsException {
        return this.lookupRecords(DnsRecordType.CERT, CERTRecord.class, name, certRecord -> ToolDnsRecordUtils.hasCertRecordParameter(certRecord,
            certType, keyAlgTypes));
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
    public DnsLookupResult<PTRRecord> lookupPtrRecords(Name name) throws DnsException {
        return this.lookupRecords(DnsRecordType.PTR, PTRRecord.class, name);
    }

    @Override
    public DnsLookupResult<SOARecord> lookupSoaRecords(Name name) throws DnsException {
        return this.lookupRecords(DnsRecordType.SOA, SOARecord.class, name);
    }

    @Override
    public DnsLookupResult<SRVRecord> lookupSrvRecords(DnsServiceType serviceType, DnsServiceProtocol serviceProtocol, Name name) throws DnsException {
        return this.lookupRecords(DnsRecordType.SRV, SRVRecord.class,
            ToolDnsNameUtils.fromLabels(serviceType.getNameLabel(), serviceProtocol.getNameLabel(), name));
    }

    @Override
    public DnsLookupResult<TXTRecord> lookupTxtRecords(Name name) throws DnsException {
        return this.lookupRecords(DnsRecordType.TXT, TXTRecord.class, name);
    }

    @Override
    public <T extends Record> DnsLookupResult<T> lookupRecords(DnsRecordType recordType, Class<T> recordClass, Name name) throws DnsException {
        return this.lookupRecords(recordType, recordClass, name, null);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <T extends Record> DnsLookupResult<T>
        lookupRecords(DnsRecordType recordType, Class<T> recordClass, Name name, @Nullable Predicate<T> recordPredicate) throws DnsException {
        Lookup lookup = new Lookup(name, recordType.getCode(), recordType.getDclassType().getCode());

        if (this.hasCache()) {
            lookup.setCache(this.cache);
        }

        if (this.hasResolver()) {
            lookup.setResolver(this.resolver);
        }

        lookup.setSearchPath(ToolCollectionUtils.toArray(this.searchPaths, Name.class));

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

    @Override
    public boolean hasSearchPaths() {
        return !CollectionUtils.isEmpty(this.searchPaths);
    }

    @Nullable
    @Override
    public Set<Name> getSearchPaths() {
        return this.searchPaths;
    }

    @Override
    public void setSearchPaths(@Nullable Set<Name> searchPaths) {
        this.searchPaths = searchPaths;
    }
}
