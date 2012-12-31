package gov.hhs.onc.dcdt.utils.beans;

import gov.hhs.onc.dcdt.utils.annotations.ConfigBean;

@ConfigBean("settings/setting")
public class Setting extends UtilityBean
{
	private String id;
	private String name;
	private String value;

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
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