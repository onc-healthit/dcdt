package gov.hhs.onc.dcdt.beans.testcases.hosting;

import java.util.EnumSet;

public enum HostingTestcaseLocation
{
	DNS("dns"), LDAP("ldap");
	
	private String name;
	
	HostingTestcaseLocation(String name)
	{
		this.name = name;
	}

	public static HostingTestcaseLocation fromName(String name)
	{
		for (HostingTestcaseLocation location : EnumSet.allOf(HostingTestcaseLocation.class))
		{
			if (location.getName().equalsIgnoreCase(name))
			{
				return location;
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