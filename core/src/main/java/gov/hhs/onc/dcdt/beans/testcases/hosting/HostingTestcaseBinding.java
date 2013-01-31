package gov.hhs.onc.dcdt.beans.testcases.hosting;

import java.util.EnumSet;

public enum HostingTestcaseBinding
{
	ADDRESS("address"), DOMAIN("domain"), BOTH("both");
	
	private String name;
	
	HostingTestcaseBinding(String name)
	{
		this.name = name;
	}

	public static HostingTestcaseBinding fromName(String name)
	{
		for (HostingTestcaseBinding binding : EnumSet.allOf(HostingTestcaseBinding.class))
		{
			if (binding.getName().equalsIgnoreCase(name))
			{
				return binding;
			}
		}
		
		return null;
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
	
	public String getName()
	{
		return this.name;
	}
}