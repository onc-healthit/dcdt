package gov.hhs.onc.dcdt.utils.config;

import gov.hhs.onc.dcdt.utils.Utility;
import org.apache.commons.lang.text.StrLookup;

public class UtilityConfigStrLookup extends StrLookup
{
	private final static String NAME_ATTRIB_KEY = "name";
	
	private Utility util;
	
	public UtilityConfigStrLookup(Utility util)
	{
		this.util = util;
	}
	
	@Override
	public String lookup(String key)
	{
		return !key.equals(NAME_ATTRIB_KEY) ? 
			this.util.getConfig().getUtilConfig().getString(UtilityConfig.XPATH_ATTRIB_KEY_PREFIX + key) : 
			this.util.getName();
	}
}