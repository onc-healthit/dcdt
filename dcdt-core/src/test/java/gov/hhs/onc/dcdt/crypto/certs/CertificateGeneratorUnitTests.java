package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.credentials.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.keys.KeyConfig;
import gov.hhs.onc.dcdt.crypto.keys.KeyGenerator;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.crypto.keys.all" }, groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all",
    "dcdt.test.unit.crypto.certs.all", "dcdt.test.unit.crypto.certs.gen" })
public class CertificateGeneratorUnitTests extends ToolTestNgUnitTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private KeyGenerator keyGen;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificateGenerator certGen;

    @Resource(name = "testCa1CredConfig")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testCa1CredConfig;

    @Resource(name = "testAddr1CredConfig")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testAddr1CredConfig;

    @Resource(name = "testDomain1CredConfig")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testDomain1CredConfig;

    private CredentialInfo testCa1CredInfo, testAddr1CredInfo, testDomain1CredInfo;

    @Test(dependsOnMethods = { "testGenerateCaCertificate" })
    public void testGenerateDomainBoundCertificate() throws CryptographyException {
        KeyInfo testDomain1KeyPairInfo = this.keyGen.generateKeys(this.testDomain1CredConfig.getKeyDescriptor());

        this.testDomain1CredInfo = new CredentialInfoImpl(testDomain1KeyPairInfo, this.certGen.generateCertificate(this.testCa1CredInfo,
            testDomain1KeyPairInfo, this.testDomain1CredConfig.getCertificateDescriptor()));

        assertCredentialDescriptorsMatch(this.testDomain1CredConfig, this.testDomain1CredInfo);
    }

    @Test(dependsOnMethods = { "testGenerateCaCertificate" })
    public void testGenerateAddressBoundCertificate() throws CryptographyException {
        KeyInfo testAddr1KeyPairInfo = this.keyGen.generateKeys(this.testAddr1CredConfig.getKeyDescriptor());

        this.testAddr1CredInfo = new CredentialInfoImpl(testAddr1KeyPairInfo, this.certGen.generateCertificate(this.testCa1CredInfo, testAddr1KeyPairInfo,
            this.testAddr1CredConfig.getCertificateDescriptor()));

        assertCredentialDescriptorsMatch(this.testAddr1CredConfig, this.testAddr1CredInfo);
    }

    @Test
    public void testGenerateCaCertificate() throws CryptographyException {
        KeyInfo testCa1KeyPairInfo = this.keyGen.generateKeys(this.testCa1CredConfig.getKeyDescriptor());

        this.testCa1CredInfo = new CredentialInfoImpl(testCa1KeyPairInfo, this.certGen.generateCertificate(testCa1KeyPairInfo,
            this.testCa1CredConfig.getCertificateDescriptor()));

        assertCredentialDescriptorsMatch(this.testCa1CredConfig, this.testCa1CredInfo);
    }

    private static void assertCredentialDescriptorsMatch(CredentialConfig testCredConfig, CredentialInfo testCredInfo) {
        assertKeyDescriptorsMatch(testCredConfig.getKeyDescriptor(), testCredInfo.getKeyDescriptor());
        assertCertificateDescriptorsMatch(testCredConfig.getCertificateDescriptor(), testCredInfo.getCertificateDescriptor());
    }

    private static void assertKeyDescriptorsMatch(KeyConfig testKeyConfig, KeyInfo testKeyInfo) {
        Assert.assertSame(testKeyInfo.getKeyAlgorithm(), testKeyConfig.getKeyAlgorithm(), "Key configuration and information key algorithms do not match.");
        Assert.assertEquals(testKeyInfo.getKeySize(), testKeyConfig.getKeySize(), "Key configuration and information key sizes do not match.");
    }

    private static void assertCertificateDescriptorsMatch(CertificateConfig testCertConfig, CertificateInfo testCertInfo) {
        Assert.assertSame(testCertInfo.getCertificateType(), testCertConfig.getCertificateType(),
            "Certificate configuration and information certificate types do not match.");
        Assert.assertSame(testCertInfo.getSignatureAlgorithm(), testCertConfig.getSignatureAlgorithm(),
            "Certificate configuration and information signature algorithms do not match.");
        Assert.assertEquals(testCertInfo.getSubject(), testCertConfig.getSubject(), "Certificate configuration and information subjects do not match.");
    }
}
