package gov.hhs.onc.dcdt.dns.lookup;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import java.util.Set;
import javax.annotation.Nullable;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Cache;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.PTRRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.TXTRecord;

public interface DnsLookupService extends ToolBean {
    public DnsLookupResult<ARecord> lookupARecords(Name name);

    public DnsLookupResult<CERTRecord> lookupCertRecords(Name name);

    public DnsLookupResult<CNAMERecord> lookupCnameRecords(Name name);

    public DnsLookupResult<MXRecord> lookupMxRecords(Name name);

    public DnsLookupResult<NSRecord> lookupNsRecords(Name name);

    public DnsLookupResult<PTRRecord> lookupPtrRecords(Name name);

    public DnsLookupResult<SOARecord> lookupSoaRecords(Name name);

    public DnsLookupResult<SRVRecord> lookupSrvRecords(Name name);

    public DnsLookupResult<TXTRecord> lookupTxtRecords(Name name);

    public <T extends Record> DnsLookupResult<T> lookupRecords(DnsRecordType recordType, Class<T> recordClass, Name name);

    public boolean hasCache();

    @Nullable
    public Cache getCache();

    public void setCache(@Nullable Cache cache);

    public boolean hasResolver();

    @Nullable
    public Resolver getResolver();

    public void setResolver(@Nullable Resolver resolver);

    public boolean hasSearchPaths();

    @Nullable
    public Set<Name> getSearchPaths();

    public void setSearchPaths(@Nullable Set<Name> searchPaths);
}
