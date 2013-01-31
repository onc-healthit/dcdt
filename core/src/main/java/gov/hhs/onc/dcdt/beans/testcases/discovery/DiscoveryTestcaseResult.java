package gov.hhs.onc.dcdt.beans.testcases.discovery;

import gov.hhs.onc.dcdt.annotations.ConfigBean;
import gov.hhs.onc.dcdt.beans.testcases.TestcaseResult;
import org.apache.commons.lang3.StringUtils;

@ConfigBean("testcases/discoveryTestcase/discoveryTestcaseResult")
public class DiscoveryTestcaseResult extends TestcaseResult
{
	private String entryProperty;
	
	public boolean hasEntryProperty()
	{
		return !StringUtils.isBlank(this.entryProperty);
	}
	
	public String getEntryProperty()
	{
		return this.entryProperty;
	}

	public void setEntryProperty(String entryProperty)
	{
		this.entryProperty = entryProperty;
	}
}