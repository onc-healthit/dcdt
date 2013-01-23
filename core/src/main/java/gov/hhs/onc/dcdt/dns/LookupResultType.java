package gov.hhs.onc.dcdt.dns;

import java.util.EnumSet;
import org.xbill.DNS.Lookup;

public enum LookupResultType
{
	UNKNOWN(-1, "UNKNOWN"), 
	SUCCESSFUL(Lookup.SUCCESSFUL, "SUCCESSFUL"), 
	UNRECOVERABLE(Lookup.UNRECOVERABLE, "UNRECOVERABLE"), 
	TRY_AGAIN(Lookup.TRY_AGAIN, "TRY_AGAIN"), 
	HOST_NOT_FOUND(Lookup.HOST_NOT_FOUND, "HOST_NOT_FOUND"), 
	TYPE_NOT_FOUND(Lookup.TYPE_NOT_FOUND, "TYPE_NOT_FOUND");
	
	private int result;
	private String name;
	
	LookupResultType(int result, String name)
	{
		this.result = result;
		this.name = name;
	}
	
	public static LookupResultType fromResult(int result)
	{
		for (LookupResultType lookupResult : EnumSet.allOf(LookupResultType.class))
		{
			if (lookupResult.getResult() == result)
			{
				return lookupResult;
			}
		}
		
		return null;
	}
	
	public boolean isUnknown()
	{
		return this == UNKNOWN;
	}
	
	public boolean isSuccess()
	{
		return this == SUCCESSFUL;
	}
	
	public boolean isFailure()
	{
		return !this.isSuccess() && !this.isUnknown();
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

	public int getResult()
	{
		return this.result;
	}
}