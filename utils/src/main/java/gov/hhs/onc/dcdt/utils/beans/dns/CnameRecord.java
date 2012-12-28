package gov.hhs.onc.dcdt.utils.beans.dns;

import gov.hhs.onc.dcdt.utils.annotations.ConfigBean;

@ConfigBean("dns/cname")
public class CnameRecord extends DnsRecord
{
	private String value;

	public String getValue()
	{
		return this.value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}