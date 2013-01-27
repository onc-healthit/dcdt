package gov.hhs.onc.dcdt.utils.beans;

import gov.hhs.onc.dcdt.annotations.ConfigBean;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@ConfigBean("entries/entry/destination")
public class EntryDestination
{
	private EntryDestinationType type;
	private String ldapServiceName;
	
	public boolean isAnchor()
	{
		return this.type == EntryDestinationType.ANCHOR;
	}
	
	public boolean isDns()
	{
		return this.type == EntryDestinationType.DNS;
	}
	
	public boolean isLdap()
	{
		return this.type == EntryDestinationType.LDAP;
	}
	
	public boolean hasLdapServiceName()
	{
		return !StringUtils.isBlank(this.ldapServiceName);
	}
	
	public boolean hasType()
	{
		return this.getType() != EntryDestinationType.NONE;
	}
	
	public String getTypeName()
	{
		return this.getType().getType();
	}
	
	public void setTypeName(String typeName)
	{
		this.type = EntryDestinationType.fromType(typeName);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("type=");
		builder.append(this.getTypeName());
		
		if (this.hasLdapServiceName())
		{
			builder.append(", ldapServiceName=");
			builder.append(this.ldapServiceName);
		}
		
		return builder.toString();
	}

	public String getLdapServiceName()
	{
		return this.ldapServiceName;
	}

	public void setLdapServiceName(String ldapServiceName)
	{
		this.ldapServiceName = ldapServiceName;
	}

	public EntryDestinationType getType()
	{
		return ObjectUtils.defaultIfNull(this.type, EntryDestinationType.NONE);
	}

	public void setType(EntryDestinationType type)
	{
		this.type = type;
	}
}