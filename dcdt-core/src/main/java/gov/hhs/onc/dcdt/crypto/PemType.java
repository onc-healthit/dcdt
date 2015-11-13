package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import gov.hhs.onc.dcdt.beans.ToolTypeIdentifier;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

public enum PemType implements ToolIdentifier, ToolTypeIdentifier {
    PUBLIC_KEY("PUBLIC KEY", PublicKey.class), PRIVATE_KEY("PRIVATE KEY", PrivateKey.class), CERTIFICATE("CERTIFICATE", Certificate.class), CRL("CRL",
        java.security.cert.CRL.class);

    private final String id;
    private final Class<?> type;

    private PemType(String id, Class<?> type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }
}
