package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class DigestUtils {
    private DigestUtils() {
    }

    public static byte[] digest(String algId, byte ... data) throws CryptographyException {
        return getMessageDigest(algId).digest(data);
    }

    public static MessageDigest getMessageDigest(String algId) throws CryptographyException {
        try {
            return CryptographyUtils.PROVIDER_HELPER.createDigest(algId);
        } catch (NoSuchAlgorithmException e) {
            throw new DigestException(String.format("Unable to get message digest algorithm (id=%s, providerName=%s).", algId, CryptographyUtils.PROVIDER_NAME),
                e);
        }
    }
}
