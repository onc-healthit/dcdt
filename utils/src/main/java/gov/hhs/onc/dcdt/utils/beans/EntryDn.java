package gov.hhs.onc.dcdt.utils.beans;

import gov.hhs.onc.dcdt.annotations.ConfigBean;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;

@ConfigBean("entries/entry/dn")
public class EntryDn
{
	public final static String MAIL_ADDRESS_DELIM = "@";
	
	private String mail;
	private String commonName;
	private String country;
	private String state;
	private String locality;
	private String organization;
	private String organizationUnit;
	
	public GeneralNames getSubjAltNames()
	{
		if (this.hasMail())
		{
			String mail = this.getMail();
			
			return new GeneralNames(new GeneralName(mail.contains(MAIL_ADDRESS_DELIM) ? 
				GeneralName.rfc822Name : GeneralName.dNSName, mail));
		}
		
		return null;
	}
	
	public X500Name toX500Name()
	{
		List<RDN> rdnList = new ArrayList<>();
		
		if (this.hasMail())
		{
			rdnList.add(getEntryRdn(BCStyle.EmailAddress, this.getMail()));
		}
		
		if (this.hasCommonName())
		{
			rdnList.add(getEntryRdn(BCStyle.CN, this.getCommonName()));
		}
		
		if (this.hasCountry())
		{
			rdnList.add(getEntryRdn(BCStyle.C, this.getCountry()));
		}
		
		if (this.hasState())
		{
			rdnList.add(getEntryRdn(BCStyle.ST, this.getState()));
		}
		
		if (this.hasLocality())
		{
			rdnList.add(getEntryRdn(BCStyle.L, this.getLocality()));
		}
		
		if (this.hasOrganization())
		{
			rdnList.add(getEntryRdn(BCStyle.O, this.getOrganization()));
		}
		
		if (this.hasOrganizationUnit())
		{
			rdnList.add(getEntryRdn(BCStyle.OU, this.getOrganizationUnit()));
		}
		
		return new X500Name(BCStyle.INSTANCE, rdnList.toArray(new RDN[rdnList.size()]));
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof EntryDn) && this.toX500Name().equals(((EntryDn)obj).toX500Name());
	}

	@Override
	public String toString()
	{
		return this.toX500Name().toString();
	}
	
	private static RDN getEntryRdn(ASN1ObjectIdentifier oid, String value)
	{
		return new RDN(oid, BCStyle.INSTANCE.stringToValue(oid, value));
	}

	public boolean hasCommonName()
	{
		return !StringUtils.isBlank(this.getCommonName());
	}
	
	public String getCommonName()
	{
		return this.commonName;
	}

	public void setCommonName(String commonName)
	{
		this.commonName = commonName;
	}

	public boolean hasCountry()
	{
		return !StringUtils.isBlank(this.getCountry());
	}
	
	public String getCountry()
	{
		return this.country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public boolean hasLocality()
	{
		return !StringUtils.isBlank(this.getLocality());
	}
	
	public String getLocality()
	{
		return this.locality;
	}

	public void setLocality(String locality)
	{
		this.locality = locality;
	}

	public boolean hasMail()
	{
		return !StringUtils.isBlank(this.getMail());
	}
	
	public String getMail()
	{
		return this.mail;
	}

	public void setMail(String mail)
	{
		this.mail = mail;
	}

	public boolean hasOrganization()
	{
		return !StringUtils.isBlank(this.getOrganization());
	}
	
	public String getOrganization()
	{
		return this.organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public boolean hasOrganizationUnit()
	{
		return !StringUtils.isBlank(this.getOrganizationUnit());
	}
	
	public String getOrganizationUnit()
	{
		return this.organizationUnit;
	}

	public void setOrganizationUnit(String organizationUnit)
	{
		this.organizationUnit = organizationUnit;
	}

	public boolean hasState()
	{
		return !StringUtils.isBlank(this.getState());
	}
	
	public String getState()
	{
		return this.state;
	}

	public void setState(String state)
	{
		this.state = state;
	}
}