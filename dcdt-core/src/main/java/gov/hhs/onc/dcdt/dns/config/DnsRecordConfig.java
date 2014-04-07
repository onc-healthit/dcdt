package gov.hhs.onc.dcdt.dns.config;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public interface DnsRecordConfig<T extends Record> extends ToolBean {
    public T toRecord() throws DnsException;

    public Name getName();

    public void setName(Name name);

    public long getTtl();

    public void setTtl(long ttl);

    public DnsRecordType getRecordType();
}
