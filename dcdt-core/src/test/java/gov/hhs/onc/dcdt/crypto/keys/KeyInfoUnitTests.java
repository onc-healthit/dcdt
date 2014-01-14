package gov.hhs.onc.dcdt.crypto.keys;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.keys.impl.KeyInfoImpl;
import gov.hhs.onc.dcdt.crypto.utils.KeyUtils;
import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.keys.all", "dcdt.test.unit.crypto.keys.info" })
public class KeyInfoUnitTests extends ToolTestNgUnitTests {
    @Value("${dcdt.test.crypto.keys.1.key.public}")
    private String testKeysPublicKeyStr;

    @Value("${dcdt.test.crypto.keys.1.key.private}")
    private String testKeysPrivateKeyStr;

    private KeyInfo testKeyInfo;

    @Test
    public void testGetAuthorityKeyId() {
        Assert.assertNotNull(this.testKeyInfo.getAuthorityKeyId(), "Key pair authority key ID is null.");
    }

    @Test
    public void testGetPrivateKeyInfo() {
        Assert.assertNotNull(this.testKeyInfo.getPrivateKeyInfo(), "Key pair private key information is null.");
    }

    @Test
    public void testGetSubjectKeyId() {
        Assert.assertNotNull(this.testKeyInfo.getSubjectKeyId(), "Key pair subject key ID is null.");
    }

    @Test
    public void testGetSubjectPublicKeyInfo() {
        Assert.assertNotNull(this.testKeyInfo.getSubjectPublicKeyInfo(), "Key pair subject public key information is null.");
    }

    @BeforeClass
    public void buildKeyInfo() throws CryptographyException {
        this.testKeyInfo = new KeyInfoImpl(KeyUtils.readPublicKey(getKeyData(this.testKeysPublicKeyStr), KeyAlgorithm.RSA, DataEncoding.DER),
            KeyUtils.readPrivateKey(getKeyData(this.testKeysPrivateKeyStr), KeyAlgorithm.RSA, DataEncoding.DER));
    }

    private static byte[] getKeyData(String testKeysKeyStr) {
        return Base64.decodeBase64(testKeysKeyStr);
    }
}
