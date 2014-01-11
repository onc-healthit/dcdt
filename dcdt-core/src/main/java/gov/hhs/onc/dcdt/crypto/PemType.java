package gov.hhs.onc.dcdt.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public enum PemType implements CryptographyTypeIdentifier {
    X509_PUBLIC_KEY("PUBLIC KEY", PublicKey.class), PKCS8_PRIVATE_KEY("RSA PRIVATE KEY", PrivateKey.class), X509_CERTIFICATE("X.509 CERTIFICATE",
        X509Certificate.class);

    private final String name;
    private final Class<?> type;

    private PemType(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }
}
