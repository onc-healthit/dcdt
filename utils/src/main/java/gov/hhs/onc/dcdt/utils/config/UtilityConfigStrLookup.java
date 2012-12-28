package gov.hhs.onc.dcdt.utils.config;

import gov.hhs.onc.dcdt.utils.Utility;
import org.apache.commons.lang.text.StrLookup;

public class UtilityConfigStrLookup extends StrLookup
{
	private Utility util;
	
	public UtilityConfigStrLookup(Utility util)
	{
		this.util = util;
	}
	
	@Override
	public String lookup(String key)
	{
		return this.util.getUtilConfig().getString(key);
	}
}