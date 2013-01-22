package gov.hhs.onc.dcdt.web.startup;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * Stores configuration properties for both the main application 
 * configurations (read-only) and the email properties (read and write).
 * @author jasonsmith
 *
 */
public class ConfigInfo {

	private static PropertiesConfiguration configProperties;
	private static PropertiesConfiguration emailProperties;
	private static PropertiesConfiguration versionProperties;
	private final static Logger LOGGER = Logger.getLogger(ConfigInfo.class);

	/**
	 * Loads version properties from local file.
	 * @param fileLocation
	 * @throws ConfigurationException
	 */
	public static void loadVerionProperties(String fileLocation)
			throws ConfigurationException
	{
		versionProperties = new PropertiesConfiguration(fileLocation);
	}
	
	/**
	 * Loads email properties from local file.
	 * @param fileLocation
	 * @throws ConfigurationException
	 */
	public static void loadEmailProperties(String fileLocation)
			throws ConfigurationException {
		emailProperties = new PropertiesConfiguration(fileLocation);
	}

	/**
	 * Loads application properties from local file.
	 * @param fileLocation
	 * @throws ConfigurationException
	 */
	public static void loadConfigProperties(String fileLocation)
			throws ConfigurationException {
		configProperties = new PropertiesConfiguration(fileLocation);
	}

	/**
	 * Returns a version property for the given key value.
	 * @param key
	 * @return version property value
	 */
	public static synchronized String getVersionProperty(String key)
	{
		String value = versionProperties.getString(key);
		
		if (value == null)
		{
			LOGGER.error("Properties file: " + versionProperties.getFileName() + 
				" is missing required property: " + key + ".");
			
			return null;
		}
		
		return value;
	}
	
	/**
	 * Returns the non-Direct email address for the given Direct email
	 * address.
	 * @param key
	 * @return non-Direct email address
	 */
	public static synchronized String getEmailProperty(String key) {
		return emailProperties.getString(key);
	}

	/**
	 * Returns an application property for the given key value.
	 * @param key
	 * @return application property value
	 */
	public static synchronized String getConfigProperty(String key) {
		String value = configProperties.getString(key);
		if (value == null)
			LOGGER.fatal("Properties file: " + configProperties.getFileName() +
					" is missing required property: " + key + ".");

		// Currently doesn't shut down, so if missing property at this point,
		// there may be side effect errors.
		return value;
	}

	/**
	 * Adds or updates an email property value with the Direct
	 * and non-Direct email addresses.  Saves the property file.
	 * @param key
	 * @param value
	 */
	public static synchronized void setEmailProperty(String key, String value) {
		if (emailProperties.getProperty(key) == null) {
			emailProperties.addProperty(key, value);
		} else {
			emailProperties.setProperty(key, value);
		}
		try {
			emailProperties.save();
		} catch (ConfigurationException e) {
			LOGGER.error(e);
		}
	}

	/**
	 * Stores the email properties file.
	 * @param fileLocation
	 * @throws ConfigurationException
	 */
	public static void storeEmailProperties(String fileLocation)
			throws ConfigurationException {
		emailProperties.save();
	}

}

