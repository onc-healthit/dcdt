package gov.hhs.onc.dcdt.crypto;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import gov.hhs.onc.dcdt.crypto.constants.DataEncoding;
import gov.hhs.onc.dcdt.crypto.constants.KeyAlgorithm;
import gov.hhs.onc.dcdt.crypto.utils.KeyPairUtils;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.key" })
public class KeyPairUtilsTest {
    private PrivateKey privateKey;

    @Test
    public void testGenerateKeyPair() throws CryptographyException {
        KeyPair keyPair = KeyPairUtils.generateKeyPair(1024);
        privateKey = keyPair.getPrivate();
        Assert.assertNotNull(keyPair);
        Assert.assertEquals(keyPair.getPublic().getAlgorithm(), KeyAlgorithm.RSA.getAlgorithm());
        Assert.assertEquals(keyPair.getPrivate().getAlgorithm(), KeyAlgorithm.RSA.getAlgorithm());
    }

    @Test(dataProvider = "encoding", dependsOnMethods = "testGenerateKeyPair")
    public void testWriteReadPrivateKey(String fileName, DataEncoding encoding) throws CryptographyException {
        try {
            File tempFile = File.createTempFile(fileName, "." + encoding.getEncoding());
            KeyPairUtils.writePrivateKey(tempFile, privateKey, encoding);
            PrivateKey privateKeyRead = KeyPairUtils.readPrivateKey(tempFile, encoding);

            Assert.assertTrue(tempFile.delete());
            Assert.assertEquals(privateKeyRead.getEncoded(), privateKey.getEncoded());
            Assert.assertEquals(privateKeyRead.getAlgorithm(), privateKey.getAlgorithm());
        } catch (IOException e) {
            Assert.fail("Could not write " + encoding.toString() + "-encoded private key to temporary file.", e);
        }
    }

    @DataProvider(name = "encoding")
    private Object[][] parameterEncodingTestProvider() {
        return new Object[][] { { "testPrivateKey", DataEncoding.DER }, { "testPrivateKey", DataEncoding.PEM } };
    }
}
