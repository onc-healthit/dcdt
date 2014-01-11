package gov.hhs.onc.dcdt.crypto.keys;

import gov.hhs.onc.dcdt.crypto.CryptographyTypeIdentifier;
import java.security.KeyRep.Type;
import java.security.PrivateKey;
import java.security.PublicKey;

public enum KeyType implements CryptographyTypeIdentifier {
    PUBLIC(Type.PUBLIC.name(), PublicKey.class), PRIVATE(Type.PRIVATE.name(), PrivateKey.class);

    private final String name;
    private final Class<?> type;

    private KeyType(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public Type getKeyRepType() {
        return Type.valueOf(this.name);
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
