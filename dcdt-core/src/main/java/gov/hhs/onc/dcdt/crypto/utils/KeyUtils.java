package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.PemType;
import gov.hhs.onc.dcdt.crypto.keys.KeyAlgorithm;
import gov.hhs.onc.dcdt.crypto.keys.KeyException;
import gov.hhs.onc.dcdt.crypto.keys.KeyType;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyRep;
import java.security.NoSuchAlgorithmException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

public abstract class KeyUtils {
    public static Key readKey(KeyType keyType, InputStream inStream, KeyAlgorithm keyAlg, DataEncoding dataEnc) throws CryptographyException {
        return readKey(keyType, new InputStreamReader(inStream), keyAlg, dataEnc);
    }

    public static Key readKey(KeyType keyType, Reader reader, KeyAlgorithm keyAlg, DataEncoding dataEnc) throws CryptographyException {
        try {
            return readKey(keyType, IOUtils.toByteArray(reader), keyAlg, dataEnc);
        } catch (IOException e) {
            throw new KeyException(String.format("Unable to read key (type=%s, algId=%s, format=%s, providerName=%s) from reader (class=%s).", keyType.getId(),
                keyAlg.getId(), keyAlg.getFormat(keyType), CryptographyUtils.PROVIDER_NAME, ToolClassUtils.getName(reader)), e);
        }
    }

    public static Key readKey(KeyType keyType, byte[] data, KeyAlgorithm keyAlg, DataEncoding dataEnc) throws CryptographyException {
        try {
            if (dataEnc == DataEncoding.PEM) {
                data = PemUtils.writePemContent(CryptographyUtils.findByType(PemType.class, keyType.getType()), data);
            }

            return ((Key) SerializationUtils.deserialize(SerializationUtils.serialize(new KeyRep(keyType.getKeyRepType(), keyAlg.getId(), keyAlg
                .getFormat(keyType), data))));
        } catch (SerializationException e) {
            throw new KeyException(String.format("Unable to read key (type=%s, algId=%s, format=%s, providerName=%s) from data.", keyType.getId(),
                keyAlg.getId(), keyAlg.getFormat(keyType), CryptographyUtils.PROVIDER_NAME), e);
        }
    }

    public static <T extends Key> byte[] writeKey(T key, DataEncoding dataEnc) throws CryptographyException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        writeKey(outStream, key, dataEnc);

        return outStream.toByteArray();
    }

    public static <T extends Key> void writeKey(OutputStream outStream, T key, DataEncoding dataEnc) throws CryptographyException {
        writeKey(new OutputStreamWriter(outStream), key, dataEnc);
    }

    public static <T extends Key> void writeKey(Writer writer, T key, DataEncoding dataEnc) throws CryptographyException {
        Class<? extends Key> keyClass = key.getClass();
        KeyType keyType = CryptographyUtils.findByType(KeyType.class, keyClass);
        KeyAlgorithm keyAlg = CryptographyUtils.findById(KeyAlgorithm.class, key.getAlgorithm());
        // noinspection ConstantConditions
        Class<? extends EncodedKeySpec> keySpecClass = keyAlg.getKeySpecClass(keyType);

        try {
            // noinspection ConstantConditions
            byte[] data = keySpecClass.cast(getKeyFactory(keyAlg).getKeySpec(key, keySpecClass)).getEncoded();

            if (dataEnc == DataEncoding.PEM) {
                PemUtils.writePemContent(writer, CryptographyUtils.findByType(PemType.class, keyClass), data);
            } else {
                IOUtils.write(data, writer);
            }

            writer.flush();
        } catch (InvalidKeySpecException | IOException e) {
            throw new KeyException(String.format(
                "Unable to write key (type=%s, algId=%s, format=%s, keySpecClass=%s, class=%s, providerName=%s) to writer (class=%s).", keyType,
                keyAlg.getId(), keyAlg.getFormat(keyType), ToolClassUtils.getName(keySpecClass), ToolClassUtils.getName(keyClass),
                CryptographyUtils.PROVIDER_NAME, ToolClassUtils.getName(writer)), e);
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
