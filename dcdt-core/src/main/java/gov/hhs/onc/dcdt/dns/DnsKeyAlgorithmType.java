package gov.hhs.onc.dcdt.dns;

import javax.annotation.Nonnegative;
import org.xbill.DNS.DNSSEC.Algorithm;

/**
 * @see org.xbill.DNS.DNSSEC.Algorithm
 */
public enum DnsKeyAlgorithmType implements DnsMnemonicIdentifier {
    RSAMD5(Algorithm.RSAMD5), DH(Algorithm.DH), DSA(Algorithm.DSA), RSASHA1(Algorithm.RSASHA1), DSA_NSEC3_SHA1(Algorithm.DSA_NSEC3_SHA1), RSA_NSEC3_SHA1(
        Algorithm.RSA_NSEC3_SHA1), RSASHA256(Algorithm.RSASHA256), RSASHA512(Algorithm.RSASHA512), ECDSAP256SHA256(Algorithm.ECDSAP256SHA256), ECDSAP384SHA384(
        Algorithm.ECDSAP384SHA384), INDIRECT(Algorithm.INDIRECT), PRIVATEDNS(Algorithm.PRIVATEDNS), PRIVATEOID(Algorithm.PRIVATEOID);

    private final int code;
    private final String id;

    private DnsKeyAlgorithmType(@Nonnegative int code) {
        this.code = code;
        this.id = Algorithm.string(this.code);
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
