package gov.hhs.onc.dcdt.utils.config.generator;

import gov.hhs.onc.dcdt.config.ToolConfig;
import gov.hhs.onc.dcdt.utils.Utility;
import gov.hhs.onc.dcdt.utils.cli.UtilityCli;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class ConfigGen extends Utility<ConfigGenCliOption>
{
	private final static String UTIL_NAME = "configgen";
	
	private final static String OUTPUT_FILE_ARCHIVE_PATH_ATTRIB_NAME = ConfigGenCliOption.OUTPUT_FILE.getAttribName() + "ArchivePath";
	
	private final static String BASE_CONFIG_PROPS_NAME = UTIL_NAME + "-base-config";
	private final static String BASE_EMAIL_PROPS_NAME = UTIL_NAME + "-base-email";
	
	private final static Logger LOGGER = Logger.getLogger(ConfigGen.class);
	
	public ConfigGen()
	{
		super(UTIL_NAME, new UtilityCli<>(ConfigGenCliOption.class));
	}
	
	public static void main(String ... args)
	{
		new ConfigGen().execute(args);
	}
	
	@Override
	protected void execute(String ... args)
	{
		super.execute(args);
		
		CombinedConfiguration configAdditional = this.config.getAdditionalConfigSection();
		PropertiesConfiguration baseConfigProps = ToolConfig.getChildPropsConfig(configAdditional, BASE_CONFIG_PROPS_NAME), 
			baseEmailProps = ToolConfig.getChildPropsConfig(configAdditional, BASE_EMAIL_PROPS_NAME);

		File outputFile = new File(this.getConfig().getUtilString(ConfigGenCliOption.OUTPUT_FILE));
		String outputFileArchivePath = this.getConfig().getUtilString(OUTPUT_FILE_ARCHIVE_PATH_ATTRIB_NAME);
		
		if (!outputFile.exists())
		{
			try
			{
				FileUtils.touch(outputFile);
			}
			catch (IOException e)
			{
				LOGGER.error("Unable to create output file: " + outputFile, e);
			}
		}
		
		try (ZipOutputStream outputFileStream = new ZipOutputStream(new FileOutputStream(outputFile)))
		{
			if (!outputFile.exists())
			{
				FileUtils.touch(outputFile);
			}
			
			ByteArrayOutputStream outputPropsStream;
			
			for (PropertiesConfiguration baseProps : new PropertiesConfiguration[]{ baseConfigProps, baseEmailProps })
			{
				outputFileStream.putNextEntry(new ZipEntry(outputFileArchivePath + File.separatorChar + baseProps.getFile().getName()));
				outputPropsStream = new ByteArrayOutputStream();
				((PropertiesConfiguration)baseProps.interpolatedConfiguration()).save(outputPropsStream);
				outputFileStream.write(outputPropsStream.toByteArray());
				outputFileStream.closeEntry();
			}
			
			outputFileStream.finish();
			outputFileStream.close();
			
			LOGGER.info("Successfully wrote output file: " + outputFile);
		}
		catch (ConfigurationException | IOException e)
		{
			LOGGER.error("Unable to write output file: " + outputFile, e);
		}
	}

	@Override
	protected void processCmdLine()
	{
		super.processCmdLine();
		
		this.config.setUtilString(ConfigGenCliOption.DOMAIN);
		
		this.config.setUtilString(ConfigGenCliOption.OUTPUT_FILE);
		
		String outputFilePath = this.config.getUtilString(ConfigGenCliOption.OUTPUT_FILE);
		
		if (StringUtils.isBlank(outputFilePath))
		{
			LOGGER.error("Output file path must be specified.");
			
			exitError();
		}
		
		File outputFile = new File(outputFilePath);
		
		if (outputFile.exists() && !outputFile.isFile())
		{
			LOGGER.error("Output file path is not a file: " + outputFile);
			
			exitError();
		}
		
		this.config.setUtilString(ConfigGenCliOption.RESULT_MAIL_ADDRESS);
		
		if (StringUtils.isBlank(this.config.getUtilString(ConfigGenCliOption.RESULT_MAIL_ADDRESS)))
		{
			LOGGER.error("Result mail address must be specified.");
			
			exitError();
		}
		
		this.config.setUtilString(ConfigGenCliOption.RESULT_MAIL_PASSWORD);
		
		if (StringUtils.isBlank(this.config.getUtilString(ConfigGenCliOption.RESULT_MAIL_PASSWORD)))
		{
			LOGGER.error("Result mail password must be specified.");
			
			exitError();
		}
		
		this.config.setUtilString(ConfigGenCliOption.RESULT_MAIL_HOST);
		
		if (StringUtils.isBlank(this.config.getUtilString(ConfigGenCliOption.RESULT_MAIL_HOST)))
		{
			LOGGER.error("Result mail host must be specified.");
			
			exitError();
		}
		
		this.config.setUtilString(ConfigGenCliOption.RESULT_MAIL_PORT);
		
		String resultMailPort = this.config.getUtilString(ConfigGenCliOption.RESULT_MAIL_PORT);
		int resultMailPortNum;
		
		if (StringUtils.isBlank(resultMailPort))
		{
			LOGGER.error("Result mail port must be specified.");
			
			exitError();
		}
		
		if (!StringUtils.isNumeric(resultMailPort))
		{
			LOGGER.error("Result mail port must be numeric: " + resultMailPort);
			
			exitError();
		}
		else
		{
			resultMailPortNum = Integer.parseInt(resultMailPort);
			
			if (resultMailPortNum <= 0)
			{
				LOGGER.error("Result mail port must be a positive integer: " + resultMailPortNum);
				
				exitError();
			}
		}
		
		this.config.setUtilString(ConfigGenCliOption.RESULT_MAIL_USE_SSL);
	}
}