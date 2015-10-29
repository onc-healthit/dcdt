package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.SecureRandomType;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;

public abstract class SecureRandomUtils {
    public static SecureRandom getRandom(SecureRandomType type, Provider prov) throws CryptographyException {
        try {
            SecureRandom random = SecureRandom.getInstance(type.getId(), prov);
            random.nextBytes(new byte[1]);

            return random;
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException(String.format("Unable to get secure random instance for secure random type (id=%s, providerName=%s).",
                type.getId(), prov.getName()), e);
        }
    }
}
