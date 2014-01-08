package gov.hhs.onc.dcdt.dns;

import org.xbill.DNS.ARecord;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.SRVRecord;

public interface DnsLookupService {

    public MXRecord[] getMxRecords(boolean resolveLocally, String dnsName) throws DnsLookupException;

    public CERTRecord[] getCertRecords(boolean resolveLocally, String dnsName) throws DnsLookupException;

    public ARecord[] getARecords(boolean resolveLocally, String dnsName) throws DnsLookupException;

    public NSRecord[] getNsRecords(boolean resolveLocally, String dnsName) throws DnsLookupException;

    public SRVRecord[] getSrvRecords(boolean resolveLocally, ServiceType serviceType, ServiceProtocol serviceProtocol, String dnsName) throws DnsLookupException;

    public SOARecord[] getSoaRecords(boolean resolveLocally, String dnsName) throws DnsLookupException;

    public CNAMERecord[] getCNameRecords(boolean resolveLocally, String dnsName) throws DnsLookupException;

}
