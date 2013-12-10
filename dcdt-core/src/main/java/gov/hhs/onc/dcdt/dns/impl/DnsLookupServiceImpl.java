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
        try {
            Record[] records = new Lookup(domain, Type.MX).run();
            if (null == records) {
                return null;
            }
            return Arrays.copyOf(records, records.length, MXRecord[].class);
        } catch (TextParseException textParseException) {
            throw new DnsLookupException(textParseException);
        }
    }

    @Override
    public CERTRecord[] getCertRecords(String domain) throws DnsLookupException {
        try {
            Record[] records = new Lookup(domain, Type.CERT).run();
            if (null == records) {
                return null;
            }
            return Arrays.copyOf(records, records.length, CERTRecord[].class);
        } catch (TextParseException textParseException) {
            throw new DnsLookupException(textParseException);
        }
    }

    @Override
    public ARecord[] getARecords(String domain) throws DnsLookupException {
        try {
            Record[] records = new Lookup(domain, Type.A).run();
            if (null == records) {
                return null;
            }
            return Arrays.copyOf(records, records.length, ARecord[].class);
        } catch (TextParseException textParseException) {
            throw new DnsLookupException(textParseException);
        }
    }

    @Override
    public NSRecord[] getNsRecords(String domain) throws DnsLookupException {
        try {
            Record[] records = new Lookup(domain, Type.NS).run();
            if (null == records) {
                return null;
            }
            return Arrays.copyOf(records, records.length, NSRecord[].class);
        } catch (TextParseException textParseException) {
            throw new DnsLookupException(textParseException);
        }
    }

    @Override
    public SRVRecord[] getSrvRecords(ServiceType serviceType, Protocol protocol, String domain) throws DnsLookupException {
        try {
            Record[] records = new Lookup(serviceType.getServiceType() + DOT + protocol.getProtocol() + DOT + domain, Type.SRV).run();
            if (null == records) {
                return null;
            }
            return Arrays.copyOf(records, records.length, SRVRecord[].class);
        } catch (TextParseException textParseException) {
            throw new DnsLookupException(textParseException);
        }
    }

    @Override
    public SOARecord[] getSoaRecords(String domain) throws DnsLookupException {
        try {
            Record[] records = new Lookup(domain, Type.SOA).run();
            if (null == records) {
                return null;
            }
            return Arrays.copyOf(records, records.length, SOARecord[].class);
        } catch (TextParseException textParseException) {
            throw new DnsLookupException(textParseException);
        }
    }

}
