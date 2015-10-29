package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;

public enum SecureRandomType implements ToolIdentifier {
    SHA1PRNG("SHA1PRNG");

    private final String id;

    private SecureRandomType(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
