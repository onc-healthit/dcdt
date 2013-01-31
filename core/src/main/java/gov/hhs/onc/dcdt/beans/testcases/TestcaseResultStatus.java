package gov.hhs.onc.dcdt.beans.testcases;

import java.util.EnumSet;

public enum TestcaseResultStatus
{
	PASS("pass", "passed"), 
	OPTIONAL("opt", "passed (optional requirement)"), 
	FAIL("fail", "failed");
	
	private String name;
	private String nameDisplay;
	
	TestcaseResultStatus(String name, String nameDisplay)
	{
		this.name = name;
		this.nameDisplay = nameDisplay;
	}
	
	public static TestcaseResultStatus forName(String name)
	{
		for (TestcaseResultStatus status : EnumSet.allOf(TestcaseResultStatus.class))
		{
			if (status.getName().equalsIgnoreCase(name))
			{
				return status;
			}
		}
		
		return null;
	}

	public boolean isPass()
	{
		return this.isPass(false);
	}
	
	public boolean isPass(boolean includeOpt)
	{
		return (this == PASS) || (includeOpt && this.isOptional());
	}
	
	public boolean isOptional()
	{
		return this == OPTIONAL;
	}
	
	public boolean isFail()
	{
		return this == FAIL;
	}

	public String getName()
	{
		return name;
	}

	public String getNameDisplay()
	{
		return nameDisplay;
	}
}