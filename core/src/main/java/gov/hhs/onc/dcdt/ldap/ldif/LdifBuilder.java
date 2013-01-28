package gov.hhs.onc.dcdt.ldap.ldif;

import gov.hhs.onc.dcdt.config.ToolConfig;
import gov.hhs.onc.dcdt.ldap.LdapServiceWrapper;
import gov.hhs.onc.dcdt.ldap.ToolLdapException;
import gov.hhs.onc.dcdt.reflect.resources.ResourceDiscoveryUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.filter.EqualityNode;
import org.apache.directory.api.ldap.model.ldif.ChangeType;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.apache.directory.api.ldap.model.ldif.LdifReader;
import org.apache.directory.api.ldap.model.ldif.LdifUtils;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.name.Rdn;
import org.apache.log4j.Logger;

public class LdifBuilder
{
	public final static String LDIF_FILE_EXT = ".ldif";
	
	private final static String LDIF_FILE_HEADER = "version: 1\n\n";
	
	private final static Logger LOGGER = Logger.getLogger(LdifBuilder.class);
	
	private ToolConfig config;
	private LdapServiceWrapper serviceWrapper;
	
	public LdifBuilder(ToolConfig config, LdapServiceWrapper serviceWrapper)
	{
		this.config = config;
		this.serviceWrapper = serviceWrapper;
	}
	
	public static void setAttribute(LdifEntry ldifEntry, String attrId, Object attrValue) throws ToolLdapException
	{
		if (ldifEntry.getEntry().containsAttribute(attrId))
		{
			ldifEntry.removeAttribute(attrId);
		}
		
		try
		{
			ldifEntry.putAttribute(attrId, attrValue);
		}
		catch (LdapException e)
		{
			throw new ToolLdapException("Unable to put attribute (id=" + attrId + ") value (" + attrValue + ") into LDIF entry: " + 
				ldifEntry, e);
		}
	}
	
	public void parseEntries(List<LdifEntry> ldifEntries) throws ToolLdapException
	{
		for (LdifEntry ldifEntry : ldifEntries)
		{
			this.parseEntry(ldifEntry);
		}
	}
	
	public void parseEntry(LdifEntry ldifEntry) throws ToolLdapException
	{
		ldifEntry.setChangeType(ChangeType.Add);
		
		Dn ldifEntryDn = ldifEntry.getDn();
		Rdn ldifEntryFirstRdn = ldifEntryDn.getRdn(0);
		Entry entry = ldifEntry.getEntry(), existingEntry;
		List<Entry> existingEntries = this.serviceWrapper.search(
			ldifEntryDn.getParent(), new EqualityNode<>(ldifEntryFirstRdn.getType(), ldifEntryFirstRdn.getValue()), 
			SearchScope.SUBTREE, LdapServiceWrapper.toAttributeNameArray(entry));
		
		existingEntry = !existingEntries.isEmpty() ? existingEntries.get(0) : null;
		
		if (existingEntry != null)
		{
			ldifEntry.setChangeType(ChangeType.Modify);
			
			for (Attribute attribute : entry)
			{
				if (existingEntry.contains(attribute))
				{
					if (!Arrays.equals(attribute.get().getBytes(), existingEntry.get(attribute.getId()).get().getBytes()))
					{
						ldifEntry.addModification(ModificationOperation.REPLACE_ATTRIBUTE, attribute);
					}
				}
				else
				{
					ldifEntry.addModification(ModificationOperation.ADD_ATTRIBUTE, attribute);
				}
			}
		}
		
		LOGGER.trace("Parsed LDIF entry: " + ldifEntry);
	}
	
	public List<LdifEntry> readEntries(String resourcePath) throws ToolLdapException
	{
		return this.readEntries(ResourceDiscoveryUtils.findResource(false, false, resourcePath)
			.getResourceAsStream());
	}
	
	public List<LdifEntry> readEntries(InputStream inStream) throws ToolLdapException
	{
		List<LdifEntry> ldifEntries = new ArrayList<>();
		
		try
		{
			String entriesStr = this.config.getConfig().getSubstitutor().replace(IOUtils.toString(inStream));
			LdifReader reader = new LdifReader(new StringReader(entriesStr));
			LdifEntry ldifEntry;
			
			while (reader.hasNext())
			{
				ldifEntry = reader.next();
				
				LOGGER.trace("Read LDIF entry from input stream: " + ldifEntry);
				
				ldifEntries.add(ldifEntry);
			}
		}
		catch (IOException | LdapException e)
		{
			throw new ToolLdapException("Unable to read LDIF entry from input stream.", e);
		}
		
		return ldifEntries;
	}
	
	public void writeEntries(List<LdifEntry> ldifEntries, OutputStream outStream) throws ToolLdapException
	{
		StringBuilder ldifEntriesBuilder = new StringBuilder();
		
		try
		{
			for (LdifEntry ldifEntry : ldifEntries)
			{
				if (ldifEntriesBuilder.length() != 0)
				{
					ldifEntriesBuilder.append("\n");
				}
				
				ldifEntriesBuilder.append(LdifUtils.convertToLdif(ldifEntry));
			}
			
			IOUtils.write(ldifEntriesBuilder.toString(), outStream);
		}
		catch (IOException | LdapException e)
		{
			throw new ToolLdapException("Unable to write LDIF entries to output stream.", e);
		}
	}
}