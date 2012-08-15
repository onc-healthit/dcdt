package gov.onc.startup;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
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

	private static final String APACHE_HOME = System.getProperty("catalina.home");
	private static final String CONFIG_PROP_FILE = "conf/direct/config.properties";
	private static final String EMAIL_PROP_FILE = "conf/direct/email.properties";
		
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
			ConfigInfo.storeEmailProperties(APACHE_HOME
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
		setLogging(arg0);

		Logger log = Logger.getLogger("emailMessageLogger");
		try {
			ConfigInfo.loadConfigProperties(APACHE_HOME
				+ File.separatorChar + CONFIG_PROP_FILE);
			ConfigInfo.loadEmailProperties(APACHE_HOME
				+ File.separatorChar + EMAIL_PROP_FILE);
			directoryWatcher =
					new EmailDirectoryWatcher(ConfigInfo.getConfigProperty("EmlLocation"));
			directoryWatcher.start();
			log.info("Directory Watcher Started.");
		} catch (ConfigurationException e) {
			log.error("Error Loading Properties files.\n"
				+ e.getStackTrace());
		} catch (IOException e) {
			log.error("IO Error - Loading config files.\n"
				+ e.getStackTrace());
		}
	}

	/**
	 * Finds log4j.properties files and creates logs for
	 * cert discovery and email messaging. Outputs start-up to logging.
	 * @param e
	 */
	private void setLogging(ServletContextEvent e) {
		ServletContext ctx = e.getServletContext();

		String prefix =  ctx.getRealPath("/");
		String file = "WEB-INF" + System.getProperty("file.separator")
				+ "classes" + System.getProperty("file.separator")
				+ "log4j.properties";
		File isFile = new File(prefix + file);

		if (isFile.exists()) {
			PropertyConfigurator.configure(prefix + file);
			String message = "APPLICATION START - UP";

		    Logger logE = Logger.getLogger("emailMessageLogger");
		    Logger logC = Logger.getLogger("certDiscoveryLogger");

		    logE.info(message);
		    logC.info(message);

		    System.out.println("Log4J Logging started for application: "
		    	+ prefix + file);
		} else {
			System.out.println("Log4J is not configured for application: "
				+ prefix + file);
		}
	}
}
