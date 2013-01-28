package gov.hhs.onc.dcdt.beans.dns;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.data.DataException;
import org.apache.commons.lang3.StringUtils;
import org.nhindirect.config.store.DNSRecord;
import org.xbill.DNS.Type;

public abstract class DnsRecord extends ToolBean
{
	protected final static String DOMAIN_NAME_SEPARATOR = ".";
	
	protected int type;
	protected String name;
	protected int ttl;

	protected DnsRecord(int type)
	{
		this.type = type;

		Type.check(this.type);
	}
	
	public org.nhind.config.DnsRecord toDataRecord() throws DataException
	{
		DNSRecord storeRecord = this.toStoreRecord();
		
		org.nhind.config.DnsRecord dataRecord = new org.nhind.config.DnsRecord();
		dataRecord.setData(storeRecord.getData());
		dataRecord.setDclass(storeRecord.getDclass());
		dataRecord.setName(storeRecord.getName());
		dataRecord.setTtl(storeRecord.getTtl());
		dataRecord.setType(storeRecord.getType());
		
		return dataRecord;
	}
	
	protected static String getAbsoluteDomainName(String domainName)
	{
		return domainName + (!domainName.endsWith(DOMAIN_NAME_SEPARATOR) ? DOMAIN_NAME_SEPARATOR : StringUtils.EMPTY);
	}
	
	protected abstract DNSRecord toStoreRecord() throws DataException;
	
	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getTtl()
	{
		return this.ttl;
	}

	public void setTtl(int ttl)
	{
		this.ttl = ttl;
	}

	public int getType()
	{
		return this.type;
	}

	public void setType(int type)
	{
		this.type = type;
	}
}