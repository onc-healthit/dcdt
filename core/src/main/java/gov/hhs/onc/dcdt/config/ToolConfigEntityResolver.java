package gov.hhs.onc.dcdt.config;

import gov.hhs.onc.dcdt.reflect.resources.ResourceDiscoveryUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.discovery.Resource;
import org.apache.log4j.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ToolConfigEntityResolver implements EntityResolver
{
	private final static String SYSTEM_ID_PREFIX = "http://schema.direct-test.com/dcdt/";
	
	private final static String SCHEMA_FILE_EXT = ".xsd";
	private final static String SCHEMA_DIR_RESOURCE_PATH = "schema";
	
	private final static Logger LOGGER = Logger.getLogger(ToolConfigEntityResolver.class);
	
	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
	{
		if (systemId.startsWith(SYSTEM_ID_PREFIX))
		{
			String schemaResourcePath = systemId.substring(SYSTEM_ID_PREFIX.length());

			URL schemaUrl;
			
			if (((schemaUrl = resolveSchemaUrl(true, true, schemaResourcePath)) != null) || 
				((schemaUrl = resolveSchemaUrl(false, false, schemaResourcePath)) != null))
			{
				LOGGER.trace("Schema (publicId=" + publicId + ", systemId=" + systemId + ") resource URL resolved: " + 
					schemaUrl);
				
				return new InputSource(schemaUrl.openStream());
			}
			else
			{
				throw new FileNotFoundException("Unable to resolve schema (publicId=" + publicId + ", systemId=" + 
					systemId + ") resource URL.");
			}
		}
		
		return null;
	}
	
	private static URL resolveSchemaUrl(boolean inMetaInf, boolean isToolResource, String schemaResourcePath)
	{
		Resource schemaResource = ResourceDiscoveryUtils.findResource(inMetaInf, isToolResource, 
			SCHEMA_DIR_RESOURCE_PATH, schemaResourcePath + SCHEMA_FILE_EXT);
		
		return (schemaResource != null) ? schemaResource.getResource() : null;
	}
}