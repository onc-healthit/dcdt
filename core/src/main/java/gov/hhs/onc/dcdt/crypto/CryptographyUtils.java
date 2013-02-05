package gov.hhs.onc.dcdt.crypto;

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
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

public abstract class CryptographyUtils
{
	public final static Provider BOUNCY_CASTLE_PROVIDER = new BouncyCastleProvider();
	
	static
	{
		Security.addProvider(BOUNCY_CASTLE_PROVIDER);
	}
	
	//<editor-fold desc="PEM I/O methods">
	public static void writePemContent(OutputStream stream, String pemType, byte[] pemContent) throws CryptographyException
	{
		writePemObject(stream, new PemObject(pemType, pemContent));
	}
	
	public static void writePemObject(OutputStream stream, PemObject pemObj) throws CryptographyException
	{
		PemWriter pemWriter = new PemWriter(new OutputStreamWriter(stream));
		
		try
		{
			pemWriter.writeObject(pemObj);
			pemWriter.flush();
		}
		catch (IOException e)
		{
			throw new CryptographyException("Unable to write PEM object to stream.", e);
		}
	}
	
	public static byte[] readPemContent(InputStream stream) throws CryptographyException
	{
		return readPemObject(stream).getContent();
	}
	
	public static PemObject readPemObject(InputStream stream) throws CryptographyException
	{
		try
		{
			return new PemReader(new InputStreamReader(stream)).readPemObject();
		}
		catch (IOException e)
		{
			throw new CryptographyException("Unable to read PEM object from stream.", e);
		}
	}
	//</editor-fold>
	
	//<editor-fold desc="Random methods">
	/**
	 * Gets a random number generator instance.
	 * 
	 * @param seedSize size of the seed to use
	 * @return the generator instance
	 * @throws CryptographyException thrown when the generator cannot be created
	 */
	public static SecureRandom getRandom(int seedSize) throws CryptographyException
	{
		return getRandom(generateRandomSeed(seedSize));
	}

	/**
	 * Gets a random number generator instance.
	 * 
	 * @param seed seed to use
	 * @return the generator instance
	 * @throws CryptographyException thrown when the generator cannot be created
	 */
	public static SecureRandom getRandom(byte[] seed) throws CryptographyException
	{
		return new SecureRandom(seed);
	}

	/**
	 * Generates a random seed.
	 * 
	 * @param size size (bytes) of the seed
	 * @return the seed
	 * @throws CryptographyException thrown when the seed cannot be generated
	 */
	public static byte[] generateRandomSeed(int size) throws CryptographyException
	{
		if (size <= 0)
		{
			throw new CryptographyException("Random seed size must be a positive integer: " + size);
		}
		
		byte[] randSeed = new byte[size];
		
		VMPCRandomGenerator seedRandGen = new VMPCRandomGenerator();
		seedRandGen.addSeedMaterial(System.currentTimeMillis());
		seedRandGen.nextBytes(randSeed);
		
		return randSeed;
	}
	//</editor-fold>
}