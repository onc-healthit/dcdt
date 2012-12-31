package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.utils.cli.CliOption;
import gov.hhs.onc.dcdt.utils.cli.UtilityCli;
import gov.hhs.onc.dcdt.utils.cli.UtilityCliException;
import gov.hhs.onc.dcdt.utils.cli.UtilityCliOption;
import gov.hhs.onc.dcdt.utils.config.UtilityConfigEntityResolver;
import gov.hhs.onc.dcdt.utils.config.UtilityConfigListener;
import gov.hhs.onc.dcdt.utils.config.UtilityConfigStrLookup;
import java.io.File;
import java.security.Security;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.util.HelpFormatter;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationRuntimeException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.interpol.ConfigurationInterpolator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.extras.DOMConfigurator;
import org.apache.log4j.helpers.LogLog;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.xml.sax.SAXParseException;

public abstract class Utility<T extends Enum<T> & CliOption>
{
	protected final static int EXIT_SUCCESS = 0;
	protected final static int EXIT_ERROR = 1;
	
	protected final static String TERM_WIDTH_PROP_NAME = "dcdt.utils.term.width";
	
	protected final static String DISPLAY_NAME_ATTRIB_NAME = "displayName";
	
	private final static String LOG4J_DEFAULT_INIT_OVERRIDE_PROP_NAME = "log4j.defaultInitOverride";
	private final static String LOG4J_CONFIG_FILE_PATH = "conf/log4j.xml";
	private final static long LOG4J_CONFIG_FILE_WATCH_INTERVAL = 5000;
	private final static String LOGS_DIR_PATH = "logs";
	
	private final static String CONFIG_FILE_PATH = "config.xml";
	private final static String CONFIG_PARSE_ERROR_SOURCE_DELIM = ":";
	
	private final static String UTIL_LOOKUP_PREFIX = "util";
	
	private static Logger logger;
	
	protected String name;
	protected UtilityConfigEntityResolver configEntityResolver;
	protected DefaultConfigurationBuilder configBuilder;
	protected CombinedConfiguration config;
	protected UtilityConfigListener configListener;
	protected UtilityCli<T> cli;
	protected UtilityData data;
	
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
	
	public SubnodeConfiguration getUtilConfig()
	{
		return this.config.configurationAt(this.name);
	}
	
	protected static void printHelp(String name, Group optionsGroup, OptionException exception)
	{
		int termWidth = HelpFormatter.DEFAULT_FULL_WIDTH;
		String termWidthPropValue;
		
		if (System.getProperties().containsKey(TERM_WIDTH_PROP_NAME) && 
			StringUtils.isNumeric((termWidthPropValue = System.getProperty(TERM_WIDTH_PROP_NAME))))
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

		File logsDir = new File(LOGS_DIR_PATH);
		
		if (logsDir.exists())
		{
			if (!logsDir.isDirectory())
			{
				LogLog.error("Logs directory path is not a directory: " + logsDir);
				
				exitError();
			}
		}
		else if (!logsDir.mkdir())
		{
			LogLog.error("Unable to make logs directory: " + logsDir);
			
			exitError();
		}

		DOMConfigurator.configureAndWatch(LOG4J_CONFIG_FILE_PATH, LOG4J_CONFIG_FILE_WATCH_INTERVAL);
		
		logger = Logger.getLogger(Utility.class);
	}
	
	protected static Throwable getRootConfigurationCause(Throwable throwable)
	{
		return (!(throwable instanceof ConfigurationException) && !(throwable instanceof ConfigurationRuntimeException)) || 
			(throwable.getCause() == null) || (throwable.getCause() == throwable) ? throwable : 
			getRootConfigurationCause(throwable.getCause());
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
		try
		{
			this.configListener = new UtilityConfigListener(this);
			
			this.configEntityResolver = new UtilityConfigEntityResolver();
			
			this.configBuilder = new DefaultConfigurationBuilder(CONFIG_FILE_PATH);
			this.configBuilder.addConfigurationListener(this.configListener);
			this.configBuilder.addErrorListener(this.configListener);
			this.configBuilder.setEntityResolver(this.configEntityResolver);
			ConfigurationUtils.enableRuntimeExceptions(this.configBuilder);
			
			ConfigurationInterpolator.registerGlobalLookup(UTIL_LOOKUP_PREFIX, new UtilityConfigStrLookup(this));
			
			this.config = this.configBuilder.getConfiguration(true);
			this.config.addConfigurationListener(this.configListener);
			this.config.addErrorListener(this.configListener);
			ConfigurationUtils.enableRuntimeExceptions(this.config);
		}
		catch (ConfigurationException | ConfigurationRuntimeException e)
		{
			Throwable rootConfigCause = getRootConfigurationCause(e);
			
			if (rootConfigCause instanceof SAXParseException)
			{
				SAXParseException parseConfigCause = (SAXParseException)rootConfigCause;
				
				logger.error("Invalid configuration: source=" + StringUtils.join(new Object[]{ parseConfigCause.getSystemId(), 
					parseConfigCause.getLineNumber(), parseConfigCause.getColumnNumber() }, CONFIG_PARSE_ERROR_SOURCE_DELIM) + ", error=" + 
					parseConfigCause.getMessage());
			}
			else
			{
				logger.error("Unable to initialize utility (name=" + this.name + ") configuration.", e);
			}
			
			exitError();
		}
	}
	
	protected void init()
	{
		this.initConfig();
		
		this.data = new UtilityData(this);
	}

	public String getName()
	{
		return this.name;
	}

	public HierarchicalConfiguration getConfig()
	{
		return this.config;
	}
}