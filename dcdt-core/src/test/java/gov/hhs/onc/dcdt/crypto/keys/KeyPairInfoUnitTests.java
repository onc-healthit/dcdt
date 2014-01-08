package gov.hhs.onc.dcdt.crypto.keys;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.keys.impl.KeyPairInfoImpl;
import gov.hhs.onc.dcdt.crypto.utils.KeyUtils;
import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.keys.all", "dcdt.test.unit.crypto.keys.info" })
public class KeyPairInfoUnitTests extends ToolTestNgUnitTests {
    @Value("${dcdt.test.crypto.key.pair.1.key.public}")
    private String testKeyPairPublicKeyStr;

    @Value("${dcdt.test.crypto.key.pair.1.key.private}")
    private String testKeyPairPrivateKeyStr;

    private KeyPairInfo testKeyPairInfo;

    @Test
    public void testGetAuthorityKeyId() {
        Assert.assertNotNull(this.testKeyPairInfo.getAuthorityKeyId(), "Key pair authority key ID is null.");
    }

    @Test
    public void testGetPrivateKeyInfo() {
        Assert.assertNotNull(this.testKeyPairInfo.getPrivateKeyInfo(), "Key pair private key information is null.");
    }

    @Test
    public void testGetSubjectKeyId() {
        Assert.assertNotNull(this.testKeyPairInfo.getSubjectKeyId(), "Key pair subject key ID is null.");
    }

    @Test
    public void testGetSubjectPublicKeyInfo() {
        Assert.assertNotNull(this.testKeyPairInfo.getSubjectPublicKeyInfo(), "Key pair subject public key information is null.");
    }

    @BeforeClass
    public void buildKeyPairInfo() throws CryptographyException {
        this.testKeyPairInfo = new KeyPairInfoImpl(KeyUtils.readPublicKey(getKeyPairKeyData(this.testKeyPairPublicKeyStr), KeyAlgorithm.RSA, DataEncoding.DER),
            KeyUtils.readPrivateKey(getKeyPairKeyData(this.testKeyPairPrivateKeyStr), KeyAlgorithm.RSA, DataEncoding.DER));
    }

    private static byte[] getKeyPairKeyData(String testKeyPairKeyStr) {
        return Base64.decodeBase64(testKeyPairKeyStr);
    }
}
