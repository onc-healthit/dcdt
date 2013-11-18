package gov.hhs.onc.dcdt.crypto;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyPairInfo {
    private KeyPair keyPair;

    public KeyPair getKeyPair() {
        return this.keyPair;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public PublicKey getPublicKey() {
        return this.keyPair != null ? this.keyPair.getPublic() : null;
    }

    public PrivateKey getPrivateKey() {
        return this.keyPair != null ? this.keyPair.getPrivate() : null;
    }

}
