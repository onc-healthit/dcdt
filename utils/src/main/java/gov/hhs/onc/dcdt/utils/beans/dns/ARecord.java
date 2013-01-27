package gov.hhs.onc.dcdt.utils.beans.dns;

import gov.hhs.onc.dcdt.utils.UtilityException;
import gov.hhs.onc.dcdt.annotations.ConfigBean;
import org.nhindirect.config.store.DNSRecord;
import org.nhindirect.config.store.util.DNSRecordUtils;
import org.xbill.DNS.Type;

@ConfigBean("dns/a")
public class ARecord extends DnsRecord
{
	private String value;

	public ARecord()
	{
		super(Type.A);
	}

	@Override
	protected DNSRecord toStoreRecord() throws UtilityException
	{
		return DNSRecordUtils.createARecord(this.name, this.ttl, this.value);
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