package gov.hhs.onc.dcdt.dns;

import java.util.List;
import javax.annotation.Nullable;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Cache;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;

public interface DnsLookupService {
    public List<ARecord> getARecords(String dnsNameStr) throws DnsLookupException;

    public List<ARecord> getARecords(Name dnsName) throws DnsLookupException;

    public List<MXRecord> getMxRecords(String dnsNameStr) throws DnsLookupException;

    public List<MXRecord> getMxRecords(Name dnsName) throws DnsLookupException;

    public List<CERTRecord> getCertRecords(String dnsNameStr) throws DnsLookupException;

    public List<CERTRecord> getCertRecords(Name dnsName) throws DnsLookupException;

    public List<CNAMERecord> getCnameRecords(String dnsNameStr) throws DnsLookupException;

    public List<CNAMERecord> getCnameRecords(Name dnsName) throws DnsLookupException;

    public List<NSRecord> getNsRecords(String dnsNameStr) throws DnsLookupException;

    public List<NSRecord> getNsRecords(Name dnsName) throws DnsLookupException;

    public List<SOARecord> getSoaRecords(String dnsNameStr) throws DnsLookupException;

    public List<SOARecord> getSoaRecords(Name dnsName) throws DnsLookupException;

    public List<SRVRecord> getSrvRecords(DnsServiceType dnsServiceType, DnsServiceProtocol dnsServiceProtocol, Name dnsName) throws DnsLookupException;

    public List<SRVRecord> getSrvRecords(DnsServiceType dnsServiceType, DnsServiceProtocol dnsServiceProtocol, String dnsNameStr) throws DnsLookupException;

    public boolean hasDnsCache();

    @Nullable
    public Cache getDnsCache();

    public void setDnsCache(@Nullable Cache dnsCache);

    public boolean hasDnsResolver();

    @Nullable
    public Resolver getDnsResolver();

    public void setDnsResolver(@Nullable Resolver dnsResolver);
}
