package gov.hhs.onc.dcdt.dns;

import org.xbill.DNS.DNSSEC.Algorithm;

public enum DnsKeyAlgorithm {
    RSASHA1(Algorithm.RSASHA1), INDIRECT(Algorithm.INDIRECT);

    private final int alg;

    private DnsKeyAlgorithm(int alg) {
        this.alg = alg;
    }

    public int getAlgorithm() {
        return this.alg;
    }
}
