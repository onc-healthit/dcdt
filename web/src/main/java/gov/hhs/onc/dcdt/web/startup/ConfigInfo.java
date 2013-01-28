package gov.hhs.onc.dcdt.web.startup;

import gov.hhs.onc.dcdt.config.ToolConfigException;
import gov.hhs.onc.dcdt.web.config.WebConfig;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
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
	private final static Logger LOGGER = Logger.getLogger(ConfigInfo.class);
	
	private static WebConfig config;
	private static PropertiesConfiguration configProperties;
	private static PropertiesConfiguration emailProperties;
	private static PropertiesConfiguration versionProperties;

	public static WebConfig getConfig()
	{
		return config;
	}
	
	public static void loadConfig() throws ToolConfigException
	{
		config = new WebConfig();
		config.initConfig();
	}
	
	/**
	 * Loads version properties from local file.
	 */
	public static void loadVersionProperties(String fileLocation)
		throws ConfigurationException
	{
		versionProperties = new PropertiesConfiguration(fileLocation);
	}

	/**
	 * Loads email properties from local file.
	 */
	public static void loadEmailProperties(String fileLocation)
		throws ConfigurationException
	{
		emailProperties = new PropertiesConfiguration(fileLocation);
	}

	/**
	 * Loads application properties from local file.
	 */
	public static void loadConfigProperties(String fileLocation)
		throws ConfigurationException
	{
		configProperties = new PropertiesConfiguration(fileLocation);
	}

	/**
	 * Returns a version property for the given key value.
	 *
	 * @return version property value
	 */
	public static synchronized String getVersionProperty(String key)
	{
		return getVersionProperty(key, false);
	}
	
	/**
	 * Returns a version property for the given key value.
	 * Optionally allows for non-existent properties.
	 * 
	 * @return version property value
	 */
	public static synchronized String getVersionProperty(String key, boolean allowMissing)
	{
		return getProperty(versionProperties, key, allowMissing);
	}

	/**
	 * Returns the non-Direct email address for the given Direct email address.
	 *
	 * @return non-Direct email address
	 */
	public static synchronized String getEmailProperty(String key)
	{
		return getEmailProperty(key, false);
	}
	
	/**
	 * Returns the non-Direct email address for the given Direct email address.
	 * Optionally allows for non-existent properties.
	 * 
	 * @return non-Direct email address
	 */
	public static synchronized String getEmailProperty(String key, boolean allowMissing)
	{
		return getProperty(emailProperties, key, allowMissing);
	}

	/**
	 * Returns an application property for the given key value.
	 * 
	 * @return application property value
	 */
	public static synchronized String getConfigProperty(String key)
	{
		return getConfigProperty(key, false);
	}
	
	/**
	 * Returns an application property for the given key value.
	 * Optionally allows for non-existent properties.
	 * 
	 * @return application property value
	 */
	public static synchronized String getConfigProperty(String key, boolean allowMissing)
	{
		return getProperty(configProperties, key, allowMissing);
	}

	/**
	 * Adds or updates an email property value with the Direct
	 * and non-Direct email addresses.  Saves the property file.
	 */
	public static synchronized void setEmailProperty(String key, String value)
	{
		if (emailProperties.getProperty(key) == null)
		{
			emailProperties.addProperty(key, value);
		}
		else
		{
			emailProperties.setProperty(key, value);
		}
		try
		{
			emailProperties.save();
		}
		catch (ConfigurationException e)
		{
			LOGGER.error(e);
		}
	}

	/**
	 * Stores the email properties file.
	 */
	public static void storeEmailProperties(String fileLocation)
		throws ConfigurationException
	{
		emailProperties.save();
	}
	
	/**
	 * Gets the property value for a given properties configuration and key.
	 *
	 * @param props properties configuration to search in
	 * @param key key to get value for
	 * @return property value
	 */
	private static synchronized String getProperty(PropertiesConfiguration props, String key)
	{
		return getProperty(props, key, false);
	}
	
	/**
	 * Gets the property value for a given properties configuration and key.
	 * Optionally allows for non-existent properties.
	 *
	 * @param props properties configuration to search in
	 * @param key key to get value for
	 * @param allowMissing whether to allow for non-existent properties
	 * @return property value
	 */
	private static synchronized String getProperty(PropertiesConfiguration props, String key, boolean allowMissing)
	{
		String value = props.getString(key);
		
		if (!allowMissing && (value == null))
		{
			LOGGER.fatal("Properties file (" + props.getFileName() + ") is missing required property: " + 
				key + ".");
		}

		// Currently doesn't shut down, so if missing property at this point,
		// there may be side effect errors.
		return value;
	}
}