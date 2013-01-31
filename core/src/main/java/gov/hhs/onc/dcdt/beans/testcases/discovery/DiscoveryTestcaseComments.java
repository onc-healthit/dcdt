package gov.hhs.onc.dcdt.beans.testcases.discovery;

import gov.hhs.onc.dcdt.annotations.ConfigBean;
import gov.hhs.onc.dcdt.beans.testcases.TestcaseComments;

@ConfigBean("testcases/discoveryTestcase/comments")
public class DiscoveryTestcaseComments extends TestcaseComments
{
	private String additionalInfo;
	private String targetCert;
	private String backgroundCerts;

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

	public String getTargetCert()
	{
		return this.targetCert;
	}

	public void setTargetCert(String targetCert)
	{
		this.targetCert = targetCert;
	}
}