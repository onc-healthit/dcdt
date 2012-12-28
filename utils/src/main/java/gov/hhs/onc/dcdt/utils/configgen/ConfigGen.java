package gov.hhs.onc.dcdt.utils.configgen;

import gov.hhs.onc.dcdt.utils.Utility;
import gov.hhs.onc.dcdt.utils.cli.UtilityCli;
import gov.hhs.onc.dcdt.utils.configgen.cli.ConfigGenCliOption;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class ConfigGen extends Utility
{
	private final static String UTIL_NAME = "configgen";
	
	private final static String DOMAIN_PROP_NAME = "domain";
	private final static String INPUT_DIR_PROP_NAME = "inputDir";
	private final static String OUTPUT_FILE_PROP_NAME = "outputFile";
	private final static String OUTPUT_FILE_INTERNAL_PATH_PROP_NAME = OUTPUT_FILE_PROP_NAME + "InternalPath";
	
	private final static String BASE_CONFIG_PROPS_NAME = UTIL_NAME + "-base-config";
	private final static String BASE_EMAIL_PROPS_NAME = UTIL_NAME + "-base-email";
	
	private final static Logger LOGGER = Logger.getLogger(ConfigGen.class);
	
	public ConfigGen()
	{
		super(UTIL_NAME, new UtilityCli(ConfigGenCliOption.class));
	}
	
	public static void main(String ... args)
	{
		new ConfigGen().execute(args);
	}
	
	@Override
	protected void execute(String ... args)
	{
		super.execute(args);
		
		CombinedConfiguration configAdditional = (CombinedConfiguration)this.config.getConfiguration(
			DefaultConfigurationBuilder.ADDITIONAL_NAME);
		PropertiesConfiguration baseConfigProps = (PropertiesConfiguration)configAdditional.getConfiguration(BASE_CONFIG_PROPS_NAME), 
			baseEmailProps = (PropertiesConfiguration)configAdditional.getConfiguration(BASE_EMAIL_PROPS_NAME);

		File outputFile = new File(this.getUtilConfig().getString(OUTPUT_FILE_PROP_NAME));
		String outputFileInternalPath = this.getUtilConfig().getString(OUTPUT_FILE_INTERNAL_PATH_PROP_NAME);
		
		try
		{
			if (!outputFile.exists())
			{
				FileUtils.touch(outputFile);
			}
			
			ByteArrayOutputStream outputPropsStream;
			ZipOutputStream outputFileStream = new ZipOutputStream(new FileOutputStream(outputFile));
			
			for (PropertiesConfiguration baseProps : new PropertiesConfiguration[]{ baseConfigProps, baseEmailProps })
			{
				outputFileStream.putNextEntry(new ZipEntry(outputFileInternalPath + File.separatorChar + baseProps.getFile().getName()));
				outputPropsStream = new ByteArrayOutputStream();
				((PropertiesConfiguration)baseProps.interpolatedConfiguration()).save(outputPropsStream);
				outputFileStream.write(outputPropsStream.toByteArray());
			}
			
			outputFileStream.finish();
			outputFileStream.close();
			
			LOGGER.info("Successfully generated configuration output file: " + outputFile);
		}
		catch (ConfigurationException | IOException e)
		{
			LOGGER.error("Unable to generate configuration output file: " + outputFile, e);
		}
	}

	@Override
	protected void processCmdLine()
	{
		super.processCmdLine();
		
		String domain = this.cli.getOptionValue(ConfigGenCliOption.DOMAIN.getOption());
		
		if (StringUtils.isBlank(domain))
		{
			LOGGER.error("A domain name must be specified.");
			
			exitError();
		}

		this.getUtilConfig().setProperty(DOMAIN_PROP_NAME, domain);
		
		String inputDirPath = this.cli.hasOption(ConfigGenCliOption.INPUT_DIR.getOption()) ? 
			this.cli.getOptionValue(ConfigGenCliOption.INPUT_DIR.getOption()) : 
			this.getUtilConfig().getString(INPUT_DIR_PROP_NAME);
		
		if (StringUtils.isBlank(inputDirPath))
		{
			LOGGER.error("Input directory path must not be blank.");
			
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
		
		this.getUtilConfig().setProperty(INPUT_DIR_PROP_NAME, inputDir.toString());
		
		String outputFilePath = this.cli.hasOption(ConfigGenCliOption.OUTPUT_FILE.getOption()) ? 
			this.cli.getOptionValue(ConfigGenCliOption.OUTPUT_FILE.getOption()) : 
			this.getUtilConfig().getString(OUTPUT_FILE_PROP_NAME);
		
		if (StringUtils.isBlank(outputFilePath))
		{
			LOGGER.error("Output file path must not be blank.");
			
			exitError();
		}
		
		File outputFile = new File(outputFilePath);
		
		if (outputFile.exists() && !outputFile.isFile())
		{
			LOGGER.error("Output file path is not a directory: " + outputFile);
			
			exitError();
		}
		
		this.getUtilConfig().setProperty(OUTPUT_FILE_PROP_NAME, outputFile.toString());
	}
}