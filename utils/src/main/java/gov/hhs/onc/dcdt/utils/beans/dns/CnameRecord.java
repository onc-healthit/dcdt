package gov.hhs.onc.dcdt.utils.beans.dns;

import gov.hhs.onc.dcdt.utils.UtilityException;
import gov.hhs.onc.dcdt.utils.annotations.ConfigBean;
import java.io.IOException;
import org.nhindirect.config.store.DNSRecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Name;
import org.xbill.DNS.Type;

@ConfigBean("dns/cname")
public class CnameRecord extends DnsRecord
{
	private String value;

	public CnameRecord()
	{
		super(Type.CNAME);
	}

	@Override
	protected DNSRecord toStoreRecord() throws UtilityException
	{
		try
		{
			CNAMERecord storeRecord = new CNAMERecord(Name.fromString(getAbsoluteDomainName(this.name)), 
				DClass.IN, this.ttl, Name.fromString(getAbsoluteDomainName(this.value)));
			
			return DNSRecord.fromWire(storeRecord.toWireCanonical());
		}
		catch (IOException e)
		{
			// TODO: finish exception
			throw new UtilityException(e);
		}
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