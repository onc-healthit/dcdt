package gov.hhs.onc.dcdt.crypto;

import java.util.Map;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.GeneralNames;

public interface CertificateName {
    public String getMail();

    public void setMail(String mail);

    public String getCommonName();

    public void setCommonName(String commonName);

    public String getCountry();

    public void setCountry(String country);

    public String getState();

    public void setState(String state);

    public String getLocality();

    public void setLocality(String locality);

    public String getOrganization();

    public void setOrganization(String organization);

    public String getOrganizationUnit();

    public void setOrganizationUnit(String organizationUnit);

    public GeneralNames getSubjAltNames();

    public X500Name toX500Name();

    public boolean hasMail();

    public String getName();

    public void setString(ASN1ObjectIdentifier oid, String value);

    public void setValue(ASN1ObjectIdentifier oid, ASN1Encodable value);

    public String getString(ASN1ObjectIdentifier oid);

    public ASN1Encodable getValue(ASN1ObjectIdentifier oid);

    public Map<ASN1ObjectIdentifier, ASN1Encodable> getRdnMap();

    public boolean hasSameRDNs(CertificateName certificateName);

    public boolean hasValue(ASN1ObjectIdentifier oid);
}
