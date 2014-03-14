package gov.hhs.onc.dcdt.dns;

import org.xbill.DNS.CERTRecord.CertificateType;

public enum DnsCertificateType {
    PKIX(CertificateType.PKIX), IPKIX(CertificateType.IPKIX);

    private final int type;

    private DnsCertificateType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
