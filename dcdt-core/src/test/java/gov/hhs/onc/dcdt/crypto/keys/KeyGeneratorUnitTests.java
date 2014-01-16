package gov.hhs.onc.dcdt.crypto.keys;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.keys.all", "dcdt.test.unit.crypto.keys.gen" })
public class KeyGeneratorUnitTests extends ToolTestNgUnitTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private KeyGenerator keyGen;

    @Resource(name = "testKeyConfigCa1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private KeyConfig testKeyConfig;

    @Test
    public void testGenerateKeys() throws CryptographyException {
        KeyInfo testKeyInfo = this.keyGen.generateKeys(this.testKeyConfig);

        Assert.assertNotNull(testKeyInfo, "Unable to generate key information.");
        Assert.assertEquals(testKeyInfo.getKeyAlgorithm(), testKeyConfig.getKeyAlgorithm(), "Key configuration and information key algorithms do not match.");
        Assert.assertEquals(testKeyInfo.getKeySize(), testKeyConfig.getKeySize(), "Key configuration and information key sizes do not match.");
    }
}
