package gov.hhs.onc.dcdt.crypto;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeyPairInfo {
    public KeyPair getKeyPair();

    public void setKeyPair(KeyPair keyPair);

    public PublicKey getPublicKey();

    public PrivateKey getPrivateKey();
}
