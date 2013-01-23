package gov.hhs.onc.dcdt.utils.ldap;

import gov.hhs.onc.dcdt.utils.beans.LdapService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.apache.directory.api.ldap.model.ldif.ChangeType;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.apache.directory.api.ldap.model.message.ResultCodeEnum;
import org.apache.directory.api.ldap.model.message.ResultResponse;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.log4j.Logger;

public class LdapServiceWrapper
{
	private final static Logger LOGGER = Logger.getLogger(LdapServiceWrapper.class);
	
	private LdapService service;
	private LdapConnection connection;
	
	public LdapServiceWrapper(LdapService service)
	{
		this.service = service;
	}
	
	public static String[] toAttributeNameArray(Iterable<? extends Attribute> attributes)
	{
		List<String> attributeNames = new ArrayList<>();
		
		for (Attribute attribute : attributes)
		{
			attributeNames.add(attribute.getId());
		}
		
		return attributeNames.toArray(new String[attributeNames.size()]);
	}
	
	public boolean add(Entry... entries) throws UtilityLdapException
	{
		return this.add(Arrays.asList(entries));
	}
	
	public boolean add(Iterable<Entry> entries) throws UtilityLdapException
	{
		boolean modified = false;
		
		for (Entry entry : entries)
		{
			modified |= this.add(entry);
		}
		
		return modified;
	}
	
	public boolean add(Entry entry) throws UtilityLdapException
	{
		if (!this.isBound())
		{
			throw new UtilityLdapException("LDAP service wrapper is not bound to LDAP service (" + this.service.toUrl() + ").");
		}
		
		try
		{
			this.connection.add(entry);
		}
		catch (LdapException e)
		{
			throw new UtilityLdapException("Unable to add entry to LDAP service (" + this.service.toUrl() + "): " + entry, e);
		}
		
		LOGGER.debug("Added entry to LDAP service (" + this.service.toUrl() + "): " + entry);
		
		return true;
	}
	
	public boolean modify(LdifEntry... ldifEntries) throws UtilityLdapException
	{
		return this.modify(Arrays.asList(ldifEntries));
	}
	
	public boolean modify(Iterable<LdifEntry> ldifEntries) throws UtilityLdapException
	{
		boolean modified = false;
		
		for (LdifEntry ldifEntry : ldifEntries)
		{
			modified |= this.modify(ldifEntry);
		}
		
		return modified;
	}
	
	public boolean modify(LdifEntry ldifEntry) throws UtilityLdapException
	{
		if (!this.isBound())
		{
			throw new UtilityLdapException("LDAP service wrapper is not bound to LDAP service (" + this.service.toUrl() + ").");
		}
		
		if (ldifEntry.getChangeType() == ChangeType.Add)
		{
			return this.add(ldifEntry.getEntry());
		}

		Modification[] modifications = ldifEntry.getModificationArray();
		
		if (ArrayUtils.isEmpty(modifications))
		{
			LOGGER.trace("Skipped modification of LDIF entry data in LDAP service (" + this.service.toUrl() + "): " + ldifEntry);
			
			return false;
		}
		
		try
		{
			this.connection.modify(ldifEntry.getDn(), modifications);
		}
		catch (LdapException e)
		{
			throw new UtilityLdapException("Unable to modify LDIF entry data in LDAP service (" + this.service.toUrl() + "): " + ldifEntry, e);
		}
		
		LOGGER.debug("Modified LDIF entry data in LDAP service (" + this.service.toUrl() + "): " + ldifEntry);
		
		return true;
	}
	
	public List<Dn> getBaseDns() throws UtilityLdapException
	{
		if (!this.isBound())
		{
			throw new UtilityLdapException("LDAP service wrapper is not bound to LDAP service (" + this.service.toUrl() + ").");
		}
		
		List<Dn> baseDns = new ArrayList<>();
		Attribute attr = null;
		Dn baseDn;
		
		LOGGER.debug("Searching LDAP service (" + this.service.toUrl() + ") for base DN(s).");
		
		try
		{
			Iterator<Attribute> attrIterator = this.connection.getRootDse(SchemaConstants.NAMING_CONTEXTS_AT).getAttributes().iterator();
			
			while (attrIterator.hasNext())
			{
				baseDn = new Dn((attr = attrIterator.next()).getString());
				
				LOGGER.trace("Found base DN in LDAP service (" + this.service.toUrl() + "): " + baseDn.getName());
				
				baseDns.add(baseDn);
			}
		}
		catch (LdapException e)
		{
			throw new UtilityLdapException("Unable to create base DN from naming contexts attribute: " + attr, e);
		}
		
		LOGGER.debug("Found " + baseDns.size() + " base DN(s) in LDAP service: " + this.service.toUrl());
		
		return baseDns;
	}
	
	public List<Entry> search(ExprNode filterNode, String ... attributes) throws UtilityLdapException
	{
		return this.search(filterNode, SearchScope.ONELEVEL, attributes);
	}
	
	public List<Entry> search(ExprNode filterNode, SearchScope scope, String ... attributes) throws UtilityLdapException
	{
		return this.search(new ArrayList<Dn>(), filterNode, scope, attributes);
	}
	
