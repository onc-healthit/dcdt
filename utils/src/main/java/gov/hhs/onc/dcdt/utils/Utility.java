package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.utils.cli.CliOption;
import gov.hhs.onc.dcdt.utils.cli.UtilityCli;
import gov.hhs.onc.dcdt.utils.cli.UtilityCliException;
import gov.hhs.onc.dcdt.utils.cli.UtilityCliOption;
import gov.hhs.onc.dcdt.utils.config.UtilityConfig;
import gov.hhs.onc.dcdt.utils.config.UtilityConfigException;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.security.Security;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.util.HelpFormatter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.extras.DOMConfigurator;
import org.apache.log4j.helpers.LogLog;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public abstract class Utility<T extends Enum<T> & CliOption>
{
	protected final static int EXIT_SUCCESS = 0;
	protected final static int EXIT_ERROR = 1;
	
	protected final static String LOG_DIR_SYS_PROP_NAME = "dcdt.utils.log.dir";
	protected final static String LOG_DIR_SYS_PROP_VALUE_DEFAULT = "logs";
	protected final static String LOG_FILE_NAME_BASE_SYS_PROP_NAME = "dcdt.utils.log.file.name.base";
	protected final static String LOG_FILE_NAME_BASE_SYS_PROP_VALUE_DEFAULT = "utils";
	protected final static String TERM_WIDTH_SYS_PROP_NAME = "dcdt.utils.term.width";
	
	protected final static String DISPLAY_NAME_ATTRIB_NAME = "displayName";
	
	private final static String LOG4J_DEFAULT_INIT_OVERRIDE_PROP_NAME = "log4j.defaultInitOverride";
	private final static String LOG4J_CONFIG_FILE_PATH = "conf/log4j.xml";
	private final static long LOG4J_CONFIG_FILE_WATCH_INTERVAL = 5000;
	
	private static Logger logger;
	
	protected String name;
	protected UtilityCli<T> cli;
	protected UtilityConfig<T> config;
	
	static
	{
		initLogging();
		
		Security.addProvider(new BouncyCastleProvider());
	}
	
	protected Utility(String name, UtilityCli<T> cli)
	{
		this.name = name;
		this.cli = cli;
		
		this.init();
	}
	
	public URL getResource(String resourcePath)
	{
		return Thread.currentThread().getContextClassLoader().getResource(resourcePath);
	}
	
	public InputStream getResourceAsStream(String resourcePath)
	{
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
	}
	
	protected static void printHelp(String name, Group optionsGroup, OptionException exception)
	{
		int termWidth = HelpFormatter.DEFAULT_FULL_WIDTH;
		String termWidthPropValue;
		
		if (System.getProperties().containsKey(TERM_WIDTH_SYS_PROP_NAME) && 
			StringUtils.isNumeric((termWidthPropValue = System.getProperty(TERM_WIDTH_SYS_PROP_NAME))))
		{
			termWidth = Integer.parseInt(termWidthPropValue);
		}
		
		HelpFormatter helpFormatter = new HelpFormatter(HelpFormatter.DEFAULT_GUTTER_LEFT, HelpFormatter.DEFAULT_GUTTER_CENTER, 
			HelpFormatter.DEFAULT_GUTTER_RIGHT, termWidth);
		helpFormatter.setException(exception);
		helpFormatter.setGroup(optionsGroup);
		helpFormatter.setShellCommand(name);
		
		helpFormatter.print();
	}
	
	protected static void exitError()
	{
		System.exit(EXIT_ERROR);
	}
	
	protected static void exitSuccess()
	{
		System.exit(EXIT_SUCCESS);
	}
	
	protected static void initLogging()
	{
		System.setProperty(LOG4J_DEFAULT_INIT_OVERRIDE_PROP_NAME, Boolean.toString(true));
		
		if (!System.getProperties().containsKey(LOG_DIR_SYS_PROP_NAME))
		{
			System.setProperty(LOG_DIR_SYS_PROP_NAME, LOG_DIR_SYS_PROP_VALUE_DEFAULT);
		}
		
		String logDirPath = System.getProperty(LOG_DIR_SYS_PROP_NAME);
		
		if (StringUtils.isBlank(logDirPath))
		{
			LogLog.error("Log directory system property (name=" + LOG_DIR_SYS_PROP_NAME + ") must be specified.");
			
			exitError();
		}
		
		File logDir = new File(logDirPath);
		
		if (logDir.exists())
		{
			if (!logDir.isDirectory())
			{
				LogLog.error("Log directory path is not a directory: " + logDir);
				
				exitError();
			}
		}
		else if (!logDir.mkdir())
		{
			LogLog.error("Unable to make log directory: " + logDir);
			
			exitError();
		}
		
		if (!System.getProperties().containsKey(LOG_FILE_NAME_BASE_SYS_PROP_NAME))
		{
			System.setProperty(LOG_FILE_NAME_BASE_SYS_PROP_NAME, LOG_FILE_NAME_BASE_SYS_PROP_VALUE_DEFAULT);
		}
		
		if (StringUtils.isBlank(System.getProperty(LOG_FILE_NAME_BASE_SYS_PROP_NAME)))
		{
			LogLog.error("Log file name base system property (name=" + LOG_FILE_NAME_BASE_SYS_PROP_NAME + ") must be specified.");
			
			exitError();
		}

		DOMConfigurator.configureAndWatch(LOG4J_CONFIG_FILE_PATH, LOG4J_CONFIG_FILE_WATCH_INTERVAL);
		
		logger = Logger.getLogger(Utility.class);
	}
	
	protected void execute(String ... args)
	{
		try
		{
			this.cli.parse(args);
		}
		catch (UtilityCliException e)
		{
			printHelp(this.name, this.cli.getOptionsGroup(), (OptionException)e.getCause());
			
			exitError();
		}
		
		this.processCmdLine();
	}
	
	protected void processCmdLine()
	{
		if (this.cli.getCmdLine().hasOption(UtilityCliOption.HELP.getOption()))
		{
			printHelp(this.name, this.cli.getOptionsGroup(), null);
			
			exitSuccess();
		}
	}
	
	protected void initConfig()
	{
		this.config = new UtilityConfig<>(this);
		
		try
		{
			this.config.initConfig();
		}
		catch (UtilityConfigException e)
		{
			logger.error(e.getMessage());
			
			exitError();
		}
	}
	
	protected void init()
	{
		this.initConfig();
	}

	public UtilityCli<T> getCli()
	{
		return this.cli;
	}

	public UtilityConfig getConfig()
	{
		return this.config;
	}
	
	public String getName()
	{
		return this.name;
	}
}