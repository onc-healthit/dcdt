package gov.hhs.onc.dcdt.dns.impl;

import gov.hhs.onc.dcdt.dns.DnsLookupException;
import gov.hhs.onc.dcdt.dns.DnsLookupService;
import gov.hhs.onc.dcdt.dns.ServiceProtocol;
import gov.hhs.onc.dcdt.dns.ServiceType;
import gov.hhs.onc.dcdt.utils.ToolUriUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Cache;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

@Service
@Component("dnsLookupServiceImpl")
public class DnsLookupServiceImpl implements DnsLookupService {

    @Autowired
    private Cache dnsCache;

    @Autowired
    private List<Resolver> dnsResolverHostsLocal;

    @Autowired
    private List<Resolver> dnsResolverHostsExternal;

    @Override
    public MXRecord[] getMxRecords(boolean resolveLocally, String dnsName) throws DnsLookupException {
        Record[] records = doLookup(resolveLocally, null, null, dnsName, Type.MX);
        if (null != records) {
            return Arrays.copyOf(records, records.length, MXRecord[].class);
        }
        return null;
    }

    @Override
    public CERTRecord[] getCertRecords(boolean resolveLocally, String dnsName) throws DnsLookupException {
        Record[] records = doLookup(resolveLocally, null, null, dnsName, Type.CERT);
        if (null != records) {
            return Arrays.copyOf(records, records.length, CERTRecord[].class);
        }
        return null;
    }

    @Override
    public ARecord[] getARecords(boolean resolveLocally, String dnsName) throws DnsLookupException {
        Record[] records = doLookup(resolveLocally, null, null, dnsName, Type.A);
        if (null != records) {
            return Arrays.copyOf(records, records.length, ARecord[].class);
        }
        return null;
    }

    @Override
    public NSRecord[] getNsRecords(boolean resolveLocally, String dnsName) throws DnsLookupException {
        Record[] records = doLookup(resolveLocally, null, null, dnsName, Type.NS);
        if (null != records) {
            return Arrays.copyOf(records, records.length, NSRecord[].class);
        }
        return null;
    }

    @Override
    public SRVRecord[] getSrvRecords(boolean resolveLocally, ServiceType serviceType, ServiceProtocol serviceProtocol, String dnsName)
        throws DnsLookupException {
        Record[] records = doLookup(resolveLocally, serviceType, serviceProtocol, dnsName, Type.SRV);
        if (null != records) {
            return Arrays.copyOf(records, records.length, SRVRecord[].class);
        }
        return null;
    }

    @Override
    public SOARecord[] getSoaRecords(boolean resolveLocally, String dnsName) throws DnsLookupException {
        Record[] records = doLookup(resolveLocally, null, null, dnsName, Type.SOA);
        if (null != records) {
            return Arrays.copyOf(records, records.length, SOARecord[].class);
        }
        return null;
    }

    @Override
    public CNAMERecord[] getCNameRecords(boolean resolveLocally, String dnsName) throws DnsLookupException {
        Record[] records = doLookup(resolveLocally, null, null, dnsName, Type.CNAME);
        if (null != records) {
            return Arrays.copyOf(records, records.length, CNAMERecord[].class);
        }
        return null;
    }

    private Record[] doLookup(boolean resolveLocally, ServiceType serviceType, ServiceProtocol serviceProtocol, String dnsName, int type)
        throws DnsLookupException {
        try {
            String name = (null != serviceType) ? serviceType.getServiceType() + ToolUriUtils.DOMAIN_DELIM + serviceProtocol.getServiceProtocol()
                + ToolUriUtils.DOMAIN_DELIM + dnsName : dnsName;
            Lookup lookup = new Lookup(name, type);
            if (resolveLocally) {
                lookup.setResolver(new ExtendedResolver(this.dnsResolverHostsLocal.toArray(new Resolver[this.dnsResolverHostsLocal.size()])));
            } else {
                // there is some bug with external resolvers and SOA records...working around it
                if(Type.SOA != type) {
                    lookup.setResolver(new ExtendedResolver(this.dnsResolverHostsExternal.toArray(new Resolver[this.dnsResolverHostsExternal.size()])));
                }
            }
            lookup.setCache(this.dnsCache);
            Record[] records = lookup.run();
            if (null == records) {
                return null;
            }
            return records;
        } catch (TextParseException | UnknownHostException exception) {
            throw new DnsLookupException(exception);
        }
    }

}
