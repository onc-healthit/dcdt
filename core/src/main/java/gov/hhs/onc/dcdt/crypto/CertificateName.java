package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.lang.IterableUtils;
import gov.hhs.onc.dcdt.lang.builder.ComparatorBuilder;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;

public class CertificateName implements Principal
{
	private static class ASN1ObjectIdentifierComparator extends ComparatorBuilder<ASN1ObjectIdentifier>
	{
		private final static Map<ASN1ObjectIdentifier, Integer> OID_ORDER_MAP = new LinkedHashMap<>();
		
		static
		{
			OID_ORDER_MAP.put(BCStyle.EmailAddress, OID_ORDER_MAP.size());
			OID_ORDER_MAP.put(BCStyle.CN, OID_ORDER_MAP.size());
			OID_ORDER_MAP.put(BCStyle.C, OID_ORDER_MAP.size());
			OID_ORDER_MAP.put(BCStyle.ST, OID_ORDER_MAP.size());
			OID_ORDER_MAP.put(BCStyle.L, OID_ORDER_MAP.size());
			OID_ORDER_MAP.put(BCStyle.O, OID_ORDER_MAP.size());
			OID_ORDER_MAP.put(BCStyle.OU, OID_ORDER_MAP.size());
		}
		
		public ASN1ObjectIdentifierComparator()
		{
			super(ASN1ObjectIdentifier.class);
		}

		@Override
		protected boolean appendObjsCompare(ASN1ObjectIdentifier obj1, ASN1ObjectIdentifier obj2)
		{
			return this.appendObjCompare(Integer.class, OID_ORDER_MAP.get(obj1), OID_ORDER_MAP.get(obj2)) || 
				this.appendObjCompare(String.class, obj1.getId(), obj2.getId());
		}
	}
	
	private SortedMap<ASN1ObjectIdentifier, ASN1Encodable> rdnsMap = 
		new TreeMap<>(new ASN1ObjectIdentifierComparator());
	
	public CertificateName(X500Name name)
	{
		this((name != null) ? name.getRDNs() : null);
	}
	
	public CertificateName(RDN ... rdns)
	{
		this(IterableUtils.asIterable(rdns));
	}
	
	public CertificateName(Iterable<RDN> rdns)
	{
		this.setValues(rdns);
	}
	
	public static RDN createRdn(ASN1ObjectIdentifier oid, String value)
	{
		return createRdn(oid, stringToValue(oid, value));
	}
	
	public static RDN createRdn(ASN1ObjectIdentifier oid, ASN1Encodable value)
	{
		return (value != null) ? new RDN(oid, value) : null;
	}
	
	public static ASN1Encodable stringToValue(ASN1ObjectIdentifier oid, String value)
	{
		return (value != null) ? BCStyle.INSTANCE.stringToValue(oid, value) : null;
	}
	
	public static String valueToString(ASN1Encodable value)
	{
		return (value != null) ? IETFUtils.valueToString(value) : null;
	}
	
	//<editor-fold desc="Certificate RDN accessor methods">
	public String getMail()
	{
		return this.getString(BCStyle.EmailAddress);
	}
	
	public void setMail(String mail)
	{
		this.setString(BCStyle.EmailAddress, mail);
	}
	
	public String getCommonName()
	{
		return this.getString(BCStyle.CN);
	}
	
	public void setCommonName(String commonName)
	{
		this.setString(BCStyle.CN, commonName);
	}
	
	public String getCountry()
	{
		return this.getString(BCStyle.C);
	}
	
	public void setCountry(String country)
	{
		this.setString(BCStyle.C, country);
	}
	
	public String getState()
	{
		return this.getString(BCStyle.ST);
	}
	
	public void setState(String state)
	{
		this.setString(BCStyle.ST, state);
	}
	
	public String getLocality()
	{
		return this.getString(BCStyle.L);
	}
	
	public void setLocality(String locality)
	{
		this.setString(BCStyle.L, locality);
	}
	
	public String getOrganization()
	{
		return this.getString(BCStyle.O);
	}
	
	public void setOrganization(String organization)
	{
		this.setString(BCStyle.O, organization);
	}
	
	public String getOrganizationUnit()
	{
		return this.getString(BCStyle.OU);
	}
	
	public void setOrganizationUnit(String organizationUnit)
	{
		this.setString(BCStyle.OU, organizationUnit);
	}
	//</editor-fold>

	//<editor-fold desc="RDN accessor methods">
	public boolean hasString(ASN1ObjectIdentifier oid)
	{
		return !StringUtils.isBlank(this.getString(oid));
	}
	
	public boolean hasValue(ASN1ObjectIdentifier oid)
	{
		return this.rdnsMap.containsKey(oid) && (this.rdnsMap.get(oid) != null);
	}
	
	public void setString(ASN1ObjectIdentifier oid, String value)
	{
		this.setValue(oid, stringToValue(oid, value));
	}
	
	public void setValue(ASN1ObjectIdentifier oid, ASN1Encodable value)
	{
		this.rdnsMap.put(oid, value);
	}
	
	public void setValues(RDN ... rdns)
	{
		this.setValues(IterableUtils.asIterable(rdns));
	}
	
	public void setValues(Iterable<RDN> rdns)
	{
		ASN1Encodable value;
		
		for (RDN rdn : rdns)
		{
			for (AttributeTypeAndValue attrValue : rdn.getTypesAndValues())
			{
				value = attrValue.getValue();
				
				if (value != null)
				{
					this.rdnsMap.put(attrValue.getType(), value);
				}
			}
		}
	}
	
	public String getString(ASN1ObjectIdentifier oid)
	{
		return valueToString(this.getValue(oid));
	}
	
	public ASN1Encodable getValue(ASN1ObjectIdentifier oid)
	{
		return this.rdnsMap.get(oid);
	}
	//</editor-fold>
	
	public X500Name toX500Name()
	{
		X500NameBuilder x500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
		
		for (ASN1ObjectIdentifier oid : this.rdnsMap.keySet())
		{
			x500NameBuilder.addRDN(oid, this.rdnsMap.get(oid));
		}
		
		return x500NameBuilder.build();
	}
	
	@Override
	public String getName()
	{
		return this.toX500Name().toString();
	}

	@Override
	public String toString()
	{
		return this.getName();
	}
}