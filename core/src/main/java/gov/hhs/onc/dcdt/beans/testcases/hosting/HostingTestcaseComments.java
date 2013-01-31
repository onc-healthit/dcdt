package gov.hhs.onc.dcdt.beans.testcases.hosting;

import gov.hhs.onc.dcdt.annotations.ConfigBean;
import gov.hhs.onc.dcdt.beans.testcases.TestcaseComments;

@ConfigBean("testcases/hostingTestcase/comments")
public class HostingTestcaseComments extends TestcaseComments
{
	private String instructions;

	public String getInstructions()
	{
		return this.instructions;
	}

	public void setInstructions(String instructions)
	{
		this.instructions = instructions;
	}
}