package gov.hhs.onc.dcdt.beans;

import org.apache.commons.lang3.StringUtils;

public abstract class ToolBean
{
	private String id;

	public boolean hasId()
	{
		return !StringUtils.isBlank(this.getId());
	}
	
	public String getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}
}