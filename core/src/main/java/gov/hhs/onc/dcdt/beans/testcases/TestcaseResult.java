package gov.hhs.onc.dcdt.beans.testcases;

import gov.hhs.onc.dcdt.beans.ToolBean;
import org.apache.commons.lang3.StringUtils;

public abstract class TestcaseResult extends ToolBean
{
	protected TestcaseResultStatus status;
	protected String msg;
	
	public String getStatusName()
	{
		return this.hasStatus() ? this.status.getName() : null;
	}
	
	public void setStatusName(String statusName)
	{
		this.status = TestcaseResultStatus.forName(statusName);
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