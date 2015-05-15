package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.xbill.DNS.DNSSEC;
import org.xbill.DNS.DNSSEC.Algorithm;
import org.xbill.DNS.DNSSEC.UnsupportedAlgorithmException;

/**
 * @see org.xbill.DNS.DNSSEC.Algorithm
 */
public enum DnsKeyAlgorithmType implements DnsMnemonicIdentifier {
    RSAMD5(Algorithm.RSAMD5), DH(Algorithm.DH), DSA(Algorithm.DSA), RSASHA1(Algorithm.RSASHA1), DSA_NSEC3_SHA1(Algorithm.DSA_NSEC3_SHA1), RSA_NSEC3_SHA1(
        Algorithm.RSA_NSEC3_SHA1), RSASHA256(Algorithm.RSASHA256), RSASHA512(Algorithm.RSASHA512), ECDSAP256SHA256(Algorithm.ECDSAP256SHA256), ECDSAP384SHA384(
        Algorithm.ECDSAP384SHA384), INDIRECT(Algorithm.INDIRECT), PRIVATEDNS(Algorithm.PRIVATEDNS), PRIVATEOID(Algorithm.PRIVATEOID);

    public final static String PROP_NAME_SIG_ALG = "signatureAlgorithm";

    private final int code;
    private final String id;
    private SignatureAlgorithm sigAlg;

    private DnsKeyAlgorithmType(@Nonnegative int code) {
        this.code = code;
        this.id = Algorithm.string(this.code);

        try {
            this.sigAlg =
                CryptographyUtils.findByOid(SignatureAlgorithm.class, CryptographyUtils.SIG_ALG_ID_FINDER.find(DNSSEC.algString(this.code)).getAlgorithm());
        } catch (IllegalArgumentException | UnsupportedAlgorithmException ignored) {
        }
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public boolean hasSignatureAlgorithm() {
        return (this.sigAlg != null);
    }

    @Nullable
    public SignatureAlgorithm getSignatureAlgorithm() {
        return this.sigAlg;
    }
}
