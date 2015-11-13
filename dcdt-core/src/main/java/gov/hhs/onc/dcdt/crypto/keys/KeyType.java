package gov.hhs.onc.dcdt.crypto.keys;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import gov.hhs.onc.dcdt.beans.ToolTypeIdentifier;
import java.security.KeyRep.Type;
import java.security.PrivateKey;
import java.security.PublicKey;

public enum KeyType implements ToolIdentifier, ToolTypeIdentifier {
    PUBLIC(Type.PUBLIC.name(), PublicKey.class), PRIVATE(Type.PRIVATE.name(), PrivateKey.class);

    private final String id;
    private final Class<?> type;

    private KeyType(String id, Class<?> type) {
        this.id = id;
        this.type = type;
    }

    public Type getKeyRepType() {
        return Type.valueOf(this.id);
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
