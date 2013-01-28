package gov.hhs.onc.dcdt.beans.entry;

import java.util.EnumSet;

public enum EntryDestinationType
{
	NONE("none"), ANCHOR("anchor"), DNS("dns"), LDAP("ldap");
	
	private String type;
	
	private EntryDestinationType(String type)
	{
		this.type = type;
	}

	public static EntryDestinationType fromType(String type)
	{
		for (EntryDestinationType destType : EnumSet.allOf(EntryDestinationType.class))
		{
			if (destType.getType().equals(type))
			{
				return destType;
			}
		}
		
		return EntryDestinationType.NONE;
	}
	
	@Override
	public String toString()
	{
		return this.getType();
	}
	
	public String getType()
	{
		return this.type;
	}
}