package gov.hhs.onc.dcdt.utils.beans.dns;

import gov.hhs.onc.dcdt.utils.UtilityException;
import gov.hhs.onc.dcdt.utils.annotations.ConfigBean;
import org.nhindirect.config.store.DNSRecord;
import org.nhindirect.config.store.util.DNSRecordUtils;
import org.xbill.DNS.Type;

@ConfigBean("dns/soa")
public class SoaRecord extends DnsRecord
{
	private String nameServer;
	private String hostMaster;
	private int expire;
	private int minimum;
	private int refresh;
	private int serial;
	private int retry;

	public SoaRecord()
	{
		super(Type.SOA);
	}
	
	@Override
	protected DNSRecord toStoreRecord() throws UtilityException
	{
		return DNSRecordUtils.createSOARecord(this.name, this.ttl, this.nameServer, this.hostMaster, 
			this.serial, this.refresh, this.retry, this.expire, this.minimum);
	}
	
	public int getExpire()
	{
		return this.expire;
	}

	public void setExpire(int expire)
	{
		this.expire = expire;
	}

	public String getHostMaster()
	{
		return this.hostMaster;
	}

	public void setHostMaster(String hostMaster)
	{
		this.hostMaster = hostMaster;
	}

	public int getMinimum()
	{
		return this.minimum;
	}

	public void setMinimum(int minimum)
	{
		this.minimum = minimum;
	}

	public String getNameServer()
	{
		return this.nameServer;
	}

	public void setNameServer(String nameServer)
	{
		this.nameServer = nameServer;
	}

	public int getRefresh()
	{
		return this.refresh;
	}

	public void setRefresh(int refresh)
	{
		this.refresh = refresh;
	}

	public int getRetry()
	{
		return this.retry;
	}

	public void setRetry(int retry)
	{
		this.retry = retry;
	}

	public int getSerial()
	{
		return this.serial;
	}

	public void setSerial(int serial)
	{
		this.serial = serial;
	}
}