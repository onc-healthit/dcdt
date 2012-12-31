package gov.hhs.onc.dcdt.utils.beans;

import gov.hhs.onc.dcdt.utils.annotations.ConfigBean;

@ConfigBean("domains/domain")
public class Domain extends UtilityBean
{
	private String id;
	private String name;

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}