package gov.hhs.onc.dcdt.crypto.impl;

import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import gov.hhs.onc.dcdt.crypto.CertificateName;

public class CertificateNameImpl implements CertificateName {
    private final static String MAIL_ADDRESS_DELIM = "@";
    private final Map<ASN1ObjectIdentifier, ASN1Encodable> rdnMap = new HashMap<>();

    @Override
    public String getMail() {
        return this.getString(BCStyle.EmailAddress);
    }

    @Override
    public void setMail(String mail) {
        this.setString(BCStyle.EmailAddress, mail);
    }

    @Override
    public String getCommonName() {
        return this.getString(BCStyle.CN);
    }

    @Override
    public void setCommonName(String commonName) {
        this.setString(BCStyle.CN, commonName);
    }

    @Override
    public String getCountry() {
        return this.getString(BCStyle.C);
    }

    @Override
    public void setCountry(String country) {
        this.setString(BCStyle.C, country);
    }

    @Override
    public String getState() {
        return this.getString(BCStyle.ST);
    }

    @Override
    public void setState(String state) {
        this.setString(BCStyle.ST, state);
    }

    @Override
    public String getLocality() {
        return this.getString(BCStyle.L);
    }

    @Override
    public void setLocality(String locality) {
        this.setString(BCStyle.L, locality);
    }

    @Override
    public String getOrganization() {
        return this.getString(BCStyle.O);
    }

    @Override
    public void setOrganization(String organization) {
        this.setString(BCStyle.O, organization);
    }

    @Override
    public String getOrganizationUnit() {
        return this.getString(BCStyle.OU);
    }

    @Override
    public void setOrganizationUnit(String organizationUnit) {
        this.setString(BCStyle.OU, organizationUnit);
    }

    @Override
    public GeneralNames getSubjAltNames() {
        if (this.hasMail()) {
            String mail = this.getMail();

            return new GeneralNames(new GeneralName(mail.contains(MAIL_ADDRESS_DELIM) ? GeneralName.rfc822Name : GeneralName.dNSName, mail));
        }

        return null;
    }

    @Override
    public X500Name toX500Name() {
        X500NameBuilder x500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);

        for (ASN1ObjectIdentifier oid : this.rdnMap.keySet()) {
            x500NameBuilder.addRDN(oid, this.rdnMap.get(oid));
        }
        return x500NameBuilder.build();
    }

    @Override
    public boolean hasMail() {
        return this.getMail() != null;
    }

    @Override
    public String getName() {
        return this.toX500Name().toString();
    }

    @Override
    public void setString(ASN1ObjectIdentifier oid, String value) {
        this.setValue(oid, stringToValue(oid, value));
    }

    public static ASN1Encodable stringToValue(ASN1ObjectIdentifier oid, String value) {
        return value != null ? BCStyle.INSTANCE.stringToValue(oid, value) : null;
    }

    @Override
    public void setValue(ASN1ObjectIdentifier oid, ASN1Encodable value) {
        this.rdnMap.put(oid, value);
    }

    @Override
    public String getString(ASN1ObjectIdentifier oid) {
        return valueToString(this.getValue(oid));
    }

    public static String valueToString(ASN1Encodable value) {
        return value != null ? IETFUtils.valueToString(value) : null;
    }

    @Override
    public ASN1Encodable getValue(ASN1ObjectIdentifier oid) {
        return this.rdnMap.get(oid);
    }

    @Override
    public Map<ASN1ObjectIdentifier, ASN1Encodable> getRdnMap() {
        return rdnMap;
    }

    @Override
    public boolean hasSameRDNs(CertificateName certificateName) {
        if (this.rdnMap.size() == certificateName.getRdnMap().size()) {
            for (ASN1ObjectIdentifier oid : rdnMap.keySet()) {
                if (!certificateName.hasValue(oid) || !certificateName.getValue(oid).equals(this.getValue(oid))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean hasValue(ASN1ObjectIdentifier oid) {
        return this.rdnMap.containsKey(oid) && this.rdnMap.get(oid) != null;
    }
}
