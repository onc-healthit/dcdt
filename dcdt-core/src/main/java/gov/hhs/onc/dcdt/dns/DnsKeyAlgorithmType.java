package gov.hhs.onc.dcdt.dns;

import org.xbill.DNS.DNSSEC.Algorithm;

public enum DnsKeyAlgorithmType {
    RSASHA1(Algorithm.RSASHA1), INDIRECT(Algorithm.INDIRECT);

    private final int type;

    private DnsKeyAlgorithmType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
