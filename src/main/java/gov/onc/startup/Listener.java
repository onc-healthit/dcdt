package gov.onc.startup;

import gov.onc.decrypt.LookupTest;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContextEvent;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Listens on application start-up / shut-down.  Loads/saves configuration
 * files and initializes logging.
 * @author jasonsmith
 *
 */
public class Listener implements javax.servlet.ServletContextListener {

	private String configHome;
	private static final String CONFIG_HOME_NAME = "CONFIG_DIR";
	private static final String CONFIG_PROP_FILE = "config.properties";
	private static final String EMAIL_PROP_FILE = "email.properties";
		
	private static EmailDirectoryWatcher directoryWatcher = null;

	/**
	 * On Application shut down, email properties file saved
	 * and action logged.
	 * @param arg0
	 */
	public void contextDestroyed(ServletContextEvent arg0) {

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
			logE.error("Error storing email properties file.\n"
				+ e.getStackTrace());
		}
	}

	/**
	 * On application start-up, properties files loaded
	 * and logging initialized.
	 * @param arg0
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		configHome = System.getProperty(CONFIG_HOME_NAME);
		
		if(configHome == null)
			System.getenv(CONFIG_HOME_NAME);
		
		if(configHome == null)
			System.out.println("ERROR:  Configuration directory variable not set!!!");
		
		setLogging(arg0);

		Logger log = Logger.getLogger("emailMessageLogger");
		try {
			// Load in config files
			ConfigInfo.loadConfigProperties(configHome
				+ File.separatorChar + CONFIG_PROP_FILE);
			ConfigInfo.loadEmailProperties(configHome
				+ File.separatorChar + EMAIL_PROP_FILE);
			
			// Initialize HashMap with LookupTest-specific info
			LookupTest.fillMap();
			
			// Kick off the thread to watch the directory where emails will arrive
			directoryWatcher =
					new EmailDirectoryWatcher(ConfigInfo.getConfigProperty("EmlLocation"));
			directoryWatcher.start();
			
			log.info("Directory Watcher Started.");
		} catch (ConfigurationException e) {
			log.error("Error Loading Properties files.\n"
				+ e.getMessage());
		} catch (IOException e) {
			log.error("IO Error - Loading config files.\n"
				+ e.getMessage());
		}
	}

	/**
	 * Finds log4j.properties files and creates logs for
	 * cert discovery and email messaging. Outputs start-up to logging.
	 * @param e
	 */
	private void setLogging(ServletContextEvent e) {
		String loggingProp = configHome + File.separatorChar + "log4j.properties";
		File isFile = new File(loggingProp);

		if (isFile.exists()) {
			PropertyConfigurator.configure(loggingProp);
			String message = "APPLICATION START - UP";

		    Logger logE = Logger.getLogger("emailMessageLogger");
		    Logger logC = Logger.getLogger("certDiscoveryLogger");

		    logE.info(message);
		    logC.info(message);

		    logE.info("Log4J Logging started for application with properties: "
		    	+ loggingProp);
		    logC.info("Log4J Logging started for application with properties: "
			    	+ loggingProp);
		} else {
			System.out.println("Log4J is not configured for application with properties: "
				+ loggingProp);
		}
	}
}
