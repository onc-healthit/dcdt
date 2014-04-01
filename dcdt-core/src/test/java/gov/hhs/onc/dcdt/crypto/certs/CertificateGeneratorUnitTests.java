package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.credentials.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.keys.KeyConfig;
import gov.hhs.onc.dcdt.crypto.keys.KeyGenerator;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.crypto.keys.all" }, groups = { "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.certs.all",
    "dcdt.test.unit.crypto.certs.gen" })
public class CertificateGeneratorUnitTests extends AbstractToolUnitTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private KeyGenerator keyGen;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificateGenerator certGen;

    @Resource(name = "testCredConfigCa1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testCredConfigCa1;

    @Resource(name = "testCredConfigAddr1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testCredConfigAddr1;

    @Resource(name = "testCredConfigDomain1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testCredConfigDomain1;

    private CredentialInfo testCredInfoCa1;

    @Test(dependsOnMethods = { "testGenerateCaCertificate" })
    public void testGenerateDomainBoundCertificate() throws Exception {
        KeyInfo testDomain1KeyPairInfo = this.keyGen.generateKeys(this.testCredConfigDomain1.getKeyDescriptor());

        CredentialInfo testDomain1CredInfo =
            new CredentialInfoImpl(testDomain1KeyPairInfo, this.certGen.generateCertificate(this.testCredInfoCa1, testDomain1KeyPairInfo,
                this.testCredConfigDomain1.getCertificateDescriptor()));

        assertCredentialDescriptorsMatch(this.testCredConfigDomain1, testDomain1CredInfo);
    }

    @Test(dependsOnMethods = { "testGenerateCaCertificate" })
    public void testGenerateAddressBoundCertificate() throws Exception {
        KeyInfo testAddr1KeyPairInfo = this.keyGen.generateKeys(this.testCredConfigAddr1.getKeyDescriptor());

        CredentialInfo testAddr1CredInfo =
            new CredentialInfoImpl(testAddr1KeyPairInfo, this.certGen.generateCertificate(this.testCredInfoCa1, testAddr1KeyPairInfo,
                this.testCredConfigAddr1.getCertificateDescriptor()));

        assertCredentialDescriptorsMatch(this.testCredConfigAddr1, testAddr1CredInfo);
    }

    @Test
    public void testGenerateCaCertificate() throws Exception {
        KeyInfo testCa1KeyPairInfo = this.keyGen.generateKeys(this.testCredConfigCa1.getKeyDescriptor());

        this.testCredInfoCa1 =
            new CredentialInfoImpl(testCa1KeyPairInfo, this.certGen.generateCertificate(testCa1KeyPairInfo, this.testCredConfigCa1.getCertificateDescriptor()));

        assertCredentialDescriptorsMatch(this.testCredConfigCa1, this.testCredInfoCa1);
    }

    private static void assertCredentialDescriptorsMatch(CredentialConfig testCredConfig, CredentialInfo testCredInfo) throws Exception {
        assertKeyDescriptorsMatch(testCredConfig.getKeyDescriptor(), testCredInfo.getKeyDescriptor());
        assertCertificateDescriptorsMatch(testCredConfig.getCertificateDescriptor(), testCredInfo.getCertificateDescriptor());
    }

    private static void assertKeyDescriptorsMatch(KeyConfig testKeyConfig, KeyInfo testKeyInfo) {
        Assert.assertSame(testKeyInfo.getKeyAlgorithm(), testKeyConfig.getKeyAlgorithm(), "Key configuration and information key algorithms do not match.");
        Assert.assertEquals(testKeyInfo.getKeySize(), testKeyConfig.getKeySize(), "Key configuration and information key sizes do not match.");
    }

    private static void assertCertificateDescriptorsMatch(CertificateConfig testCertConfig, CertificateInfo testCertInfo) throws Exception {
        Assert.assertSame(testCertInfo.getCertificateType(), testCertConfig.getCertificateType(),
            "Certificate configuration and information certificate types do not match.");
        Assert.assertSame(testCertInfo.getSignatureAlgorithm(), testCertConfig.getSignatureAlgorithm(),
            "Certificate configuration and information signature algorithms do not match.");
        Assert.assertEquals(testCertInfo.getSubject(), testCertConfig.getSubject(), "Certificate configuration and information subjects do not match.");
    }
}
