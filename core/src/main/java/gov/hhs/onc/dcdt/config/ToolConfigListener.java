package gov.hhs.onc.dcdt.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.event.ConfigurationErrorEvent;
import org.apache.commons.configuration.event.ConfigurationErrorListener;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;
import org.apache.log4j.Logger;

public class ToolConfigListener implements ConfigurationListener, ConfigurationErrorListener
{
	private final static Logger LOGGER = Logger.getLogger(Configuration.class);
	
	@Override
	public void configurationError(ConfigurationErrorEvent event)
	{
		LOGGER.error("Configuration error (type=" + event.getType() + "): source=" + 
			event.getSource() + ", propName=" + event.getPropertyName() + ", propValue=" + 
			event.getPropertyValue(), event.getCause());
	}

	@Override
	public void configurationChanged(ConfigurationEvent event)
	{
		switch (event.getType())
		{
			// TODO: implement change type specific logging
			
			default:
				LOGGER.trace("Configuration changed (type=" + event.getType() + "): source=" + 
					event.getSource() + ", propName=" + event.getPropertyName() + ", propValue=" + 
					event.getPropertyValue());
				break;
		}
	}
}