package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.crypto.constants.KeyAlgorithm;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.apache.log4j.Logger;

public abstract class KeyPairUtils
{
	private final static int KEY_PAIR_SEED_SIZE = 32;

	private final static Logger LOGGER = Logger.getLogger(KeyPairUtils.class);

	//<editor-fold desc="Generation methods">
	/**
	 * Generates a RSA key pair.
	 * 
	 * @param keySize size of the keys
	 * @return the key pair
	 * @throws CryptographyException thrown when the key pair cannot be generated
	 */
	public static KeyPair generateKeyPair(int keySize) throws CryptographyException
	{
		return generateKeyPair(keySize, CryptographyUtils.getRandom(KEY_PAIR_SEED_SIZE));
	}

	/**
	 * Generates a RSA key pair.
	 * 
	 * @param keySize size of the keys
	 * @param rand random number generator to use
	 * @return the key pair
	 * @throws CryptographyException thrown when the key pair cannot be generated
	 */
	public static KeyPair generateKeyPair(int keySize, SecureRandom rand) throws CryptographyException
	{
		KeyPairGenerator keyPairGen = getRsaKeyPairGenerator();
		keyPairGen.initialize(keySize, rand);
		
		KeyPair keyPair = keyPairGen.generateKeyPair();
		
		LOGGER.debug("Generated RSA key pair (keySize=" + keySize + ").");
		
		return keyPair;
	}
	//</editor-fold>

	//<editor-fold desc="Factory methods">
	/**
	 * Gets a RSA key pair generator instance.
	 * 
	 * @return the generator instance
	 * @throws CryptographyException thrown when the generator instance cannot be created
	 */
	public static KeyPairGenerator getRsaKeyPairGenerator() throws CryptographyException
	{
		try
		{
			return KeyPairGenerator.getInstance(KeyAlgorithm.RSA, CryptographyUtils.BOUNCY_CASTLE_PROVIDER);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new CryptographyException("Unable to get RSA key pair generator (algorithm=" + KeyAlgorithm.RSA + 
				") instance from the BouncyCastle provider (name=" + CryptographyUtils.BOUNCY_CASTLE_PROVIDER.getName() + 
				").", e);
		}
	}
	//</editor-fold>
}