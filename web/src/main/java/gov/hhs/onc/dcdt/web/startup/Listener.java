package gov.hhs.onc.dcdt.web.startup;

import gov.hhs.onc.dcdt.web.mail.decrypt.LookupTest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.LogLog;

/**
 * Listens on application start-up / shut-down.  Loads/saves configuration
 * files and initializes logging.
 * @author jasonsmith
 *
 */
public class Listener implements ServletContextListener
{
	private static final String CERT_DISCOVERY_LOGGER_NAME = "certDiscoveryLogger";
	private static final String EMAIL_MSG_LOGGER_NAME = "emailMessageLogger";
	
	private final static Logger CERT_DISCOVERY_LOGGER = Logger.getLogger(CERT_DISCOVERY_LOGGER_NAME);
	private final static Logger EMAIL_MSG_LOGGER = Logger.getLogger(EMAIL_MSG_LOGGER_NAME);
	private final static Logger LOGGER = Logger.getLogger(Listener.class);
	
	private static final String CONFIG_HOME_NAME = "dcdt.config.dir";
	private static final String CONFIG_PROP_FILE = "config.properties";
	private static final String EMAIL_PROP_FILE = "email.properties";
	private static final String VERSION_PROP_FILE = "META-INF/gov/hhs/onc/dcdt/web/version.properties";
	
	private static String configHome;
	
	private static EmailDirectoryWatcher directoryWatcher = null;
	
	/**
	 * On Application shut down, email properties file saved
	 * and action logged.
	 * @param contextEvent
	 */
	public void contextDestroyed(ServletContextEvent contextEvent) {
		if (directoryWatcher != null) {
			directoryWatcher.stopRunning();
			EMAIL_MSG_LOGGER.info("Directory Watcher Stopped.");
		}
		try {
			ConfigInfo.storeEmailProperties(configHome
				+ File.separatorChar + EMAIL_PROP_FILE);
			EMAIL_MSG_LOGGER.info("Server SHUT DOWN");
			CERT_DISCOVERY_LOGGER.info("Server SHUT DOWN");
		} catch (ConfigurationException e) {
			EMAIL_MSG_LOGGER.error("Error storing email properties file.", e);
		}
	}

	/**
	 * On application start-up, properties files loaded
	 * and logging initialized.
	 * @param contextEvent
	 */
	public void contextInitialized(ServletContextEvent contextEvent) {
		setLogging(contextEvent);
		
		configHome = System.getProperty(CONFIG_HOME_NAME);
		
		if(configHome == null)
		{
			System.getenv(CONFIG_HOME_NAME);
		}
		
		if (configHome == null)
		{
			LOGGER.error("Configuration directory variable not set: " + CONFIG_HOME_NAME);
		}
		
		if (!Paths.get(configHome).toFile().exists())
		{
			LOGGER.error("Configuration directory does not exist: " + configHome);
		}
		
		try {
			// Load in config files
			ConfigInfo.loadConfigProperties(configHome + File.separatorChar + CONFIG_PROP_FILE);
			ConfigInfo.loadEmailProperties(configHome + File.separatorChar + EMAIL_PROP_FILE);
			ConfigInfo.loadVerionProperties(contextEvent.getServletContext().getRealPath("/" + VERSION_PROP_FILE));
			
			// Initialize HashMap with LookupTest-specific info
			LookupTest.fillMap();
			
			// Kick off the thread to watch the directory where emails will arrive
			directoryWatcher =
					new EmailDirectoryWatcher(ConfigInfo.getConfigProperty("EmlLocation"));
			directoryWatcher.start();
			
			LOGGER.info("Directory Watcher Started.");
		} catch (ConfigurationException e) {
			LOGGER.error("Error Loading Properties files.", e);
		} catch (IOException e) {
			LOGGER.error("IO Error - Loading config files.", e);
		}
	}

	/**
	 * Finds log4j.properties files and creates logs for
	 * cert discovery and email messaging. Outputs start-up to logging.
	 * @param contextEvent
	 */
	private void setLogging(ServletContextEvent contextEvent) {
		String message = "APPLICATION START - UP";

		EMAIL_MSG_LOGGER.info(message);
		CERT_DISCOVERY_LOGGER.info(message);

		EMAIL_MSG_LOGGER.info("Log4J Logging started for application.");
		CERT_DISCOVERY_LOGGER.info("Log4J Logging started for application.");
	}
}
