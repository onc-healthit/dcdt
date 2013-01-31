package gov.hhs.onc.dcdt.web.startup;

import gov.hhs.onc.dcdt.beans.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.beans.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.config.ToolConfig;
import gov.hhs.onc.dcdt.config.ToolConfigException;
import gov.hhs.onc.dcdt.web.config.WebConfig;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Stores configuration properties for both the main application
 * configurations (read-only) and the email properties (read and write).
 *
 * @author jasonsmith
 * @author michal.kotelba@esacinc.com
 */
public class ConfigInfo
{
	private final static String CONFIG_WEB_NAME = "config-web";
	private final static String CONFIG_NAME = "config";
	private final static String EMAIL_CONFIG_NAME = "email";
	private final static String VERSION_CONFIG_NAME = "version";
	
	private final static Map<String, HostingTestcase> HOSTING_TESTCASES = new TreeMap<>();
	private final static Map<String, DiscoveryTestcase> DISCOVERY_TESTCASES = new TreeMap<>();
	
	private final static Logger LOGGER = Logger.getLogger(ConfigInfo.class);
	
	private static WebConfig config;

	public static void initTestcases() throws ToolConfigException
	{
		HOSTING_TESTCASES.clear();
		
		for (HostingTestcase hostingTestcase : config.getHostingTestcases())
		{
			HOSTING_TESTCASES.put(hostingTestcase.getId(), hostingTestcase);
		}
		
		DISCOVERY_TESTCASES.clear();
		
		String discoveryTestcaseMailProp, discoveryTestcaseMail;
		
		for (DiscoveryTestcase discoveryTestcase : config.getDiscoveryTestcases())
		{
			discoveryTestcaseMailProp = discoveryTestcase.getMailProperty();
			
			if (StringUtils.isBlank((discoveryTestcaseMail = getConfigProperty(discoveryTestcaseMailProp))))
			{
				throw new ToolConfigException("Discovery testcase mail address not found: property=" + discoveryTestcaseMailProp);
			}
			
			DISCOVERY_TESTCASES.put(discoveryTestcaseMail, discoveryTestcase);
		}
	}
	
	public synchronized static WebConfig getConfig()
	{
		return config;
	}
	
	public synchronized static void loadConfig() throws ToolConfigException
	{
		config = new WebConfig();
		config.initConfig();
	}

	/**
	 * Returns a version property for the given key value.
	 *
	 * @return version property value
	 */
	public synchronized static String getVersionProperty(String propKey)
	{
		return getVersionProperty(propKey, false);
	}
	
	/**
	 * Returns a version property for the given key value.
	 * Optionally allows for non-existent properties.
	 * 
	 * @return version property value
	 */
	public synchronized static String getVersionProperty(String propKey, boolean allowMissing)
	{
		return getProperty(getVersionProperties(), propKey, allowMissing);
	}

	/**
	 * Returns the non-Direct email address for the given Direct email address.
	 *
	 * @return non-Direct email address
	 */
	public synchronized static String getEmailProperty(String propKey)
	{
		return getEmailProperty(propKey, false);
	}
	
	/**
	 * Returns the non-Direct email address for the given Direct email address.
	 * Optionally allows for non-existent properties.
	 * 
	 * @return non-Direct email address
	 */
	public synchronized static String getEmailProperty(String propKey, boolean allowMissing)
	{
		return getProperty(getEmailProperties(), propKey, allowMissing);
	}

	/**
	 * Returns an application property for the given key value.
	 * 
	 * @return application property value
	 */
	public synchronized static String getConfigProperty(String propKey)
	{
		return getConfigProperty(propKey, false);
	}
	
	/**
	 * Returns an application property for the given key value.
	 * Optionally allows for non-existent properties.
	 * 
	 * @return application property value
	 */
	public synchronized static String getConfigProperty(String propKey, boolean allowMissing)
	{
		return getProperty(getConfigProperties(), propKey, allowMissing);
	}

	/**
	 * Adds or updates an email property value with the Direct
	 * and non-Direct email addresses.  Saves the property file.
	 */
	public synchronized static void setEmailProperty(String propKey, String propValue)
	{
		PropertiesConfiguration emailProps = getEmailProperties();
		
		if (emailProps.getProperty(propKey) == null)
		{
			emailProps.addProperty(propKey, propValue);
		}
		else
		{
			emailProps.setProperty(propKey, propValue);
		}
		try
		{
			emailProps.save();
		}
		catch (ConfigurationException e)
		{
			LOGGER.error(e);
		}
	}

	/**
	 * Stores the email properties file.
	 */
	public static void storeEmailProperties() throws ConfigurationException
	{
		getEmailProperties().save();
	}
	
	/**
	 * Gets the property value for a given properties configuration and key.
	 *
	 * @param props properties configuration to search in
	 * @param propKey key to get value for
	 * @return property value
	 */
	private synchronized static String getProperty(PropertiesConfiguration props, String propKey)
	{
		return getProperty(props, propKey, false);
	}
	
	/**
	 * Gets the property value for a given properties configuration and key.
	 * Optionally allows for non-existent properties.
	 *
	 * @param props properties configuration to search in
	 * @param propKey key to get value for
	 * @param allowMissing whether to allow for non-existent properties
	 * @return property value
	 */
	private synchronized static String getProperty(PropertiesConfiguration props, String propKey, boolean allowMissing)
	{
		String value = null;
		
		try
		{
			value = props.getString(propKey);
		}
		catch (NoSuchElementException e)
		{
			if (!allowMissing)
			{
				LOGGER.fatal("Property (key=" + propKey + ") is required in file: " + props.getPath(), e);
			}
			else
			{
				LOGGER.trace("Property (key=" + propKey + ") is missing in file: " + props.getPath());
			}
		}
		
		return value;
	}
	
	private synchronized static PropertiesConfiguration getConfigProperties()
	{
		return getProperties(ToolConfig.getAdditionalConfigSection(getWebConfig()), CONFIG_NAME);
	}
	
	private synchronized static PropertiesConfiguration getEmailProperties()
	{
		return getProperties(ToolConfig.getAdditionalConfigSection(getWebConfig()), EMAIL_CONFIG_NAME);
	}
	
	private synchronized static PropertiesConfiguration getVersionProperties()
	{
		return getProperties(config.getOverrideConfigSection(), VERSION_CONFIG_NAME);
	}
	
	private synchronized static PropertiesConfiguration getProperties(CombinedConfiguration configSection, String propsName)
	{
		return ToolConfig.getChildPropsConfig(configSection, propsName);
	}
	
	private synchronized static CombinedConfiguration getWebConfig()
	{
		return ToolConfig.getChildConfig(config.getAdditionalConfigSection(), CONFIG_WEB_NAME);
	}

	public static Map<String, DiscoveryTestcase> getDiscoveryTestcases()
	{
		return DISCOVERY_TESTCASES;
	}

	public static Map<String, HostingTestcase> getHostingTestcases()
	{
		return HOSTING_TESTCASES;
	}
}