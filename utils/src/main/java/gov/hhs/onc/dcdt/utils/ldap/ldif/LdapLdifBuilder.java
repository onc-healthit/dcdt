package gov.hhs.onc.dcdt.utils.ldap.ldif;

import gov.hhs.onc.dcdt.utils.Utility;
import gov.hhs.onc.dcdt.utils.ldap.LdapServiceWrapper;
import gov.hhs.onc.dcdt.utils.ldap.UtilityLdapException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.directory.shared.ldap.model.entry.Attribute;
import org.apache.directory.shared.ldap.model.entry.Entry;
import org.apache.directory.shared.ldap.model.entry.ModificationOperation;
import org.apache.directory.shared.ldap.model.exception.LdapException;
import org.apache.directory.shared.ldap.model.filter.EqualityNode;
import org.apache.directory.shared.ldap.model.ldif.ChangeType;
import org.apache.directory.shared.ldap.model.ldif.LdifEntry;
import org.apache.directory.shared.ldap.model.ldif.LdifReader;
import org.apache.directory.shared.ldap.model.message.SearchScope;
import org.apache.directory.shared.ldap.model.name.Dn;
import org.apache.directory.shared.ldap.model.name.Rdn;
import org.apache.log4j.Logger;

public class LdapLdifBuilder
{
	private final static Logger LOGGER = Logger.getLogger(LdapLdifBuilder.class);
	
	private Utility util;
	private LdapServiceWrapper serviceWrapper;
	
	public LdapLdifBuilder(Utility util, LdapServiceWrapper serviceWrapper)
	{
		this.util = util;
		this.serviceWrapper = serviceWrapper;
	}
	
	public void parseEntries(List<LdifEntry> ldifEntries) throws UtilityLdapException
	{
		for (LdifEntry ldifEntry : ldifEntries)
		{
			this.parseEntry(ldifEntry);
		}
	}
	
	public void parseEntry(LdifEntry ldifEntry) throws UtilityLdapException
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
	
	public List<LdifEntry> readEntries(String resourcePath) throws UtilityLdapException
	{
		return this.readEntries(this.util.getResourceAsStream(resourcePath));
	}
	
	public List<LdifEntry> readEntries(InputStream inStream) throws UtilityLdapException
	{
		List<LdifEntry> ldifEntries = new ArrayList<>();
		
		try
		{
			String entriesStr = this.util.getConfig().getConfig().getSubstitutor().replace(IOUtils.toString(inStream));
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
			// TODO: finish exception
			throw new UtilityLdapException(e);
		}
		
		return ldifEntries;
	}
}