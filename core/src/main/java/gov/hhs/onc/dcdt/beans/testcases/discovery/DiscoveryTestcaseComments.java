package gov.hhs.onc.dcdt.beans.testcases.discovery;

import gov.hhs.onc.dcdt.annotations.ConfigBean;
import gov.hhs.onc.dcdt.beans.ToolBean;

@ConfigBean("testcases/discoveryTestcase/comments")
public class DiscoveryTestcaseComments extends ToolBean
{
	private String shortDescription;
	private String description;
	private String additionalInfo;
	private String targetCert;
	private String backgroundCerts;
	private String rtm;
	private String specifications;

	public String getAdditionalInfo()
	{
		return this.additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo)
	{
		this.additionalInfo = additionalInfo;
	}

	public String getBackgroundCerts()
	{
		return this.backgroundCerts;
	}

	public void setBackgroundCerts(String backgroundCerts)
	{
		this.backgroundCerts = backgroundCerts;
	}

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

	public String getTargetCert()
	{
		return this.targetCert;
	}

	public void setTargetCert(String targetCert)
	{
		this.targetCert = targetCert;
	}
}