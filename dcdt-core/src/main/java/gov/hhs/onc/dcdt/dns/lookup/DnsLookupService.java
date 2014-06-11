package gov.hhs.onc.dcdt.dns.lookup;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.dns.DnsCertificateType;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsKeyAlgorithmType;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsServiceProtocol;
import gov.hhs.onc.dcdt.dns.DnsServiceType;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.collections4.Predicate;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Cache;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;

public interface DnsLookupService extends ToolBean {
    public DnsLookupResult<ARecord> lookupARecords(Name name) throws DnsException;

    public DnsLookupResult<CERTRecord> lookupCertRecords(Name name) throws DnsException;

    public DnsLookupResult<CERTRecord> lookupCertRecords(@Nullable DnsKeyAlgorithmType keyAlgType, @Nullable DnsCertificateType certType, Name name)
        throws DnsException;

    public DnsLookupResult<CNAMERecord> lookupCnameRecords(Name name) throws DnsException;

    public DnsLookupResult<MXRecord> lookupMxRecords(Name name) throws DnsException;

    public DnsLookupResult<NSRecord> lookupNsRecords(Name name) throws DnsException;

    public DnsLookupResult<SOARecord> lookupSoaRecords(Name name) throws DnsException;

    public DnsLookupResult<SRVRecord> lookupSrvRecords(DnsServiceType serviceType, DnsServiceProtocol serviceProtocol, Name name) throws DnsException;

    public <T extends Record> DnsLookupResult<T> lookupRecords(DnsRecordType recordType, Class<T> recordClass, Name name) throws DnsException;

    public <T extends Record> DnsLookupResult<T>
        lookupRecords(DnsRecordType recordType, Class<T> recordClass, Name name, @Nullable Predicate<T> recordPredicate) throws DnsException;

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
