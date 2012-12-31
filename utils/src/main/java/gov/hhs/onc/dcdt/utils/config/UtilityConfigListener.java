package gov.hhs.onc.dcdt.utils.config;

import gov.hhs.onc.dcdt.utils.Utility;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.event.ConfigurationErrorEvent;
import org.apache.commons.configuration.event.ConfigurationErrorListener;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;
import org.apache.log4j.Logger;

public class UtilityConfigListener implements ConfigurationListener, ConfigurationErrorListener
{
	private final static Logger LOGGER = Logger.getLogger(Configuration.class);
	
	private Utility util;
	
	public UtilityConfigListener(Utility util)
	{
		this.util = util;
	}
	
	@Override
	public void configurationError(ConfigurationErrorEvent event)
	{
		LOGGER.error("Utility (name=" + this.util.getName() + ") configuration error (type=" + 
			event.getType() + "): source=" + event.getSource() + ", propName=" + 
			event.getPropertyName() + ", propValue=" + event.getPropertyValue(), event.getCause());
	}

	@Override
	public void configurationChanged(ConfigurationEvent event)
	{
		switch (event.getType())
		{
			// TODO: implement change type specific logging
			
			default:
				LOGGER.trace("Utility (name=" + this.util.getName() + ") configuration changed (type=" + 
					event.getType() + "): source=" + event.getSource() + ", propName=" + event.getPropertyName() + 
					", propValue=" + event.getPropertyValue());
				break;
		}
	}
}