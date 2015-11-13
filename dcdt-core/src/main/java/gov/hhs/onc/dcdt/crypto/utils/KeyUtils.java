package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.PemType;
import gov.hhs.onc.dcdt.crypto.keys.KeyAlgorithm;
import gov.hhs.onc.dcdt.crypto.keys.KeyException;
import gov.hhs.onc.dcdt.crypto.keys.KeyType;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyRep;
import java.security.NoSuchAlgorithmException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

public abstract class KeyUtils {
    public static Key readKey(KeyType keyType, byte[] data, KeyAlgorithm keyAlg, DataEncoding dataEnc) throws CryptographyException {
        try {
            if (dataEnc == DataEncoding.PEM) {
                data = PemUtils.writePemContent(ToolEnumUtils.findByType(PemType.class, keyType.getType()), data);
            }

            return ((Key) SerializationUtils.deserialize(SerializationUtils.serialize(new KeyRep(keyType.getKeyRepType(), keyAlg.getId(), keyAlg
                .getFormat(keyType), data))));
        } catch (SerializationException e) {
            throw new KeyException(String.format("Unable to read key (type=%s, algId=%s, format=%s, providerName=%s) from data.", keyType.getId(),
                keyAlg.getId(), keyAlg.getFormat(keyType), CryptographyUtils.PROVIDER_NAME), e);
        }
    }

    public static <T extends Key> byte[] writeKey(T key, DataEncoding dataEnc) throws CryptographyException {
        Class<? extends Key> keyClass = key.getClass();
        KeyType keyType = ToolEnumUtils.findByType(KeyType.class, keyClass);
        KeyAlgorithm keyAlg = ToolEnumUtils.findById(KeyAlgorithm.class, key.getAlgorithm());
        // noinspection ConstantConditions
        Class<? extends EncodedKeySpec> keySpecClass = keyAlg.getKeySpecClass(keyType);

        try {
            // noinspection ConstantConditions
            byte[] data = keySpecClass.cast(getKeyFactory(keyAlg).getKeySpec(key, keySpecClass)).getEncoded();

            return ((dataEnc == DataEncoding.PEM) ? PemUtils.writePemContent(ToolEnumUtils.findByType(PemType.class, keyClass), data) : data);
        } catch (InvalidKeySpecException e) {
            throw new KeyException(String.format("Unable to write key (type=%s, algId=%s, format=%s, keySpecClass=%s, class=%s, providerName=%s) to data.",
                keyType, keyAlg.getId(), keyAlg.getFormat(keyType), ToolClassUtils.getName(keySpecClass), ToolClassUtils.getName(keyClass),
                CryptographyUtils.PROVIDER_NAME), e);
        }
    }

    public static KeyPairGenerator getKeyPairGenerator(KeyAlgorithm keyAlg) throws CryptographyException {
        try {
            return CryptographyUtils.PROVIDER_HELPER.createKeyPairGenerator(keyAlg.getId());
        } catch (NoSuchAlgorithmException e) {
            throw new KeyException(String.format("Unable to get key pair generator for key algorithm (id=%s, providerName=%s).", keyAlg.getId(),
                CryptographyUtils.PROVIDER_NAME), e);
        }
    }

    public static KeyFactory getKeyFactory(KeyAlgorithm keyAlg) throws CryptographyException {
        try {
            return CryptographyUtils.PROVIDER_HELPER.createKeyFactory(keyAlg.getId());
        } catch (NoSuchAlgorithmException e) {
            throw new KeyException(String.format("Unable to get key factory for key algorithm (id=%s, providerName=%s).", keyAlg.getId(),
                CryptographyUtils.PROVIDER_NAME), e);
        }
    }
}
