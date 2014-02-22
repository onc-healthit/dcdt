package gov.hhs.onc.dcdt.crypto.keys;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.CryptographyGenerator;
import java.security.SecureRandom;

public interface KeyGenerator extends CryptographyGenerator<KeyConfig, KeyInfo> {
    public KeyInfo generateKeys(KeyConfig keyPairConfig) throws CryptographyException;

    public KeyInfo generateKeys(KeyConfig keyPairConfig, SecureRandom secureRandom) throws CryptographyException;
}
