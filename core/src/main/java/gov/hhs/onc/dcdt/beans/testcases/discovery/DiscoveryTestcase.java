package gov.hhs.onc.dcdt.beans.testcases.discovery;

import gov.hhs.onc.dcdt.annotations.ConfigBean;
import gov.hhs.onc.dcdt.beans.testcases.Testcase;
import org.apache.commons.lang3.StringUtils;

@ConfigBean("testcases/discoveryTestcase")
public class DiscoveryTestcase extends Testcase<DiscoveryTestcaseComments, DiscoveryTestcaseResult>
{
	private String mailProperty;

	public boolean hasMailProperty()
	{
		return !StringUtils.isBlank(this.mailProperty);
	}
	
	public String getMailProperty()
	{
		return this.mailProperty;
	}

	public void setMailProperty(String mailProperty)
	{
		this.mailProperty = mailProperty;
	}
}