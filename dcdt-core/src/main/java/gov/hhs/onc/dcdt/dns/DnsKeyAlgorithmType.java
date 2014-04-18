package gov.hhs.onc.dcdt.dns;

import gov.hhs.onc.dcdt.crypto.CryptographyTaggedIdentifier;
import org.xbill.DNS.DNSSEC.Algorithm;

public enum DnsKeyAlgorithmType implements CryptographyTaggedIdentifier {
    RSASHA1("RSASHA1", Algorithm.RSASHA1), INDIRECT("INDIRECT", Algorithm.INDIRECT);

    private final String id;
    private final int tag;

    private DnsKeyAlgorithmType(String id, int tag) {
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
