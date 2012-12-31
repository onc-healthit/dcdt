package gov.hhs.onc.dcdt.utils.beans.dns;

import gov.hhs.onc.dcdt.utils.beans.UtilityBean;

public abstract class DnsRecord extends UtilityBean
{
	private String name;
	private int ttl;

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
}