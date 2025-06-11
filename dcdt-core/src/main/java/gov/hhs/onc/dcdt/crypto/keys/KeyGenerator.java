package gov.hhs.onc.dcdt.crypto.keys;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.CryptographyGenerator;
import javax.annotation.Nullable;
import java.security.PublicKey;

public interface KeyGenerator extends CryptographyGenerator<KeyConfig, KeyInfo> {
    public KeyInfo generateKeys(KeyConfig keyPairConfig, @Nullable PublicKey issuerPublicKey) throws CryptographyException;
}
