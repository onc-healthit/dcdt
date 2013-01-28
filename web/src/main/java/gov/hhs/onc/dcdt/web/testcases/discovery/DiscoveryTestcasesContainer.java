package gov.hhs.onc.dcdt.web.testcases.discovery;

import gov.hhs.onc.dcdt.beans.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.config.ToolConfigException;
import gov.hhs.onc.dcdt.web.startup.ConfigInfo;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public abstract class DiscoveryTestcasesContainer
{
	private final static Map<String, DiscoveryTestcase> TESTCASES = new HashMap<>();
	
	public static void initTestcases() throws ToolConfigException
	{
		TESTCASES.clear();
		
		String testcaseMailProp, testcaseMail;
		
		for (DiscoveryTestcase testcase : ConfigInfo.getConfig().getDiscoveryTestcases())
		{
			testcaseMailProp = testcase.getMailProperty();
			
			if (StringUtils.isBlank((testcaseMail = ConfigInfo.getConfigProperty(testcaseMailProp))))
			{
				throw new ToolConfigException("Discovery testcase mail address not found: property=" + testcaseMailProp);
			}
			
			TESTCASES.put(testcaseMail, testcase);
		}
	}

	public static Map<String, DiscoveryTestcase> getTestcases()
	{
		return TESTCASES;
	}
}