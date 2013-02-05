package gov.hhs.onc.dcdt.crypto;

import org.apache.commons.lang3.ArrayUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.all", "dcdt.crypto", "dcdt.crypto.all" })
public class CryptographyUtilsTest
{
	private final static int RAND_SEED_SIZE = 16;
	
	private static byte[] seed;
	
	@Test(dependsOnMethods = { "testGenerateRandomSeed" })
	public void testGetRandom() throws CryptographyException
	{
		Assert.assertNotNull(CryptographyUtils.getRandom(seed), "Failed to get random number generator instance.");
	}
	
	@Test
	public void testGenerateRandomSeed() throws CryptographyException
	{
		seed = CryptographyUtils.generateRandomSeed(RAND_SEED_SIZE);
		
		Assert.assertEquals(ArrayUtils.getLength(seed), RAND_SEED_SIZE, "Failed to generate random seed.");
	}
}