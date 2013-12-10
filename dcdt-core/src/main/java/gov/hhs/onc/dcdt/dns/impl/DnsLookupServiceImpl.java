package gov.hhs.onc.dcdt.dns.impl;



import gov.hhs.onc.dcdt.dns.DnsLookupException;
import gov.hhs.onc.dcdt.dns.DnsLookupService;
import gov.hhs.onc.dcdt.dns.Protocol;
import gov.hhs.onc.dcdt.dns.ServiceType;
import org.springframework.stereotype.Service;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import java.util.Arrays;

@Service
public class DnsLookupServiceImpl implements DnsLookupService {

    private static final String DOT = ".";

    @Override
    public MXRecord[] getMxRecords(String domain) throws DnsLookupException {
        Record[] records = doLookup(null, null, domain, Type.MX);
        if(null != records) {
            return Arrays.copyOf(records, records.length, MXRecord[].class);
        }
        return null;
    }

    @Override
    public CERTRecord[] getCertRecords(String domain) throws DnsLookupException {
        Record[] records = doLookup(null, null, domain, Type.CERT);
        if(null != records) {
            return Arrays.copyOf(records, records.length, CERTRecord[].class);
        }
        return null;
    }

    @Override
    public ARecord[] getARecords(String domain) throws DnsLookupException {
        Record[] records = doLookup(null, null, domain, Type.A);
        if(null != records) {
            return Arrays.copyOf(records, records.length, ARecord[].class);
        }
        return null;
    }

    @Override
    public NSRecord[] getNsRecords(String domain) throws DnsLookupException {
        Record[] records = doLookup(null, null, domain, Type.NS);
        if(null != records) {
            return Arrays.copyOf(records, records.length, NSRecord[].class);
        }
        return null;
    }

    @Override
    public SRVRecord[] getSrvRecords(ServiceType serviceType, Protocol protocol, String domain) throws DnsLookupException {
        Record[] records = doLookup(serviceType, protocol, domain, Type.SRV);
        if(null != records) {
            return Arrays.copyOf(records, records.length, SRVRecord[].class);
        }
        return null;
    }

    @Override
    public SOARecord[] getSoaRecords(String domain) throws DnsLookupException {
        Record[] records = doLookup(null, null, domain, Type.SOA);
        if(null != records) {
            return Arrays.copyOf(records, records.length, SOARecord[].class);
        }
        return null;
    }

    private Record[] doLookup(ServiceType serviceType, Protocol protocol, String domain, int type) throws DnsLookupException {
        try {
            String name = (null != serviceType) ? serviceType.getServiceType() + DOT + protocol.getProtocol() + DOT + domain : domain;
            Record[] records = new Lookup(name, type).run();
            if (null == records) {
                return null;
            }
            return records;
        } catch (TextParseException textParseException) {
            throw new DnsLookupException(textParseException);
        }
    }

}
