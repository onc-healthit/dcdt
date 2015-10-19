package gov.hhs.onc.dcdt.dns.lookup.impl;

import gov.hhs.onc.dcdt.beans.ToolMessageLevel;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.impl.ToolMessageImpl;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsResultType;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import java.util.List;
import java.util.Set;
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
    public DnsLookupResult<ARecord> lookupARecords(Name name) {
        return this.lookupRecords(DnsRecordType.A, ARecord.class, name);
    }

    @Override
    public DnsLookupResult<CERTRecord> lookupCertRecords(Name name) {
        return this.lookupRecords(DnsRecordType.CERT, CERTRecord.class, name);
    }

    @Override
    public DnsLookupResult<CNAMERecord> lookupCnameRecords(Name name) {
        return this.lookupRecords(DnsRecordType.CNAME, CNAMERecord.class, name);
    }

    @Override
    public DnsLookupResult<MXRecord> lookupMxRecords(Name name) {
        return this.lookupRecords(DnsRecordType.MX, MXRecord.class, name);
    }

    @Override
    public DnsLookupResult<NSRecord> lookupNsRecords(Name name) {
        return this.lookupRecords(DnsRecordType.NS, NSRecord.class, name);
    }

    @Override
    public DnsLookupResult<PTRRecord> lookupPtrRecords(Name name) {
        return this.lookupRecords(DnsRecordType.PTR, PTRRecord.class, name);
    }

    @Override
    public DnsLookupResult<SOARecord> lookupSoaRecords(Name name) {
        return this.lookupRecords(DnsRecordType.SOA, SOARecord.class, name);
    }

    @Override
    public DnsLookupResult<SRVRecord> lookupSrvRecords(Name name) {
        return this.lookupRecords(DnsRecordType.SRV, SRVRecord.class, name);
    }

    @Override
    public DnsLookupResult<TXTRecord> lookupTxtRecords(Name name) {
        return this.lookupRecords(DnsRecordType.TXT, TXTRecord.class, name);
    }

    @Override
    public <T extends Record> DnsLookupResult<T> lookupRecords(DnsRecordType recordType, Class<T> recordClass, Name name) {
        DnsLookupResult<T> result;

        try {
            Lookup lookup = new Lookup(name, recordType.getCode(), recordType.getDclassType().getCode());

            if (this.hasCache()) {
                lookup.setCache(this.cache);
            }

            if (this.hasResolver()) {
                lookup.setResolver(this.resolver);
            }

            lookup.setSearchPath(ToolCollectionUtils.toArray(this.searchPaths, Name.class));

            List<Record> rawAnswers = ToolArrayUtils.asList(lookup.run());
            DnsResultType resultType = ToolEnumUtils.findByCode(DnsResultType.class, lookup.getResult());

            // noinspection ConstantConditions
            (result = new DnsLookupResultImpl<>(recordType, recordClass, name, resultType, ToolArrayUtils.asList(lookup.getAliases()), rawAnswers))
                .getMessages().add(new ToolMessageImpl((resultType.isSuccess() ? ToolMessageLevel.INFO : ToolMessageLevel.ERROR), lookup.getErrorString()));
        } catch (Exception e) {
            (result = new DnsLookupResultImpl<>(recordType, recordClass, name, DnsResultType.UNRECOVERABLE)).getMessages().add(
                new ToolMessageImpl(ToolMessageLevel.ERROR, e.getMessage()));
        }

        return result;
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
