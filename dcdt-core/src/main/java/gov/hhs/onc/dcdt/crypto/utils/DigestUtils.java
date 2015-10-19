package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DigestAlgorithm;
import gov.hhs.onc.dcdt.crypto.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class DigestUtils {
    private DigestUtils() {
    }

    public static byte[] digest(DigestAlgorithm digestAlg, byte ... data) throws CryptographyException {
        return getMessageDigest(digestAlg.getId()).digest(data);
    }

    public static MessageDigest getMessageDigest(String digestAlgId) throws CryptographyException {
        try {
            return CryptographyUtils.PROVIDER_HELPER.createDigest(digestAlgId);
        } catch (NoSuchAlgorithmException e) {
            throw new DigestException(
                String.format("Unable to get message digest algorithm (id=%s, providerName=%s).", digestAlgId, CryptographyUtils.PROVIDER_NAME), e);
        }
    }
}
