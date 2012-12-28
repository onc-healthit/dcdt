package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.utils.cli.UtilityCli;
import gov.hhs.onc.dcdt.utils.cli.UtilityCliException;
import gov.hhs.onc.dcdt.utils.cli.UtilityCliOption;
import gov.hhs.onc.dcdt.utils.config.UtilityConfigListener;
import gov.hhs.onc.dcdt.utils.config.UtilityConfigStrLookup;
import java.security.Security;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.interpol.ConfigurationInterpolator;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public abstract class Utility
{
	protected final static int EXIT_SUCCESS = 0;
	protected final static int EXIT_ERROR = 1;
	
	protected final static String DISPLAY_NAME_PROP_NAME = "displayName";
	
	private final static String UTIL_LOOKUP_PREFIX = "util";
	
	private final static String CONFIG_FILE_PATH = "config.xml";
	
	private final static Logger LOGGER = Logger.getLogger(Utility.class);
	
	protected String name;
	protected DefaultConfigurationBuilder configBuilder;
	protected CombinedConfiguration config;
	protected UtilityConfigListener configListener;
	protected UtilityCli cli;
	protected UtilityBeans utilBeans;
	
	static
	{
		Security.addProvider(new BouncyCastleProvider());
	}
	
	protected Utility(String name, UtilityCli cli)
	{
		this.name = name;
		this.cli = cli;
		
		this.init();
	}
	
	public SubnodeConfiguration getUtilConfig()
	{
		return this.config.configurationAt(this.name);
	}
	
	protected static void printHelp(String name, Options options)
	{
		HelpFormatter helpFormatter = new HelpFormatter();
		// TODO: set left padding and width
		// helpFormatter.setLeftPadding(HelpFormatter.DEFAULT_LEFT_PAD);
		// helpFormatter.setWidth(HelpFormatter.DEFAULT_WIDTH);
		
		helpFormatter.printHelp(name, options);
	}
	
	protected static void exitError()
	{
		System.exit(EXIT_ERROR);
	}
	
	protected static void exitSuccess()
	{
		System.exit(EXIT_SUCCESS);
	}
	
	protected void execute(String ... args)
	{
		try
		{
			this.cli.parse(args);
		}
		catch (UtilityCliException e)
		{
			LOGGER.error(e);
			
			System.exit(EXIT_ERROR);
		}
		
		this.processCmdLine();
	}
	
	protected void processCmdLine()
	{
		if (!this.cli.hasOptions() || this.cli.hasOption(UtilityCliOption.HELP.getOption()))
		{
			printHelp(this.name, this.cli.getOptions());
			
			exitSuccess();
		}
	}
	
	protected void initConfig()
	{
		try
		{
			this.configListener = new UtilityConfigListener(this);
			
			this.configBuilder = new DefaultConfigurationBuilder(CONFIG_FILE_PATH);
			this.configBuilder.addConfigurationListener(this.configListener);
			this.configBuilder.addErrorListener(this.configListener);
			ConfigurationUtils.enableRuntimeExceptions(this.configBuilder);
			
			ConfigurationInterpolator.registerGlobalLookup(UTIL_LOOKUP_PREFIX, new UtilityConfigStrLookup(this));
			
			this.config = this.configBuilder.getConfiguration(true);
			this.config.addConfigurationListener(this.configListener);
			this.config.addErrorListener(this.configListener);
			ConfigurationUtils.enableRuntimeExceptions(this.config);
		}
		catch (ConfigurationException e)
		{
			LOGGER.error("Unable to initialize utility (name=" + this.name + ") configuration.", e);
			
			exitError();
		}
	}
	
	protected void init()
	{
		this.initConfig();
		
		this.utilBeans = new UtilityBeans(this);
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