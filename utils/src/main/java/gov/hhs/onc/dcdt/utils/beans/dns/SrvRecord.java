package gov.hhs.onc.dcdt.utils.beans.dns;

import gov.hhs.onc.dcdt.utils.UtilityException;
import gov.hhs.onc.dcdt.annotations.ConfigBean;
import org.nhindirect.config.store.DNSRecord;
import org.nhindirect.config.store.util.DNSRecordUtils;
import org.xbill.DNS.Type;

@ConfigBean("dns/srv")
public class SrvRecord extends DnsRecord
{
	protected final static String NAME_PART_PREFIX = "_";
	
	private String service;
	private String protocol;
	private int priority;
	private int weight;
	private int port;
	private String target;

	public SrvRecord()
	{
		super(Type.SRV);
	}
	
	@Override
	protected DNSRecord toStoreRecord() throws UtilityException
	{
		return DNSRecordUtils.createSRVRecord(this.getFullName(), this.target, this.ttl, 
			this.port, this.priority, this.weight);
	}
	
	public String getFullName()
	{
		return NAME_PART_PREFIX + this.service + DOMAIN_NAME_SEPARATOR + NAME_PART_PREFIX + 
			this.protocol + DOMAIN_NAME_SEPARATOR + this.name;
	}
	
	public int getPort()
	{
		return this.port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public int getPriority()
	{
		return this.priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public String getProtocol()
	{
		return this.protocol;
	}

	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	public String getService()
	{
		return this.service;
	}

	public void setService(String service)
	{
		this.service = service;
	}

	public String getTarget()
	{
		return this.target;
	}

	public void setTarget(String target)
	{
		this.target = target;
	}

	public int getWeight()
	{
		return this.weight;
	}

	public void setWeight(int weight)
	{
		this.weight = weight;
	}
}