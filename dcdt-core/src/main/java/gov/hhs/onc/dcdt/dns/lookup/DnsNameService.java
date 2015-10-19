package gov.hhs.onc.dcdt.dns.lookup;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.net.InetAddress;
import java.net.UnknownHostException;

public interface DnsNameService extends ToolBean {
    public InetAddress getByName(String host) throws UnknownHostException;

    public InetAddress[] getAllByName(String host) throws UnknownHostException;

    public InetAddress[] getAllByName(String host, int limit) throws UnknownHostException;

    public DnsLookupService[] getLookupServices();

    public void setLookupServices(DnsLookupService ... lookupServices);
}
