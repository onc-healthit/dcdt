package gov.hhs.onc.dcdt.crypto.keys;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.CryptographyGenerator;

public interface KeyGenerator extends CryptographyGenerator<KeyConfig, KeyInfo> {
    public KeyInfo generateKeys(KeyConfig keyPairConfig) throws CryptographyException;
}
