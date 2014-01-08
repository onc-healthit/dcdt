package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.X500Utils;
import gov.hhs.onc.dcdt.crypto.utils.X500Utils.OrderedOidComparator;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.GeneralNames;

public class CertificateNameImpl extends AbstractToolBean implements CertificateName {
    private final static List<ASN1ObjectIdentifier> ATTR_OIDS_ORDER = ToolArrayUtils.asList(BCStyle.EmailAddress, BCStyle.CN);

    private SortedMap<ASN1ObjectIdentifier, ASN1Encodable> attrMap = new TreeMap<>(new OrderedOidComparator(ATTR_OIDS_ORDER));

    public CertificateNameImpl() {
    }

    public CertificateNameImpl(String x500NameStr) {
        this(new X500Name(x500NameStr));
    }

    public CertificateNameImpl(X500Name x500Name) {
        this(x500Name.getRDNs());
    }

    public CertificateNameImpl(RDN ... rdns) {
        this(ToolArrayUtils.asList(rdns));
    }

    public CertificateNameImpl(Iterable<RDN> rdns) {
        this.attrMap.putAll(X500Utils.mapAttributes(rdns));
    }

    @Override
    public X500Name toX500Name() {
        return X500Utils.buildName(this.attrMap);
    }

    @Override
    public boolean hasSubjectAltNames() {
        return this.getSubjectAltNames() != null;
    }

    @Nullable
    @Override
    public GeneralNames getSubjectAltNames() {
        return CertificateUtils.buildSubjectAltNames(this.getMailAddress());
    }

    @Override
    public String getName() {
        return this.toX500Name().toString();
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj)
            || (ToolClassUtils.isAssignable(CertificateName.class, ToolClassUtils.getClass(obj)) && ObjectUtils.equals(this.getName(),
                ((CertificateName) obj).getName()));
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public boolean hasCommonName() {
        return this.hasAttribute(BCStyle.CN);
    }

    @Nullable
    @Override
    public String getCommonName() {
        return this.getAttributeValueString(BCStyle.CN);
    }

    @Override
    public void setCommonName(@Nullable String commonName) {
        this.putAttributeValueString(BCStyle.CN, commonName);
    }

    @Override
    public boolean hasMailAddress() {
        return this.hasAttribute(BCStyle.EmailAddress);
    }

    @Nullable
    @Override
    public String getMailAddress() {
        return this.getAttributeValueString(BCStyle.EmailAddress);
    }

    @Override
    public void setMailAddress(@Nullable String mailAddr) {
        this.putAttributeValueString(BCStyle.EmailAddress, mailAddr);
    }

    public boolean hasAttribute(ASN1ObjectIdentifier attrOid) {
        return this.attrMap.containsKey(attrOid);
    }

    @Nullable
    public String getAttributeValueString(ASN1ObjectIdentifier attrOid) {
        return X500Utils.toStringValue(attrOid, getAttributeValue(attrOid));
    }

    @Nullable
    @Override
    public ASN1Encodable getAttributeValue(ASN1ObjectIdentifier attrOid) {
        return this.attrMap.get(attrOid);
    }

    @Override
    public void putAttributeValueString(ASN1ObjectIdentifier attrOid, @Nullable String attrValueStr) {
        this.putAttributeValue(attrOid, X500Utils.toEncodableValue(attrOid, attrValueStr));
    }

    @Override
    public void putAttributeValue(ASN1ObjectIdentifier attrOid, @Nullable ASN1Encodable attrValue) {
        this.attrMap.put(attrOid, attrValue);
    }

    @Override
    public SortedMap<ASN1ObjectIdentifier, ASN1Encodable> getAttributeMap() {
        return this.attrMap;
    }
}
