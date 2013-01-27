package gov.hhs.onc.dcdt.config;

import org.apache.commons.lang.text.StrLookup;

public class ToolConfigStrLookup extends StrLookup
{
	private ToolConfig toolConfig;
	
	public ToolConfigStrLookup(ToolConfig toolConfig)
	{
		this.toolConfig = toolConfig;
	}
	
	@Override
	public String lookup(String key)
	{
		return this.toolConfig.getConfig().getString(ToolConfig.XPATH_ATTRIB_KEY_PREFIX + key);
	}
}