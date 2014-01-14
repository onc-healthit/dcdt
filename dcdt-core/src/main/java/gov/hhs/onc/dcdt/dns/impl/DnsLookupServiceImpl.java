package gov.hhs.onc.dcdt.dns.impl;

import gov.hhs.onc.dcdt.dns.DnsLookupException;
import gov.hhs.onc.dcdt.dns.DnsLookupResultType;
import gov.hhs.onc.dcdt.dns.DnsLookupService;
import gov.hhs.onc.dcdt.dns.DnsServiceProtocol;
import gov.hhs.onc.dcdt.dns.DnsServiceType;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolUriUtils;
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
import org.xbill.DNS.NameTooLongException;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

public class DnsLookupServiceImpl implements DnsLookupService {
    private Cache dnsCache;
    private Resolver dnsResolver;

    @Override
    public List<ARecord> getARecords(String dnsNameStr) throws DnsLookupException {
        return this.getARecords(buildDnsName(dnsNameStr));
    }

    @Override
    public List<ARecord> getARecords(Name dnsName) throws DnsLookupException {
        return this.getRecords(ARecord.class, Type.A, dnsName);
    }

    @Override
    public List<MXRecord> getMxRecords(String dnsNameStr) throws DnsLookupException {
        return this.getMxRecords(buildDnsName(dnsNameStr));
    }

    @Override
    public List<MXRecord> getMxRecords(Name dnsName) throws DnsLookupException {
        return this.getRecords(MXRecord.class, Type.MX, dnsName);
    }

    @Override
    public List<CERTRecord> getCertRecords(String dnsNameStr) throws DnsLookupException {
        return this.getCertRecords(buildDnsName(dnsNameStr));
    }

    @Override
    public List<CERTRecord> getCertRecords(Name dnsName) throws DnsLookupException {
        return this.getRecords(CERTRecord.class, Type.CERT, dnsName);
    }

    @Override
    public List<CNAMERecord> getCnameRecords(String dnsNameStr) throws DnsLookupException {
        return this.getCnameRecords(buildDnsName(dnsNameStr));
    }

    @Override
    public List<CNAMERecord> getCnameRecords(Name dnsName) throws DnsLookupException {
        return this.getRecords(CNAMERecord.class, Type.CNAME, dnsName);
    }

    @Override
    public List<NSRecord> getNsRecords(String dnsNameStr) throws DnsLookupException {
        return this.getNsRecords(buildDnsName(dnsNameStr));
    }

    @Override
    public List<NSRecord> getNsRecords(Name dnsName) throws DnsLookupException {
        return this.getRecords(NSRecord.class, Type.NS, dnsName);
    }

    @Override
    public List<SOARecord> getSoaRecords(String dnsNameStr) throws DnsLookupException {
        return this.getSoaRecords(buildDnsName(dnsNameStr));
    }

    @Override
    public List<SOARecord> getSoaRecords(Name dnsName) throws DnsLookupException {
        return this.getRecords(SOARecord.class, Type.SOA, dnsName);
    }

    @Override
    public List<SRVRecord> getSrvRecords(DnsServiceType dnsServiceType, DnsServiceProtocol dnsServiceProtocol, Name dnsName) throws DnsLookupException {
        return this.getSrvRecords(dnsServiceType, dnsServiceProtocol, dnsName.toString());
    }

    @Override
    public List<SRVRecord> getSrvRecords(DnsServiceType dnsServiceType, DnsServiceProtocol dnsServiceProtocol, String dnsNameStr) throws DnsLookupException {
        return this.getRecords(SRVRecord.class, Type.SRV, buildDnsName(dnsServiceType.toString(), dnsServiceProtocol.toString(), dnsNameStr));
    }

    private static Name buildDnsName(String ... dnsNamePartStrs) throws DnsLookupException {
        Name dnsName = null, dnsNamePart;

        try {
            for (String dnsNamePartStr : dnsNamePartStrs) {
                dnsNamePart = Name.fromString(dnsNamePartStr);
                dnsName = (dnsName != null) ? Name.concatenate(dnsName, dnsNamePart) : dnsNamePart;
            }

            return dnsName;
        } catch (NameTooLongException | TextParseException e) {
            throw new DnsLookupException(String.format("Invalid DNS name: %s", ToolUriUtils.joinDomains(dnsNamePartStrs)), e);
        }
    }

    @SuppressWarnings({ "unchecked" })
    private <T extends Record> List<T> getRecords(Class<T> dnsRecordClass, int dnsRecordType, Name dnsName) throws DnsLookupException {
        Lookup dnsLookup = new Lookup(dnsName, dnsRecordType);

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
                ToolClassUtils.getName(dnsRecordClass), Type.string(dnsRecordType), dnsName, dnsLookupResultType, dnsLookup.getErrorString()));
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
