package gov.hhs.onc.dcdt.crypto.utils;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.constants.DataEncoding;
import gov.hhs.onc.dcdt.crypto.constants.KeyAlgorithm;
import gov.hhs.onc.dcdt.crypto.constants.PemType;

public abstract class KeyPairUtils {
    private final static int KEY_PAIR_SEED_SIZE = 32;

    public static KeyPair generateKeyPair(int keySize) throws CryptographyException {
        return generateKeyPair(keySize, CryptographyUtils.getRandom(KEY_PAIR_SEED_SIZE));
    }

    public static KeyPair generateKeyPair(int keySize, SecureRandom random) throws CryptographyException {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KeyAlgorithm.RSA, CryptographyUtils.BOUNCY_CASTLE_PROVIDER);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException("Can't generate key pair.", e);
        }
        keyPairGenerator.initialize(keySize, random);
        return keyPairGenerator.generateKeyPair();
    }

    public static PrivateKey readPrivateKey(File file, String encoding) throws CryptographyException {
        try {
            return readPrivateKey(new FileInputStream(file), encoding);
        } catch (IOException e) {
            throw new CryptographyException("Unable to read RSA private key from file (" + file + "): encoding=" + encoding, e);
        }
    }

    public static PrivateKey readPrivateKey(InputStream stream, String encoding) throws CryptographyException {
        try {
            return readPrivateKey(IOUtils.toByteArray(stream), encoding);
        } catch (IOException e) {
            throw new CryptographyException("Unable to read RSA private key from stream: encoding=" + encoding, e);
        }
    }

    public static PrivateKey readPrivateKey(byte[] data, String encoding) throws CryptographyException {
        try {
            switch (StringUtils.lowerCase(encoding)) {
                case DataEncoding.PEM:
                    data = CryptographyUtils.readPemContent(new ByteArrayInputStream(data));

                case DataEncoding.DER:
                    return getRsaKeyFactory().generatePrivate(new PKCS8EncodedKeySpec(data));

                default:
                    throw new CryptographyException("Unknown private key data encoding: " + encoding);
            }
        } catch (InvalidKeySpecException e) {
            throw new CryptographyException("Unable to read RSA private key from data: encoding=" + encoding, e);
        }
    }

    public static void writeKeyPair(File file, PrivateKey privateKey, String encoding) throws CryptographyException {
        try {
            writePrivateKey(new FileOutputStream(file), privateKey, encoding);
        } catch (IOException e) {
            throw new CryptographyException("Unable to write RSA private key to file (" + file + "): encoding=" + encoding, e);
        }
    }

    public static void writePrivateKey(OutputStream stream, PrivateKey privateKey, String encoding) throws CryptographyException {
        byte[] data = privateKey.getEncoded();

        try {
            switch (StringUtils.lowerCase(encoding)) {
                case DataEncoding.PEM:
                    CryptographyUtils.writePemContent(stream, PemType.PKCS8_PRIVATE_KEY, data);
                    break;

                case DataEncoding.DER:
                    IOUtils.write(data, stream);
                    break;

                default:
                    throw new CryptographyException("Unknown RSA private key data encoding: " + encoding);
            }
        } catch (IOException e) {
            throw new CryptographyException("Unable to write RSA private key to stream: encoding=" + encoding, e);
        }
    }

    public static KeyFactory getRsaKeyFactory() throws CryptographyException {
        try {
            return KeyFactory.getInstance(KeyAlgorithm.RSA, CryptographyUtils.BOUNCY_CASTLE_PROVIDER);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException("Unable to get X509 certificate factory (type=" + KeyAlgorithm.RSA
                + ") instance from BouncyCastle provider (name= " + CryptographyUtils.BOUNCY_CASTLE_PROVIDER.getName() + " ).", e);
        }
    }
}
