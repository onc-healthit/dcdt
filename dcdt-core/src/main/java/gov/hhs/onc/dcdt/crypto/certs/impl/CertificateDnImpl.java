package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.crypto.certs.CertificateDn;
import gov.hhs.onc.dcdt.crypto.utils.X500Utils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import gov.hhs.onc.dcdt.utils.ToolComparatorUtils;
import gov.hhs.onc.dcdt.utils.ToolMapUtils;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;

public class CertificateDnImpl extends AbstractToolBean implements CertificateDn {
    private final static Comparator<ASN1ObjectIdentifier> ATTR_OID_COMPARATOR = ToolComparatorUtils.comparingOrder(BCStyle.EmailAddress, BCStyle.CN);

    private Map<ASN1ObjectIdentifier, ASN1Encodable> attrMap = new LinkedHashMap<>();

    public CertificateDnImpl() {
        this(null);
    }

    public CertificateDnImpl(@Nullable X500Name name) {
        if (name != null) {
            this.attrMap.putAll(X500Utils.mapAttributes(name.getRDNs()));
        }
    }

    @Override
    public X500Name toX500Name() {
        return this.toX500Name(true);
    }

    @Override
    public X500Name toX500Name(boolean ordered) {
        return X500Utils.buildName((ordered ? ToolMapUtils.putAll(new TreeMap<>(ATTR_OID_COMPARATOR), this.attrMap.entrySet()) : this.attrMap));
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return ((obj != null) && (obj instanceof CertificateDn) && this.toX500Name().equals(((CertificateDn) obj).toX500Name()));
    }

    @Override
    public int hashCode() {
        return this.toX500Name().hashCode();
    }

    @Override
    public String toString() {
        return this.toX500Name().toString();
    }

    @Override
    public Map<ASN1ObjectIdentifier, ASN1Encodable> getAttributes() {
        return this.attrMap;
    }

    @Override
    public boolean hasCommonName() {
        return this.attrMap.containsKey(BCStyle.CN);
    }

    @Nullable
    @Override
    public String getCommonName() {
        return (this.hasCommonName() ? X500Utils.toStringValue(this.attrMap.get(BCStyle.CN)) : null);
    }

    @Override
    public void setCommonName(@Nullable String commonName) {
        if (commonName != null) {
            this.attrMap.put(BCStyle.CN, X500Utils.toEncodableValue(BCStyle.CN, commonName));
        } else {
            this.attrMap.remove(BCStyle.CN);
        }
    }

    @Override
    public boolean hasMailAddress() {
        return this.attrMap.containsKey(BCStyle.EmailAddress);
    }

    @Nullable
    @Override
    public MailAddress getMailAddress() {
        return (this.hasMailAddress() ? new MailAddressImpl(X500Utils.toStringValue(this.attrMap.get(BCStyle.EmailAddress))) : null);
    }

    @Override
    public void setMailAddress(@Nullable MailAddress mailAddr) {
        if ((mailAddr != null) && mailAddr.getBindingType().isAddressBound()) {
            this.attrMap.put(BCStyle.EmailAddress, X500Utils.toEncodableValue(BCStyle.EmailAddress, mailAddr.toAddress()));
        } else {
            this.attrMap.remove(BCStyle.EmailAddress);
        }
    }
}
