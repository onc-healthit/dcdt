package gov.hhs.onc.dcdt.config;

import org.apache.commons.lang.text.StrLookup;

public class ToolConfigStrLookup extends StrLookup
{
	private final static String MODULE_NAME_KEY = "module";
	
	private ToolConfig toolConfig;
	
	public ToolConfigStrLookup(ToolConfig toolConfig)
	{
		this.toolConfig = toolConfig;
	}
	
	@Override
	public String lookup(String key)
	{
		return key.equals(MODULE_NAME_KEY) ? toolConfig.getModuleName() : 
			this.toolConfig.getConfig().getString(XpathBuilder.buildAttribNames(key));
	}
}