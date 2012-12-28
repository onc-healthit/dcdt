package gov.hhs.onc.dcdt.utils.beans.dns;

import gov.hhs.onc.dcdt.utils.annotations.ConfigBean;

@ConfigBean("dns/mx")
public class MxRecord extends DnsRecord
{
	private int priority;
	private String value;

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