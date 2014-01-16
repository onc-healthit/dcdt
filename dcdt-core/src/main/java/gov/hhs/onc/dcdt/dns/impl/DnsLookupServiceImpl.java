package gov.hhs.onc.dcdt.dns.impl;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsLookupException;
import gov.hhs.onc.dcdt.dns.DnsLookupResultType;
import gov.hhs.onc.dcdt.dns.DnsLookupService;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.DnsServiceProtocol;
import gov.hhs.onc.dcdt.dns.DnsServiceType;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.List;
import javax.annotation.Nullable;
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

public class DnsLookupServiceImpl implements DnsLookupService {
    private Cache dnsCache;
    private Resolver dnsResolver;

    @Override
    public List<ARecord> getARecords(Name dnsName) throws DnsException {
        return this.getRecords(ARecord.class, DnsRecordType.A, dnsName);
    }

    @Override
    public List<MXRecord> getMxRecords(Name dnsName) throws DnsException {
        return this.getRecords(MXRecord.class, DnsRecordType.MX, dnsName);
    }

    @Override
    public List<CERTRecord> getCertRecords(Name dnsName) throws DnsException {
        return this.getRecords(CERTRecord.class, DnsRecordType.CERT, dnsName);
    }

    @Override
    public List<CNAMERecord> getCnameRecords(Name dnsName) throws DnsException {
        return this.getRecords(CNAMERecord.class, DnsRecordType.CNAME, dnsName);
    }

    @Override
    public List<NSRecord> getNsRecords(Name dnsName) throws DnsException {
        return this.getRecords(NSRecord.class, DnsRecordType.NS, dnsName);
    }

    @Override
    public List<SOARecord> getSoaRecords(Name dnsName) throws DnsException {
        return this.getRecords(SOARecord.class, DnsRecordType.SOA, dnsName);
    }

    @Override
    public List<SRVRecord> getSrvRecords(DnsServiceType dnsServiceType, DnsServiceProtocol dnsServiceProtocol, Name dnsName) throws DnsException {
        return this.getRecords(SRVRecord.class, DnsRecordType.SRV,
            ToolDnsNameUtils.fromLabels(dnsServiceType.getDnsNameLabel(), dnsServiceProtocol.getDnsNameLabel(), dnsName));
    }

    @SuppressWarnings({ "unchecked" })
    private <T extends Record> List<T> getRecords(Class<T> dnsRecordClass, DnsRecordType dnsRecordType, Name dnsName) throws DnsException {
        Lookup dnsLookup = new Lookup(dnsName, dnsRecordType.getType());

        if (this.hasDnsCache()) {
            dnsLookup.setCache(this.dnsCache);
        }

        if (this.hasDnsResolver()) {
            dnsLookup.setResolver(this.dnsResolver);
        }

        Record[] dnsLookupResults = dnsLookup.run();
        DnsLookupResultType dnsLookupResultType = DnsLookupResultType.findByResult(dnsLookup.getResult());

        if (dnsLookupResultType != DnsLookupResultType.SUCCESSFUL) {
            throw new DnsLookupException(String.format("DNS lookup (recordClass=%s, recordType=%s, name=%s) failed (result=%s): %s",
                ToolClassUtils.getName(dnsRecordClass), dnsRecordType.getTypeDisplay(), dnsName, dnsLookupResultType, dnsLookup.getErrorString()));
        }

        return ToolArrayUtils.asList((T[]) dnsLookupResults);
    }

    @Override
    public boolean hasDnsCache() {
        return this.dnsCache != null;
    }

    @Nullable
    @Override
    public Cache getDnsCache() {
        return this.dnsCache;
    }

    @Override
    public void setDnsCache(@Nullable Cache dnsCache) {
        this.dnsCache = dnsCache;
    }

    @Override
    public boolean hasDnsResolver() {
        return this.dnsResolver != null;
    }

    @Nullable
    @Override
    public Resolver getDnsResolver() {
        return this.dnsResolver;
    }

    @Override
    public void setDnsResolver(@Nullable Resolver dnsResolver) {
        this.dnsResolver = dnsResolver;
    }
}
