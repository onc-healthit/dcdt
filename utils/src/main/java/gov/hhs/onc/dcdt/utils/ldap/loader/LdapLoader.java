package gov.hhs.onc.dcdt.utils.ldap.loader;

import gov.hhs.onc.dcdt.utils.Utility;
import gov.hhs.onc.dcdt.utils.cli.UtilityCli;
import gov.hhs.onc.dcdt.utils.config.UtilityConfig;
import gov.hhs.onc.dcdt.utils.config.generator.ConfigGenCliOption;
import java.io.File;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class LdapLoader extends Utility<LdapLoaderCliOption>
{
	private final static String UTIL_NAME = "ldaploader";
	
	private final static Logger LOGGER = Logger.getLogger(LdapLoader.class);
	
	public LdapLoader()
	{
		super(UTIL_NAME, new UtilityCli<>(LdapLoaderCliOption.class));
	}
	
	public static void main(String ... args)
	{
		new LdapLoader().execute(args);
	}
	
	@Override
	protected void execute(String ... args)
	{
		super.execute(args);

		// TODO: implement
	}
	
	@Override
	protected void processCmdLine()
	{
		super.processCmdLine();
		
		this.config.getUtilConfig().setProperty(UtilityConfig.XPATH_ATTRIB_KEY_PREFIX + LdapLoaderCliOption.DOMAIN.getAttribName(), 
			this.cli.getOptionValue(LdapLoaderCliOption.DOMAIN));
		
		String inputDirPath = this.cli.hasOption(LdapLoaderCliOption.INPUT_DIR) ? 
			this.cli.getOptionValue(LdapLoaderCliOption.INPUT_DIR) : 
			this.config.getUtilConfig().getString(UtilityConfig.XPATH_ATTRIB_KEY_PREFIX + LdapLoaderCliOption.INPUT_DIR.getAttribName());
		
		if (StringUtils.isBlank(inputDirPath))
		{
			LOGGER.error("Input directory path must be specified.");
			
			exitError();
		}
		
		File inputDir = new File(inputDirPath);
		
		if (!inputDir.exists())
		{
			LOGGER.error("Input directory does not exist: " + inputDir);
			
			exitError();
		}
		else if (!inputDir.isDirectory())
		{
			LOGGER.error("Input directory path is not a directory: " + inputDir);
			
			exitError();
		}
		
		this.config.getUtilConfig().setProperty(UtilityConfig.XPATH_ATTRIB_KEY_PREFIX + ConfigGenCliOption.INPUT_DIR.getAttribName(), 
			inputDir.toString());
	}
}