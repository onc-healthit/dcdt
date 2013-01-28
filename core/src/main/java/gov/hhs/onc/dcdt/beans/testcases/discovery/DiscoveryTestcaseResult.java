package gov.hhs.onc.dcdt.beans.testcases.discovery;

import gov.hhs.onc.dcdt.annotations.ConfigBean;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.testcases.TestcaseResultStatus;
import org.apache.commons.lang3.StringUtils;

@ConfigBean("testcases/discoveryTestcase/discoveryTestcaseResult")
public class DiscoveryTestcaseResult extends ToolBean
{
	private String entryProperty;
	private TestcaseResultStatus status;
	private String msg;

	public String getStatusName()
	{
		return this.hasStatus() ? this.status.getName() : null;
	}
	
	public void setStatusName(String statusName)
	{
		this.status = TestcaseResultStatus.forName(statusName);
	}
	
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
	
	public boolean hasMsg()
	{
		return !StringUtils.isBlank(this.msg);
	}

	public String getMsg()
	{
		return this.msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public boolean hasStatus()
	{
		return this.status != null;
	}
	
	public TestcaseResultStatus getStatus()
	{
		return this.status;
	}

	public void setStatus(TestcaseResultStatus status)
	{
		this.status = status;
	}
}