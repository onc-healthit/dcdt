package gov.hhs.onc.dcdt.dns.lookup;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import javax.annotation.Nullable;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public interface DnsNameService extends ToolBean {
    public InetAddress getByName(String host) throws UnknownHostException;

    public InetAddress[] getAllByName(String host) throws UnknownHostException;

    public InetAddress[] getAllByName(String host, int limit) throws UnknownHostException;

    @Nullable
    public <T extends Record> List<T> lookupRecords(DnsRecordType recordType, Class<T> recordClass, String hostNameStr, int limit) throws UnknownHostException;

    @Nullable
    public <T extends Record> List<T> lookupRecords(DnsRecordType recordType, Class<T> recordClass, Name hostName, int limit);

    public DnsLookupService[] getLookupServices();

    public void setLookupServices(DnsLookupService ... lookupServices);
}
