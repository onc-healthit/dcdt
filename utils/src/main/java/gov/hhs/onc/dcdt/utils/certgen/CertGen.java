package gov.hhs.onc.dcdt.utils.certgen;

import gov.hhs.onc.dcdt.utils.BeanAttrib;
import gov.hhs.onc.dcdt.utils.Utility;
import gov.hhs.onc.dcdt.utils.beans.Entry;
import gov.hhs.onc.dcdt.utils.certgen.cli.CertGenCliOption;
import gov.hhs.onc.dcdt.utils.cli.UtilityCli;
import gov.hhs.onc.dcdt.utils.entry.EntryBuilder;
import gov.hhs.onc.dcdt.utils.entry.EntryException;
import java.io.File;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class CertGen extends Utility
{
	private final static String UTIL_NAME = "certgen";
	
	private final static String DOMAIN_PROP_NAME = "domain";
	private final static String OUTPUT_DIR_PROP_NAME = "outputDir";
	
	private final static Logger LOGGER = Logger.getLogger(CertGen.class);
	
	private EntryBuilder entryBuilder;
	
	public CertGen()
	{
		super(UTIL_NAME, new UtilityCli(CertGenCliOption.class));
	}
	
	public static void main(String ... args)
	{
		new CertGen().execute(args);
	}
	
	@Override
	protected void execute(String ... args)
	{
		super.execute(args);
		
		Entry caEntry = this.utilBeans.createBean(Entry.class, new BeanAttrib("id", "ca"));
		caEntry.setIssuer(caEntry);
		
		try
		{
			this.entryBuilder.generateCa(caEntry);
			
			LOGGER.info("Successfully built Certificate Authority (CA) entry: " + caEntry);
		}
		catch (EntryException e)
		{
			LOGGER.error("Unable to build Certificate Authority (CA) entry: " + caEntry, e);
			
			exitError();
		}
		
		for (String leafId : this.getConfig().getStringArray("entries/entry[@id!=\"ca\"]/@id"))
		{
			Entry leafEntry = this.utilBeans.createBean(Entry.class, new BeanAttrib("id", leafId));
			leafEntry.setIssuer(caEntry);
			
			try
			{
				this.entryBuilder.generateLeaf(leafEntry);
				
				LOGGER.info("Successfully built leaf entry: " + leafEntry);
			}
			catch (EntryException e)
			{
				LOGGER.error("Unable to build leaf entry: " + leafEntry, e);
				
				exitError();
			}
		}
	}

	@Override
	protected void processCmdLine()
	{
		super.processCmdLine();
		
		String domain = this.cli.getOptionValue(CertGenCliOption.DOMAIN.getOption());
		
		if (StringUtils.isBlank(domain))
		{
			LOGGER.error("A domain name must be specified.");
			
			exitError();
		}

		this.getUtilConfig().setProperty(DOMAIN_PROP_NAME, domain);
		
		String outputDirPath = this.cli.hasOption(CertGenCliOption.OUTPUT_DIR.getOption()) ? 
			this.cli.getOptionValue(CertGenCliOption.OUTPUT_DIR.getOption()) : 
			this.getUtilConfig().getString(OUTPUT_DIR_PROP_NAME);
		
		if (StringUtils.isBlank(outputDirPath))
		{
			LOGGER.error("Output directory path must not be blank.");
			
			exitError();
		}
		
		File outputDir = new File(outputDirPath);
		
		if (!outputDir.exists())
		{
			if (!outputDir.mkdirs())
			{
				LOGGER.error("Unable to create output directory: " + outputDir);
				
				exitError();
			}
		}
		else if (!outputDir.isDirectory())
		{
			LOGGER.error("Output directory path is not a directory: " + outputDir);
			
			exitError();
		}
		
		this.getUtilConfig().setProperty(OUTPUT_DIR_PROP_NAME, outputDir.toString());
	}

	@Override
	protected void init()
	{
		super.init();
		
		this.entryBuilder = new EntryBuilder();
	}
}