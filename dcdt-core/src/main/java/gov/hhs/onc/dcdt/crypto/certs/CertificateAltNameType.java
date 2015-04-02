package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyTaggedIdentifier;
import org.bouncycastle.asn1.x509.GeneralName;

public enum CertificateAltNameType implements CryptographyTaggedIdentifier {
    RFC822_NAME("rfc822Name", GeneralName.rfc822Name), DNS_NAME("dNSName", GeneralName.dNSName);

    private final String id;
    private final int tag;

    private CertificateAltNameType(String id, int tag) {
        this.id = id;
        this.tag = tag;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getTag() {
        return this.tag;
    }
}
