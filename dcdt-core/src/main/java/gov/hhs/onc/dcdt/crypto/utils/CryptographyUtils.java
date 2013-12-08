package gov.hhs.onc.dcdt.crypto.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import org.bouncycastle.crypto.prng.VMPCRandomGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import gov.hhs.onc.dcdt.crypto.CryptographyException;

public abstract class CryptographyUtils {

    public final static Provider BOUNCY_CASTLE_PROVIDER = new BouncyCastleProvider();

    static {
        Security.addProvider(BOUNCY_CASTLE_PROVIDER);
    }

    public static SecureRandom getRandom(int seedSize) {
        return getRandom(generateRandomSeed(seedSize));
    }

    public static SecureRandom getRandom(byte[] seed) {
        return new SecureRandom(seed);
    }

    public static byte[] generateRandomSeed(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Random seed size must be a positive integer: " + size);
        }

        byte[] randSeed = new byte[size];

        VMPCRandomGenerator seedRandGen = new VMPCRandomGenerator();
        seedRandGen.addSeedMaterial(System.currentTimeMillis());
        seedRandGen.nextBytes(randSeed);

        return randSeed;
    }

    public static byte[] readPemContent(InputStream stream) throws CryptographyException {
        return readPemObject(stream).getContent();
    }

    public static PemObject readPemObject(InputStream stream) throws CryptographyException {
        try (PEMParser pemParser = new PEMParser(new InputStreamReader(stream))) {
            return pemParser.readPemObject();
        } catch (IOException e) {
            throw new CryptographyException("Unable to read PEM object from stream.", e);
        }
    }

    public static void writePemContent(OutputStream stream, String pemType, byte[] pemContent) throws CryptographyException {
        writePemObject(stream, new PemObject(pemType, pemContent));
    }

    public static void writePemObject(OutputStream stream, PemObject pemObj) throws CryptographyException {
        try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(stream))) {
            pemWriter.writeObject(pemObj);
            pemWriter.flush();
        } catch (IOException e) {
            throw new CryptographyException("Unable to write PEM object to stream.", e);
        }
    }
}
