package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.certs.CertificateConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.keys.KeyPairConfig;
import gov.hhs.onc.dcdt.crypto.keys.KeyPairInfo;
import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import javax.annotation.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.crypto.keys.all", "dcdt.test.unit.crypto.certs.all", "dcdt.test.unit.crypto.utils.x500",
    "dcdt.test.unit.crypto.utils.keys" }, groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.utils.all",
    "dcdt.test.unit.crypto.utils.certs" })
public class CertificateUtilsUnitTests extends ToolTestNgUnitTests {
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
        KeyPairInfo testDomain1KeyPairInfo = KeyUtils.generateKeyPair(this.testDomain1CredConfig.getKeyPairDescriptor());

        this.testDomain1CredInfo = new CredentialInfoImpl(testDomain1KeyPairInfo, CertificateUtils.generateCertificate(this.testCa1CredInfo,
            testDomain1KeyPairInfo, this.testDomain1CredConfig.getCertificateDescriptor()));

        assertCredentialDescriptorsMatch(this.testDomain1CredConfig, this.testDomain1CredInfo);
    }

    @Test(dependsOnMethods = { "testGenerateCaCertificate" })
    public void testGenerateAddressBoundCertificate() throws CryptographyException {
        KeyPairInfo testAddr1KeyPairInfo = KeyUtils.generateKeyPair(this.testAddr1CredConfig.getKeyPairDescriptor());

        this.testAddr1CredInfo = new CredentialInfoImpl(testAddr1KeyPairInfo, CertificateUtils.generateCertificate(this.testCa1CredInfo, testAddr1KeyPairInfo,
            this.testAddr1CredConfig.getCertificateDescriptor()));

        assertCredentialDescriptorsMatch(this.testAddr1CredConfig, this.testAddr1CredInfo);
    }

    @Test
    public void testGenerateCaCertificate() throws CryptographyException {
        KeyPairInfo testCa1KeyPairInfo = KeyUtils.generateKeyPair(this.testCa1CredConfig.getKeyPairDescriptor());

        this.testCa1CredInfo = new CredentialInfoImpl(testCa1KeyPairInfo, CertificateUtils.generateCertificate(testCa1KeyPairInfo,
            this.testCa1CredConfig.getCertificateDescriptor()));

        assertCredentialDescriptorsMatch(this.testCa1CredConfig, this.testCa1CredInfo);
    }

    private static void assertCredentialDescriptorsMatch(CredentialConfig testCredConfig, CredentialInfo testCredInfo) {
        assertKeyPairDescriptorsMatch(testCredConfig.getKeyPairDescriptor(), testCredInfo.getKeyPairDescriptor());
        assertCertificateDescriptorsMatch(testCredConfig.getCertificateDescriptor(), testCredInfo.getCertificateDescriptor());
    }

    private static void assertKeyPairDescriptorsMatch(KeyPairConfig testKeyPairConfig, KeyPairInfo testKeyPairInfo) {
        Assert.assertSame(testKeyPairInfo.getKeyAlgorithm(), testKeyPairConfig.getKeyAlgorithm(),
            "Key pair configuration and information key algorithms do not match.");
        Assert.assertEquals(testKeyPairInfo.getKeySize(), testKeyPairConfig.getKeySize(), "Key pair configuration and information key sizes do not match.");
    }

    private static void assertCertificateDescriptorsMatch(CertificateConfig testCertConfig, CertificateInfo testCertInfo) {
        Assert.assertSame(testCertInfo.getCertificateType(), testCertConfig.getCertificateType(),
            "Certificate configuration and information certificate types do not match.");
        Assert.assertSame(testCertInfo.getSignatureAlgorithm(), testCertConfig.getSignatureAlgorithm(),
            "Certificate configuration and information signature algorithms do not match.");
        Assert.assertEquals(testCertInfo.getSubject(), testCertConfig.getSubject(), "Certificate configuration and information subjects do not match.");
    }
}
