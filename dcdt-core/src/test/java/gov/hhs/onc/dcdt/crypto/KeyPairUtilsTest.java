package gov.hhs.onc.dcdt.crypto;

import java.security.KeyPair;

import org.testng.Assert;
import org.testng.annotations.Test;

import gov.hhs.onc.dcdt.crypto.constants.KeyAlgorithm;
import gov.hhs.onc.dcdt.crypto.utils.KeyPairUtils;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.key" })
public class KeyPairUtilsTest {
    private KeyPair keyPair;

    @Test
    public void testGenerateKeyPair() throws CryptographyException {
        keyPair = KeyPairUtils.generateKeyPair(1024);
        Assert.assertNotNull(keyPair);
        Assert.assertEquals(keyPair.getPublic().getAlgorithm(), KeyAlgorithm.RSA);
        Assert.assertEquals(keyPair.getPrivate().getAlgorithm(), KeyAlgorithm.RSA);
    }
}
