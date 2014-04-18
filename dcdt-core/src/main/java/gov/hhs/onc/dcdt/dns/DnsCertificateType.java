package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.crypto.CryptographyTaggedIdentifier;
import org.xbill.DNS.CERTRecord.CertificateType;

public enum DnsCertificateType implements CryptographyTaggedIdentifier {
    PKIX("PKIX", CertificateType.PKIX), IPKIX("IPKIX", CertificateType.IPKIX);

    private final String id;
    private final int tag;

    private DnsCertificateType(String id, int tag) {
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
