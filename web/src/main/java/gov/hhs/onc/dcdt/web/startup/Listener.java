package gov.hhs.onc.dcdt.web.startup;

import gov.hhs.onc.dcdt.config.ToolConfigException;
import gov.hhs.onc.dcdt.web.testcases.discovery.DiscoveryTestcasesContainer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

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
	
	private static final String CONFIG_DIR_KEY = "dcdt.config.dir";
	private static final String CONTEXT_DIR_KEY = "dcdt.context.dir";
	
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
			ConfigInfo.storeEmailProperties();
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
		
		configHome = System.getProperty(CONFIG_DIR_KEY);
		
		if(configHome == null)
		{
			configHome = System.getenv(CONFIG_DIR_KEY);
		}
		
		try
		{
			if (configHome == null)
			{
				throw new ToolConfigException("Configuration directory variable not set: " + CONFIG_DIR_KEY);
			}
			
			File configHomeDir = Paths.get(configHome).toFile();
			
			if (!configHomeDir.exists())
			{
				throw new ToolConfigException("Configuration directory does not exist: " + configHomeDir);
			}
			
			if (!configHomeDir.isDirectory())
			{
				throw new ToolConfigException("Configuration directory path is not a directory: " + configHomeDir);
			}
			
			System.setProperty(CONFIG_DIR_KEY, configHome);
			System.setProperty(CONTEXT_DIR_KEY, contextEvent.getServletContext().getRealPath("/"));
			
			// Load configuration
			ConfigInfo.loadConfig();
			
			// Initialize Discovery testcases
			DiscoveryTestcasesContainer.initTestcases();
		}
		catch (ToolConfigException e)
		{
			LOGGER.error("Unable to load configuration.", e);
		}
		
		try
		{
			// Kick off the thread to watch the directory where emails will arrive
			directoryWatcher = new EmailDirectoryWatcher(ConfigInfo.getConfigProperty("EmlLocation"));
			directoryWatcher.start();
		}
		catch (IOException e)
		{
			LOGGER.error("Unable to start mail directory watcher.", e);
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
