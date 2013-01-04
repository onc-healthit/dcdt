package gov.hhs.onc.dcdt.utils.ldap;

import gov.hhs.onc.dcdt.utils.beans.LdapService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.directory.shared.ldap.model.constants.SchemaConstants;
import org.apache.directory.shared.ldap.model.cursor.EntryCursor;
import org.apache.directory.shared.ldap.model.entry.Attribute;
import org.apache.directory.shared.ldap.model.entry.Entry;
import org.apache.directory.shared.ldap.model.exception.LdapException;
import org.apache.directory.shared.ldap.model.filter.ExprNode;
import org.apache.directory.shared.ldap.model.message.ResultCodeEnum;
import org.apache.directory.shared.ldap.model.message.ResultResponse;
import org.apache.directory.shared.ldap.model.message.SearchScope;
import org.apache.directory.shared.ldap.model.name.Dn;
import org.apache.log4j.Logger;

public class LdapServiceWrapper
{
	private final static long CONNECTION_TIME_OUT = 5000;
	
	private final static Logger LOGGER = Logger.getLogger(LdapServiceWrapper.class);
	
	private LdapService service;
	private LdapConnection connection;
	
	public LdapServiceWrapper(LdapService service)
	{
		this.service = service;
	}
	
	public List<Dn> getBaseDns() throws UtilityLdapException
	{
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
	
	public boolean bind() throws UtilityLdapException
	{
		this.connection = new LdapNetworkConnection(this.service.toConnectionConfig());
		this.connection.setTimeOut(CONNECTION_TIME_OUT);
		
		try
		{
			this.connection.bind();
		}
		catch (IOException | LdapException e)
		{
			throw new UtilityLdapException("Unable to bind to LDAP service (" + this.service + ").", e);
		}
		
		LOGGER.debug("Successfully bound to LDAP service (" + this.service.toUrl() + "): bindDn=" + this.service.getBindDnName() + 
			(this.service.hashBindPass() ? ", bindPass=" + this.service.getBindPass() : ""));
		
		return this.connection.isAuthenticated();
	}
	
	public boolean unBind() throws UtilityLdapException
	{
		try
		{
			if (!this.isBound())
			{
				return true;
			}
			
			this.connection.unBind();
			
			LOGGER.debug("Successfully unbound from LDAP service (" + this.service.toUrl() + ").");
			
			return !this.connection.isAuthenticated();
		}
		catch (LdapException e)
		{
			throw new UtilityLdapException("Unable to unbind from LDAP service (" + this.service + ").", e);
		}
	}
	
	public boolean close() throws UtilityLdapException
	{
		try
		{
			if (!this.isConnected())
			{
				return true;
			}
			
			this.connection.close();
			
			LOGGER.debug("Successfully disconnected from LDAP service (" + this.service.toUrl() + ").");
			
			return this.isConnected();
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
		this.close();
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