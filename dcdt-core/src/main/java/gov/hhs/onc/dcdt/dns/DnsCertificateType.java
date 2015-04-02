package gov.hhs.onc.dcdt.dns;

import javax.annotation.Nonnegative;
import org.xbill.DNS.CERTRecord.CertificateType;

/**
 * @see org.xbill.DNS.CERTRecord.CertificateType
 */
public enum DnsCertificateType implements DnsMnemonicIdentifier {
    PKIX(CertificateType.PKIX, "PKIX"), SPKI(CertificateType.SPKI, "SPKI"), PGP(CertificateType.PGP, "PGP"), IPKIX(CertificateType.IPKIX, "IPKIX"), ISPKI(
        CertificateType.ISPKI, "ISPKI"), IPGP(CertificateType.IPGP, "IPGP"), ACPKIX(CertificateType.ACPKIX, "ACPKIX"), IACPKIX(CertificateType.IACPKIX,
        "IACPKIX"), URI(CertificateType.URI), OID(CertificateType.OID);

    private final int code;
    private final String id;

    private DnsCertificateType(@Nonnegative int code) {
        this(code, CertificateType.string(code));
    }

    private DnsCertificateType(@Nonnegative int code, String id) {
        this.code = code;
        this.id = id;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
