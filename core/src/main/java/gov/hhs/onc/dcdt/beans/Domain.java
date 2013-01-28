package gov.hhs.onc.dcdt.beans;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.annotations.ConfigBean;

@ConfigBean("domains/domain")
public class Domain extends ToolBean
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