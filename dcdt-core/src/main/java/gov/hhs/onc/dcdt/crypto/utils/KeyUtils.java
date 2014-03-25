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
import java.security.KeyPairGenerator;
import java.security.KeyRep;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

public abstract class KeyUtils {
    public static PublicKey readPublicKey(InputStream inStream, KeyAlgorithm keyAlg, DataEncoding dataEnc) throws CryptographyException {
        return (PublicKey) readKey(KeyType.PUBLIC, inStream, keyAlg, dataEnc);
    }

    public static PublicKey readPublicKey(Reader reader, KeyAlgorithm keyAlg, DataEncoding dataEnc) throws CryptographyException {
        return (PublicKey) readKey(KeyType.PUBLIC, reader, keyAlg, dataEnc);
    }

    public static PublicKey readPublicKey(byte[] data, KeyAlgorithm keyAlg, DataEncoding dataEnc) throws CryptographyException {
        return (PublicKey) readKey(KeyType.PUBLIC, data, keyAlg, dataEnc);
    }

    public static byte[] writePublicKey(PublicKey publicKey, DataEncoding dataEnc) throws CryptographyException {
        return writeKey(publicKey, dataEnc);
    }

    public static void writePublicKey(OutputStream outStream, PublicKey publicKey, DataEncoding dataEnc) throws CryptographyException {
        writeKey(outStream, publicKey, dataEnc);
    }

    public static void writePublicKey(Writer writer, PublicKey publicKey, DataEncoding dataEnc) throws CryptographyException {
        writeKey(writer, publicKey, dataEnc);
    }

    public static PrivateKey readPrivateKey(InputStream inStream, KeyAlgorithm keyAlg, DataEncoding dataEnc) throws CryptographyException {
        return (PrivateKey) readKey(KeyType.PRIVATE, inStream, keyAlg, dataEnc);
    }

    public static PrivateKey readPrivateKey(Reader reader, KeyAlgorithm keyAlg, DataEncoding dataEnc) throws CryptographyException {
        return (PrivateKey) readKey(KeyType.PRIVATE, reader, keyAlg, dataEnc);
    }

    public static PrivateKey readPrivateKey(byte[] data, KeyAlgorithm keyAlg, DataEncoding dataEnc) throws CryptographyException {
        return (PrivateKey) readKey(KeyType.PRIVATE, data, keyAlg, dataEnc);
    }

    public static byte[] writePrivateKey(PrivateKey privateKey, DataEncoding dataEnc) throws CryptographyException {
        return writeKey(privateKey, dataEnc);
    }

    public static void writePrivateKey(OutputStream outStream, PrivateKey privateKey, DataEncoding dataEnc) throws CryptographyException {
        writeKey(outStream, privateKey, dataEnc);
    }

    public static void writePrivateKey(Writer writer, PrivateKey privateKey, DataEncoding dataEnc) throws CryptographyException {
        writeKey(writer, privateKey, dataEnc);
    }

    public static Key readKey(KeyType keyType, InputStream inStream, KeyAlgorithm keyAlg, DataEncoding dataEnc) throws CryptographyException {
        return readKey(keyType, new InputStreamReader(inStream), keyAlg, dataEnc);
    }

    public static Key readKey(KeyType keyType, Reader reader, KeyAlgorithm keyAlg, DataEncoding dataEnc) throws CryptographyException {
        try {
            return readKey(keyType, IOUtils.toByteArray(reader), keyAlg, dataEnc);
        } catch (IOException e) {
            throw new KeyException(String.format(
                "Unable to read key instance (type=%s) for key algorithm (id=%s, format=%s, providerName=%s) from reader (class=%s).", keyType.getId(),
                keyAlg.getId(), keyAlg.getPrivateFormat(), CryptographyUtils.PROVIDER_NAME, ToolClassUtils.getName(reader)), e);
        }
    }

    public static Key readKey(KeyType keyType, byte[] data, KeyAlgorithm keyAlg, DataEncoding dataEnc) throws CryptographyException {
        try {
            if (dataEnc == DataEncoding.PEM) {
                data = PemUtils.writePemContent(keyType.getId(), data);
            }

            return (Key) SerializationUtils.deserialize(SerializationUtils.serialize(new KeyRep(keyType.getKeyRepType(), keyAlg.getId(), keyAlg
                .getFormat(keyType), data)));
        } catch (SerializationException e) {
            throw new KeyException(String.format("Unable to read key instance (type=%s) for key algorithm (id=%s, format=%s, providerName=%s) from data.",
                keyType.getId(), keyAlg.getId(), keyAlg.getPrivateFormat(), CryptographyUtils.PROVIDER_NAME), e);
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
        byte[] data = key.getEncoded();

        try {
            if (dataEnc == DataEncoding.PEM) {
                PemUtils.writePemContent(writer, CryptographyUtils.findTypeId(PemType.class, key.getClass()), data);
            } else {
                IOUtils.write(data, writer);
            }
        } catch (IOException e) {
            throw new KeyException(String.format("Unable to write key instance (type=%s, class=%s) to writer (class=%s).",
                CryptographyUtils.findTypeId(KeyType.class, key.getClass()), ToolClassUtils.getClass(key), ToolClassUtils.getName(writer)), e);
        }
    }

    public static KeyPairGenerator getKeyPairGenerator(KeyAlgorithm keyAlg) throws CryptographyException {
        try {
            return CryptographyUtils.PROVIDER_HELPER.createKeyPairGenerator(keyAlg.getId());
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new KeyException(String.format("Unable to get key pair generator instance for key algorithm (id=%s, providerName=%s).", keyAlg.getId(),
                CryptographyUtils.PROVIDER_NAME), e);
        }
    }
}
