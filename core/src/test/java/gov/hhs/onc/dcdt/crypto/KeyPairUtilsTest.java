package gov.hhs.onc.dcdt.crypto;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.crypto" }, groups = { "dcdt.all", "dcdt.crypto.all", "dcdt.crypto.keyPair" })
public class KeyPairUtilsTest
{
	@Test
	public void testGenerateKeyPair() throws CryptographyException
	{
		Assert.assertNotNull(KeyPairUtils.generateKeyPair(1024), "Failed to generate key pair.");
	}
}