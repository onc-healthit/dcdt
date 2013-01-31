package gov.hhs.onc.dcdt.beans.testcases;

import gov.hhs.onc.dcdt.beans.ToolBean;

public abstract class TestcaseComments extends ToolBean
{
	protected String shortDescription;
	protected String description;
	protected String rtm;
	protected String specifications;
	
	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getRtm()
	{
		return this.rtm;
	}

	public void setRtm(String rtm)
	{
		this.rtm = rtm;
	}

	public String getShortDescription()
	{
		return this.shortDescription;
	}

	public void setShortDescription(String shortDescription)
	{
		this.shortDescription = shortDescription;
	}

	public String getSpecifications()
	{
		return this.specifications;
	}

	public void setSpecifications(String specifications)
	{
		this.specifications = specifications;
	}
}