package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.crypto.certs.CertificateAltNameType;
import gov.hhs.onc.dcdt.crypto.certs.CertificateName;
import gov.hhs.onc.dcdt.crypto.utils.CertificateNameUtils;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import gov.hhs.onc.dcdt.crypto.utils.X500Utils;
import gov.hhs.onc.dcdt.crypto.utils.X500Utils.OrderedOidComparator;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.annotation.Nullable;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;

public class CertificateNameImpl extends AbstractToolBean implements CertificateName {
    private final static List<ASN1ObjectIdentifier> ATTR_OIDS_ORDER = ToolArrayUtils.asList(BCStyle.EmailAddress, BCStyle.CN);

    private SortedMap<ASN1ObjectIdentifier, ASN1Encodable> attrMap = new TreeMap<>(new OrderedOidComparator(ATTR_OIDS_ORDER));
    private Map<CertificateAltNameType, GeneralName> altNameMap;

    public CertificateNameImpl() {
        this(null, ((Iterable<RDN>) null));
    }

    public CertificateNameImpl(@Nullable X500Principal x500Principal) {
        this(null, x500Principal);
    }

    public CertificateNameImpl(@Nullable GeneralNames altNames, @Nullable X500Principal x500Principal) {
        this(altNames, ((x500Principal != null) ? x500Principal.getName() : null));
    }

    public CertificateNameImpl(@Nullable String x500NameStr) {
        this(null, x500NameStr);
    }

    public CertificateNameImpl(@Nullable GeneralNames altNames, @Nullable String x500NameStr) {
        this(altNames, new X500Name(x500NameStr));
    }

    public CertificateNameImpl(@Nullable X500Name x500Name) {
        this(null, x500Name);
    }

    public CertificateNameImpl(@Nullable GeneralNames altNames, @Nullable X500Name x500Name) {
        this(altNames, ((x500Name != null) ? x500Name.getRDNs() : null));
    }

    public CertificateNameImpl(@Nullable RDN ... rdns) {
        this(null, rdns);
    }

    public CertificateNameImpl(@Nullable GeneralNames altNames, @Nullable RDN ... rdns) {
        this(altNames, ToolArrayUtils.asList(rdns));
    }

    public CertificateNameImpl(@Nullable Iterable<RDN> rdns) {
        this(null, rdns);
    }

    public CertificateNameImpl(@Nullable GeneralNames altNames, @Nullable Iterable<RDN> rdns) {
        this.setAltNames(altNames);

        if (rdns != null) {
            this.attrMap.putAll(X500Utils.mapAttributes(rdns));
        }
    }

    @Override
    public X500Principal toX500Principal() {
        return new X500Principal(this.getName());
    }

    @Override
    public X500Name toX500Name() {
        return X500Utils.buildName(this.attrMap);
    }

    @Override
    public String getName() {
        return this.toX500Name().toString();
    }

    @Override
    @SuppressWarnings({ "EqualsWhichDoesntCheckParameterClass" })
    public boolean equals(Object obj) {
        CertificateName certName;

        return ((this == obj) || (ToolClassUtils.isAssignable(ToolClassUtils.getClass(obj), CertificateName.class) && Objects.equals((certName =
            ((CertificateName) obj)).getName(), this.getName()))
            && Objects.equals(certName.getAltNames(), this.getAltNames()));
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
        return this.getMailAddress() != null;
    }

    @Nullable
    @Override
    public MailAddress getMailAddress() {
        String mailAddrStr;

        // noinspection ConstantConditions
        return (((this.hasAltName(CertificateAltNameType.RFC822_NAME) && !StringUtils.isBlank((mailAddrStr =
            Objects.toString(this.getAltName(CertificateAltNameType.RFC822_NAME).getName(), null))))
            || (this.hasAttribute(BCStyle.EmailAddress) && !StringUtils.isBlank((mailAddrStr = this.getAttributeValueString(BCStyle.EmailAddress)))) || (this
            .hasAltName(CertificateAltNameType.DNS_NAME) && !StringUtils.isBlank((mailAddrStr =
            Objects.toString(this.getAltName(CertificateAltNameType.DNS_NAME).getName(), null))))) ? new MailAddressImpl(mailAddrStr) : null);
    }

    @Override
    public void setMailAddress(@Nullable MailAddress mailAddr) {
        if ((mailAddr != null) && mailAddr.getBindingType().isAddressBound()) {
            this.putAttributeValueString(BCStyle.EmailAddress, mailAddr.toAddress());
        } else {
            this.attrMap.remove(BCStyle.EmailAddress);
        }

        this.setAltNames(CertificateNameUtils.setMailAddress(this.getAltNames(), mailAddr));
    }

    @Override
    public boolean hasAltName(CertificateAltNameType altNameType) {
        return (this.hasAltNames() && this.altNameMap.containsKey(altNameType));
    }

    @Nullable
    @Override
    public GeneralName getAltName(CertificateAltNameType altNameType) {
        return (this.hasAltName(altNameType) ? this.altNameMap.get(altNameType) : null);
    }

    @Override
    public void setAltName(GeneralName altName) {
        CertificateAltNameType altNameType = CryptographyUtils.findTaggedId(CertificateAltNameType.class, altName.getTagNo());

        if (altNameType == null) {
            return;
        }

        if (!this.hasAltNames()) {
            this.setAltNames(new GeneralNames(altName));
        } else {
            this.altNameMap.put(altNameType, altName);
        }
    }

    @Override
    public boolean hasAltNames() {
        return !MapUtils.isEmpty(this.altNameMap);
    }

    @Nullable
    @Override
    public GeneralNames getAltNames() {
        return (this.hasAltNames() ? new GeneralNames(ToolCollectionUtils.toArray(this.altNameMap.values(), GeneralName.class)) : null);
    }

    @Override
    public void setAltNames(@Nullable GeneralNames altNames) {
        this.altNameMap = CertificateNameUtils.mapAltNames(altNames);
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
