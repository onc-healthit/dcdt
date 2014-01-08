package gov.hhs.onc.dcdt.crypto.keys;

import gov.hhs.onc.dcdt.crypto.CryptographyTypeIdentifier;
import java.security.Key;
import java.security.KeyRep.Type;
import java.security.PrivateKey;
import java.security.PublicKey;

public enum KeyType implements CryptographyTypeIdentifier<Key> {
    PUBLIC(Type.PUBLIC.name(), PublicKey.class), PRIVATE(Type.PRIVATE.name(), PrivateKey.class);

    private final String name;
    private final Class<? extends Key> type;

    private KeyType(String name, Class<? extends Key> type) {
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
    public Class<? extends Key> getType() {
        return this.type;
    }
}
