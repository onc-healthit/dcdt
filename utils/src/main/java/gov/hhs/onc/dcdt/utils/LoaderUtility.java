package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.beans.entry.Entry;
import gov.hhs.onc.dcdt.data.entry.EntryException;
import gov.hhs.onc.dcdt.data.entry.EntryLoader;
import gov.hhs.onc.dcdt.utils.cli.CliOption;
import gov.hhs.onc.dcdt.utils.cli.UtilityCli;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public abstract class LoaderUtility<T extends Enum<T> & CliOption> extends Utility<T>
{
	protected final static String INPUT_CONTAINER_PATH_ATTRIB_NAME = "inputContainerPath";
	
	private final static Logger LOGGER = Logger.getLogger(LoaderUtility.class);
	
	protected Map<String, Entry> entryMap;
	
	protected LoaderUtility(String name, UtilityCli<T> cli)
	{
		super(name, cli);
	}
	
	protected void loadEntries(String inputPath)
	{
		String inputContainerPath = this.config.getUtilString(INPUT_CONTAINER_PATH_ATTRIB_NAME);
		
		EntryLoader entryLoader = new EntryLoader();
		
		this.entryMap = new HashMap<>();
		
		for (Entry entry : this.config.getEntries())
		{
			try
			{
				entryLoader.loadEntry(entry, inputPath, inputContainerPath);
			}
			catch (EntryException e)
			{
				LOGGER.error("Unable to load entry: " + entry, e);
				
				exitError();
			}
			
			LOGGER.trace("Loaded entry: " + entry);
			
			this.entryMap.put(entry.getName(), entry);
		}
	}
}