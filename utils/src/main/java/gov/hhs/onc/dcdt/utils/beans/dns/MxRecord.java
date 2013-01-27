package gov.hhs.onc.dcdt.utils.beans.dns;

import gov.hhs.onc.dcdt.utils.UtilityException;
import gov.hhs.onc.dcdt.annotations.ConfigBean;
import org.nhindirect.config.store.DNSRecord;
import org.nhindirect.config.store.util.DNSRecordUtils;
import org.xbill.DNS.Type;

@ConfigBean("dns/mx")
public class MxRecord extends DnsRecord
{
	private int priority;
	private String value;

	public MxRecord()
	{
		super(Type.MX);
	}

	@Override
	protected DNSRecord toStoreRecord() throws UtilityException
	{
		return DNSRecordUtils.createMXRecord(this.name, this.value, this.ttl, this.priority);
	}

	public int getPriority()
	{
		return this.priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public String getValue()
	{
		return this.value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}