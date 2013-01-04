package gov.hhs.onc.dcdt.utils.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class UtilityConfigEntityResolver implements EntityResolver
{
	private final static String SYSTEM_ID_PREFIX = "http://schema.direct-test.com/utils/";
	
	private final static String SCHEMA_FILE_EXT = ".xsd";
	private final static String SCHEMA_DIR_PATH = "schema";
	
	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
	{	
		if (systemId.startsWith(SYSTEM_ID_PREFIX))
		{
			String schemaUrlStr = SCHEMA_DIR_PATH + File.separatorChar + systemId.substring(SYSTEM_ID_PREFIX.length()) + SCHEMA_FILE_EXT;
			URL schemaUrl = Thread.currentThread().getContextClassLoader().getResource(schemaUrlStr);
			
			if (schemaUrl != null)
			{
				return new InputSource(schemaUrl.openStream());
			}
			else
			{
				throw new FileNotFoundException("Schema file not found: " + schemaUrlStr);
			}
		}
		
		return null;
	}
}