package gov.hhs.onc.dcdt.crypto.impl;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import gov.hhs.onc.dcdt.crypto.KeyPairInfo;

public class KeyPairInfoImpl implements KeyPairInfo {
    private KeyPair keyPair;

    @Override
    public KeyPair getKeyPair() {
        return this.keyPair;
    }

    @Override
    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    @Override
    public PublicKey getPublicKey() {
        return this.keyPair != null ? this.keyPair.getPublic() : null;
    }

    @Override
    public PrivateKey getPrivateKey() {
        return this.keyPair != null ? this.keyPair.getPrivate() : null;
    }

}
