package gov.hhs.onc.dcdt.utils.beans.dns;

import gov.hhs.onc.dcdt.utils.annotations.ConfigBean;

@ConfigBean("dns/srv")
public class SrvRecord extends DnsRecord
{
	private String service;
	private String protocol;
	private int priority;
	private int weight;
	private int port;
	private String target;

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