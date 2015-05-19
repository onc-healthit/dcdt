package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.security.Principal;
import java.util.Set;
import java.util.SortedMap;
import javax.annotation.Nullable;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;

public interface CertificateName extends Principal, ToolBean {
    public X500Principal toX500Principal();

    public X500Name toX500Name();

    @Override
    public String getName();

    @Override
    public boolean equals(Object obj);

    @Override
    public int hashCode();

    @Override
    public String toString();

    public boolean hasCommonName();

    @Nullable
    public String getCommonName();

    public void setCommonName(@Nullable String commonName);

    public boolean hasMailAddress();

    @Nullable
    public MailAddress getMailAddress();

    public void setMailAddress(@Nullable MailAddress mailAddr);

    public boolean hasAltName(CertificateAltNameType altNameType);

    @Nullable
    public Set<GeneralName> getAltNames(CertificateAltNameType altNameType);

    public void setAltNames(Set<GeneralName> altName);

    public boolean hasAltNames();

    @Nullable
    public GeneralNames getAltNames();

    public void setAltNames(@Nullable GeneralNames altNames);

    public boolean hasAttribute(ASN1ObjectIdentifier attrOid);

    @Nullable
    public String getAttributeValueString(ASN1ObjectIdentifier attrOid);

    @Nullable
    public ASN1Encodable getAttributeValue(ASN1ObjectIdentifier attrOid);

    public void putAttributeValueString(ASN1ObjectIdentifier attrOid, @Nullable String attrValueStr);

    public void putAttributeValue(ASN1ObjectIdentifier attrOid, @Nullable ASN1Encodable attrValue);

    public SortedMap<ASN1ObjectIdentifier, ASN1Encodable> getAttributeMap();
}
