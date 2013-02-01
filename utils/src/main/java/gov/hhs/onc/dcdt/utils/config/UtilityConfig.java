package gov.hhs.onc.dcdt.utils.config;

import gov.hhs.onc.dcdt.config.ToolConfig;
import gov.hhs.onc.dcdt.config.ToolConfigException;
import gov.hhs.onc.dcdt.config.XpathBuilder;
import gov.hhs.onc.dcdt.utils.Utility;
import gov.hhs.onc.dcdt.utils.cli.CliOption;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.interpol.ConfigurationInterpolator;
import org.apache.commons.lang3.ObjectUtils;

public class UtilityConfig<T extends Enum<T> & CliOption> extends ToolConfig
{
	public final static String UTILS_MODULE_NAME = "utils";
	
	private final static String UTIL_LOOKUP_PREFIX = "util";
	
	private Utility<T> util;
	
	public UtilityConfig(Utility<T> util)
	{
		super(UTILS_MODULE_NAME);
		
		this.util = util;
	}
	
	public String getUtilString(T option)
	{
		return this.getUtilString(option, null);
	}
	
	public String getUtilString(T option, Object defaultValue)
	{
		return ObjectUtils.toString(this.util.getCli().getOptionValue(option), this.getUtilString(option.getAttribName(), 
			defaultValue));
	}
	
	public String getUtilString(String key)
	{
		return this.getUtilString(key, null);
	}
	
	public String getUtilString(String key, Object defaultValue)
	{
		return this.getUtilConfig().getString(XpathBuilder.buildAttribNames(key), ObjectUtils.toString(defaultValue, null));
	}
	
	public void setUtilString(T option)
	{
		this.setUtilString(option, null);
	}
	
	public void setUtilString(T option, Object defaultValue)
	{
		this.setUtilString(option.getAttribName(), this.getUtilString(option), defaultValue);
	}
	
	public void setUtilString(String key, Object value)
	{
		this.setUtilString(key, value, null);
	}
	
	public void setUtilString(String key, Object value, Object defaultValue)
	{
		this.getUtilConfig().setProperty(XpathBuilder.buildAttribNames(key), ObjectUtils.defaultIfNull(value, defaultValue));
	}
	
	public SubnodeConfiguration getUtilConfig()
	{
		return this.config.configurationAt(this.util.getName());
	}

	@Override
	public void initConfig() throws ToolConfigException
	{
		ConfigurationInterpolator.registerGlobalLookup(UTIL_LOOKUP_PREFIX, new UtilityConfigStrLookup(this.util));
		
		super.initConfig();
	}
}