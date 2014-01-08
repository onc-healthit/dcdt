package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.keys.KeyPairConfig;
import gov.hhs.onc.dcdt.crypto.keys.KeyPairInfo;
import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import javax.annotation.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.crypto.keys.all", "dcdt.test.unit.crypto.utils.x500" }, groups = { "dcdt.test.all", "dcdt.test.unit.all",
    "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.utils.all", "dcdt.test.unit.crypto.utils.keys" })
public class KeyUtilsUnitTests extends ToolTestNgUnitTests {
    @Resource(name = "testCa1KeyPairConfig")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private KeyPairConfig testKeyPairConfig;

    @Test
    public void testGenerateKeyPair() throws CryptographyException {
        KeyPairInfo testKeyPairInfo = KeyUtils.generateKeyPair(this.testKeyPairConfig);

        Assert.assertNotNull(testKeyPairInfo, "Unable to generate key pair information.");
        Assert.assertEquals(testKeyPairInfo.getKeyAlgorithm(), testKeyPairConfig.getKeyAlgorithm(),
            "Key pair configuration and information key algorithms do not match.");
        Assert.assertEquals(testKeyPairInfo.getKeySize(), testKeyPairConfig.getKeySize(),
            "Key pair configuration and information key sizes do not match.");
    }
}
