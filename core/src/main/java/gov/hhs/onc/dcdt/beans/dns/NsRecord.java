package gov.hhs.onc.dcdt.beans.dns;

import gov.hhs.onc.dcdt.annotations.ConfigBean;
import gov.hhs.onc.dcdt.data.DataException;
import java.io.IOException;
import org.nhindirect.config.store.DNSRecord;
import org.xbill.DNS.DClass;
import org.xbill.DNS.NSRecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.Type;

@ConfigBean("dns/ns")
public class NsRecord extends DnsRecord
{
	private String value;

	public NsRecord()
	{
		super(Type.NS);
	}

	@Override
	protected DNSRecord toStoreRecord() throws DataException
	{
		try
		{
			NSRecord storeRecord = new NSRecord(Name.fromString(getAbsoluteDomainName(this.name)), 
				DClass.IN, this.ttl, Name.fromString(getAbsoluteDomainName(this.value)));
			
			return DNSRecord.fromWire(storeRecord.toWireCanonical());
		}
		catch (IOException e)
		{
			// TODO: finish exception
			throw new DataException(e);
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