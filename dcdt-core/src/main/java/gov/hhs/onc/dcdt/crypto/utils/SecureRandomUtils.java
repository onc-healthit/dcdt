package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import java.security.SecureRandom;
import org.bouncycastle.crypto.prng.VMPCRandomGenerator;

public abstract class SecureRandomUtils {
    public static SecureRandom getRandom(int seedSize) throws CryptographyException {
        return getRandom(generateRandomSeed(seedSize));
    }

    public static SecureRandom getRandom(byte[] seed) throws CryptographyException {
        return new SecureRandom(seed);
    }

    public static byte[] generateRandomSeed(int randSeedSize) throws CryptographyException {
        if (!ToolNumberUtils.isPositive(randSeedSize)) {
            throw new CryptographyException(String.format("Random seed size must be a positive integer: %d <= 0", randSeedSize));
        }

        byte[] randSeed = new byte[randSeedSize];

        VMPCRandomGenerator seedRandGen = new VMPCRandomGenerator();
        seedRandGen.addSeedMaterial(System.currentTimeMillis());
        seedRandGen.nextBytes(randSeed);

        return randSeed;
    }
}
