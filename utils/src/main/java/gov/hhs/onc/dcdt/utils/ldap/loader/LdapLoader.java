package gov.hhs.onc.dcdt.utils.ldap.loader;

import gov.hhs.onc.dcdt.utils.Utility;
import gov.hhs.onc.dcdt.utils.beans.Entry;
import gov.hhs.onc.dcdt.utils.cli.UtilityCli;
import gov.hhs.onc.dcdt.utils.config.UtilityConfig;
import gov.hhs.onc.dcdt.utils.entry.EntryException;
import gov.hhs.onc.dcdt.utils.entry.EntryLoader;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class LdapLoader extends Utility<LdapLoaderCliOption>
{
	private final static String UTIL_NAME = "ldaploader";
	
	private final static String INPUT_CONTAINER_PATH_ATTRIB_NAME = "inputContainerPath";
	
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

		String inputPath = this.config.getUtilString(LdapLoaderCliOption.INPUT_PATH), 
			inputContainerPath = this.config.getUtilString(UtilityConfig.XPATH_ATTRIB_KEY_PREFIX + 
			INPUT_CONTAINER_PATH_ATTRIB_NAME);
		
		EntryLoader entryLoader = new EntryLoader();
		Map<String, Entry> entryMap = new HashMap<>();
		
		for (Entry entry : this.config.getEntries())
		{
			try
			{
				entryLoader.loadEntry(entry, inputPath, inputContainerPath);
			}
			catch (EntryException e)
			{
				// TODO: finish exception
				LOGGER.error(e);
				
				exitError();
			}
			
			LOGGER.trace("Loaded entry: " + entry);
			
			entryMap.put(entry.getName(), entry);
		}
		
		// TODO: iterate through LDIF entries and set userCertificate attribute values
	}
	
	@Override
	protected void processCmdLine()
	{
		super.processCmdLine();
		
		this.config.setUtilString(LdapLoaderCliOption.BIND_DN_NAME);
		this.config.setUtilString(LdapLoaderCliOption.BIND_PASS);
		this.config.setUtilString(LdapLoaderCliOption.DOMAIN);
		this.config.setUtilString(LdapLoaderCliOption.LOAD_DN_NAME);
		
		this.config.setUtilString(LdapLoaderCliOption.INPUT_PATH);
		
		File inputPath = new File(this.config.getUtilString(LdapLoaderCliOption.INPUT_PATH));
		
		if (!inputPath.exists())
		{
			LOGGER.error("Input path does not exist: " + inputPath);
			
			exitError();
		}
	}
}