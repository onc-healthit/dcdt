package gov.hhs.onc.dcdt.startup;

import gov.hhs.onc.dcdt.decrypt.LookupTest;
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
	private static final String CONFIG_HOME_NAME = "dcdt.config.dir";
	private static final String CONFIG_PROP_FILE = "config.properties";
	private static final String EMAIL_PROP_FILE = "email.properties";
	private static final String VERSION_PROP_FILE = "version.properties";
	
	private static String configHome;
	
	private static EmailDirectoryWatcher directoryWatcher = null;
	
	/**
	 * On Application shut down, email properties file saved
	 * and action logged.
	 * @param contextEvent
	 */
	public void contextDestroyed(ServletContextEvent contextEvent) {

		Logger logE = Logger.getLogger("emailMessageLogger");
	    Logger logC = Logger.getLogger("certDiscoveryLogger");

		if (directoryWatcher != null) {
			directoryWatcher.stopRunning();
			logE.info("Directory Watcher Stopped.");
		}
		try {
			ConfigInfo.storeEmailProperties(configHome
				+ File.separatorChar + EMAIL_PROP_FILE);
			logE.info("Server SHUT DOWN");
			logC.info("Server SHUT DOWN");
		} catch (ConfigurationException e) {
			logE.error("Error storing email properties file.", e);
		}
	}

	/**
	 * On application start-up, properties files loaded
	 * and logging initialized.
	 * @param contextEvent
	 */
	public void contextInitialized(ServletContextEvent contextEvent) {
		configHome = System.getProperty(CONFIG_HOME_NAME);
		
		if(configHome == null)
		{
			System.getenv(CONFIG_HOME_NAME);
		}
		
		if (configHome == null)
		{
			LogLog.error("Configuration directory variable not set: " + CONFIG_HOME_NAME);
		}
		
		if (!Paths.get(configHome).toFile().exists())
		{
			LogLog.error("Configuration directory does not exist: " + configHome);
		}
		
		setLogging(contextEvent);

		Logger log = Logger.getLogger("emailMessageLogger");
		
		try {
			// Load in config files
			ConfigInfo.loadConfigProperties(configHome + File.separatorChar + CONFIG_PROP_FILE);
			ConfigInfo.loadEmailProperties(configHome + File.separatorChar + EMAIL_PROP_FILE);
			ConfigInfo.loadVerionProperties(contextEvent.getServletContext().getRealPath("/WEB-INF/classes/" + VERSION_PROP_FILE));
			
			// Initialize HashMap with LookupTest-specific info
			LookupTest.fillMap();
			
			// Kick off the thread to watch the directory where emails will arrive
			directoryWatcher =
					new EmailDirectoryWatcher(ConfigInfo.getConfigProperty("EmlLocation"));
			directoryWatcher.start();
			
			log.info("Directory Watcher Started.");
		} catch (ConfigurationException e) {
			log.error("Error Loading Properties files.", e);
		} catch (IOException e) {
			log.error("IO Error - Loading config files.", e);
		}
	}

	/**
	 * Finds log4j.properties files and creates logs for
	 * cert discovery and email messaging. Outputs start-up to logging.
	 * @param contextEvent
	 */
	private void setLogging(ServletContextEvent contextEvent) {
		String message = "APPLICATION START - UP";

		Logger logE = Logger.getLogger("emailMessageLogger");
		Logger logC = Logger.getLogger("certDiscoveryLogger");

		logE.info(message);
		logC.info(message);

		logE.info("Log4J Logging started for application.");
		logC.info("Log4J Logging started for application.");
	}
}
