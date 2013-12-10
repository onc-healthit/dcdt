package gov.hhs.onc.dcdt.dns;

import org.xbill.DNS.ARecord;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;

public interface DnsLookupService {

    public MXRecord[] getMxRecords(String domain) throws DnsLookupException;

    public CERTRecord[] getCertRecords(String domain) throws DnsLookupException;

    public ARecord[] getARecords(String domain) throws DnsLookupException;

    public NSRecord[] getNsRecords(String domain) throws DnsLookupException;

    public SRVRecord[] getSrvRecords(ServiceType serviceType, Protocol protocol, String domain) throws DnsLookupException;

    public SOARecord[] getSoaRecords(String domain) throws DnsLookupException;

}
