package gov.hhs.onc.dcdt.utils.beans;

import gov.hhs.onc.dcdt.utils.annotations.ConfigBean;

@ConfigBean("ldaps/ldap")
public class LdapService extends UtilityBean
{
	private String id;
	private String name;
	private String host;
	private int port;
	private boolean real;

	public boolean isReal()
	{
		return this.real;
	}

	public void setIsReal(boolean real)
	{
		this.real = real;
	}

	public String getHost()
	{
		return this.host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getPort()
	{
		return this.port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}
}