	public List<Entry> search(List<Dn> baseDns, ExprNode filterNode, SearchScope scope, String ... attributes) throws UtilityLdapException
	{
		if (CollectionUtils.isEmpty(baseDns))
		{
			baseDns = this.getBaseDns();
		}
		
		List<Entry> entries = new ArrayList<>();
		
		for (Dn baseDn : baseDns)
		{
			entries.addAll(this.search(baseDn, filterNode, scope, attributes));
		}
		
		return entries;
	}
	
	public List<Entry> search(Dn baseDn, ExprNode filterNode, SearchScope scope, String ... attributes) throws UtilityLdapException
	{
		if (!this.isBound())
		{
			throw new UtilityLdapException("LDAP service wrapper is not bound to LDAP service (" + this.service.toUrl() + ").");
		}
		
		List<Entry> entries = new ArrayList<>();
		String filter = filterNode.toString();
		
		LOGGER.debug("Searching LDAP service: " + this.service.toUrl(baseDn, filter, scope, attributes));
		
		try
		{
			EntryCursor cursor = this.connection.search(baseDn, filter, scope, attributes);
			Entry entry;
			
			while (cursor.next())
			{
				entry = cursor.get();
				
				LOGGER.trace("Found entry match in LDAP service search (" + this.service.toUrl(baseDn, filter, scope, attributes)  + "): " + 
					entry);
				
				entries.add(entry);
			}
			
			cursor.close();
			
			ResultResponse searchResult = cursor.getSearchResultDone();
			ResultCodeEnum searchResultCode;
			
			if ((searchResult != null) && ((searchResultCode = searchResult.getLdapResult().getResultCode()) != ResultCodeEnum.SUCCESS))
			{
				throw new UtilityLdapException("LDAP service search (" + this.service.toUrl(baseDn, filter, scope, attributes) + 
					") was not successful: resultCode=" + searchResultCode.getResultCode() + "=" + searchResultCode.name() + 
					", resultMsg=" + searchResultCode.getMessage());
			}
		}
		catch (Exception e)
		{
			throw new UtilityLdapException("Unable to search LDAP service: " + this.service.toUrl(baseDn, filter, scope, attributes), e);
		}
		
		LOGGER.debug("Found " + entries.size() + " entry match(es) in LDAP service search (" + this.service.toUrl(baseDn, filter, scope, attributes) + 
			").");
		
		return entries;
	}
	
	public boolean connect() throws UtilityLdapException
	{
		if (this.isConnected())
		{
			this.disconnect();
		}
		
		this.connection = new LdapNetworkConnection(this.service.toConnectionConfig());
		
		try
		{
			return this.connection.connect();
		}
		catch (IOException | LdapException e)
		{
			throw new UtilityLdapException("Unable to connect to LDAP service (" + this.service + ").", e);
		}
	}
	
	public boolean bind() throws UtilityLdapException
	{
		if (!this.isConnected())
		{
			this.connect();
		}
		else if (this.isBound())
		{
			this.unBind();
		}
		
		try
		{
			if (this.service.isAnonymousBind())
			{
				this.connection.anonymousBind();
			}
			else
			{
				this.connection.bind();
			}
		}
		catch (IOException | LdapException e)
		{
			throw new UtilityLdapException("Unable to bind" + (this.service.isAnonymousBind() ? " anonymously" : "") + " to LDAP service (" + 
				this.service.toUrl() + ")" + (this.service.isAnonymousBind() ? "." : ": bindDn=" + this.service.getBindDnName() + 
				(this.service.hashBindPass() ? ", bindPass=" + this.service.getBindPass() : "")), e);
		}
		
		LOGGER.debug("Successfully bound" + (this.service.isAnonymousBind() ? " anonymously" : "") + " to LDAP service (" + this.service.toUrl() + 
			")" + (this.service.isAnonymousBind() ? "." : ": bindDn=" + this.service.getBindDnName() + (this.service.hashBindPass() ? ", bindPass=" + 
			this.service.getBindPass() : "")));
		
		return this.isBound();
	}
	
	public boolean unBind() throws UtilityLdapException
	{
		if (!this.isBound())
		{
			return true;
		}
		
		try
		{
			this.connection.unBind();
			
			LOGGER.debug("Successfully unbound from LDAP service (" + this.service.toUrl() + ").");
			
			return !this.isBound();
		}
		catch (LdapException e)
		{
			throw new UtilityLdapException("Unable to unbind from LDAP service (" + this.service + ").", e);
		}
	}
	
	public boolean disconnect() throws UtilityLdapException
	{
		if (!this.isConnected())
		{
			return true;
		}
		else if (this.isBound())
		{
			this.unBind();
		}
		
		try
		{
			this.connection.close();
			
			LOGGER.debug("Successfully disconnected from LDAP service (" + this.service.toUrl() + ").");
			
			return !this.isConnected();
		}
		catch (IOException e)
		{
			throw new UtilityLdapException("Unable to close connection to LDAP service (" + this.service + ").", e);
		}
	}
	
	public boolean isBound()
	{
		return this.isConnected() && this.connection.isAuthenticated();
	}
	
	public boolean isConnected()
	{
		return (this.connection != null) && this.connection.isConnected();
	}

	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		
		this.unBind();
		this.disconnect();
	}
	
	public LdapConnection getConnection()
	{
		return this.connection;
	}

	public LdapService getService()
	{
		return this.service;
	}
}