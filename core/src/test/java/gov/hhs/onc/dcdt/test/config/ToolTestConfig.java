package gov.hhs.onc.dcdt.test.config;

import gov.hhs.onc.dcdt.config.ToolConfig;
import gov.hhs.onc.dcdt.config.ToolConfigException;

public class ToolTestConfig extends ToolConfig
{
	public final static String TEST_MODULE_NAME = "test";
	
	protected static ToolTestConfig instance;
	
	protected ToolTestConfig()
	{
		super(TEST_MODULE_NAME);
	}
	
	public static ToolTestConfig getInstance() throws ToolConfigException
	{
		if (instance == null)
		{
			instance = new ToolTestConfig();
			instance.initConfig();
		}
		
		return instance;
	}
}