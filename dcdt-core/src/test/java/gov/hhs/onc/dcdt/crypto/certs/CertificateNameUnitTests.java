package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.beans.ToolMessage;
import gov.hhs.onc.dcdt.beans.ToolMessageLevel;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateNameImpl;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.credentials.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.keys.KeyGenerator;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.crypto.keys.all", "dcdt.test.unit.crypto.certs.gen" },
    groups = { "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.certs.all", "dcdt.test.unit.crypto.certs.name" })
public class CertificateNameUnitTests extends AbstractToolUnitTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private KeyGenerator keyGen;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificateGenerator certGen;

    @Value("${dcdt.test.crypto.subject.x500.name.ca.1}")
    private X500Name testCertSubjX500NameCa1;

    @Value("${dcdt.test.crypto.subject.x500.name.addr.1}")
    private X500Name testCertSubjX500NameAddr1;

    @Value("${dcdt.test.crypto.subject.x500.name.domain.1}")
    private X500Name testCertSubjX500NameDomain1;

    @Resource(name = "testCertConfigCa1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificateConfig testCertConfigCa1;

    @Resource(name = "testCertConfigAddr1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificateConfig testCertConfigAddr1;

    @Resource(name = "testCertConfigDomain1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificateConfig testCertConfigDomain1;

    @Resource(name = "testCredConfigCa1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testCredConfigCa1;

    @Resource(name = "testCredConfigAddr1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testCredConfigAddr1;

    @Resource(name = "testCredConfigAddr2")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testCredConfigAddr2;

    @Resource(name = "testCredConfigAddr3")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testCredConfigAddr3;

    @Resource(name = "testCredConfigAddr4")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testCredConfigAddr4;

    @Resource(name = "testCredConfigDomain1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testCredConfigDomain1;

    @Resource(name = "testCredConfigDomain2")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testCredConfigDomain2;

    @Resource(name = "testCredConfigDomain3")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testCredConfigDomain3;

    @Resource(name = "testCredConfigDomain4")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testCredConfigDomain4;

    @Resource(name = "testCertConfigAddr1SubjAltName")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private GeneralNames testSubjAltNamesAddr1;

    @Resource(name = "testCertConfigDomain1SubjAltName")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private GeneralNames testSubjAltNamesDomain1;

    @Value("${dcdt.test.instance.direct.addr.1}")
    private MailAddress testDirectAddr1;

    @Value("${dcdt.test.instance.direct.addr.2}")
    private MailAddress testDirectAddr2;

    @Value("${dcdt.test.instance.direct.addr.3}")
    private MailAddress testDirectAddr3;

    @Value("${dcdt.test.instance.direct.addr.4}")
    private MailAddress testDirectAddr4;

    @Value("${dcdt.test.instance.domain.1.name}")
    private MailAddress testDomain1;

    @Value("${dcdt.test.instance.domain.2.name}")
    private MailAddress testDomain2;

    @Value("${dcdt.test.instance.domain.3.name}")
    private MailAddress testDomain3;

    @Value("${dcdt.test.instance.domain.4.name}")
    private MailAddress testDomain4;

    private CredentialInfo testCredInfoCa1;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificateValidator certInfoValidator;

    @BeforeClass
    public void setupCaCredentialInfo() throws Exception {
        KeyInfo testCa1KeyPairInfo = this.keyGen.generateKeys(this.testCredConfigCa1.getKeyDescriptor());
        this.testCredInfoCa1 =
            new CredentialInfoImpl(testCa1KeyPairInfo, this.certGen.generateCertificate(testCa1KeyPairInfo, this.testCredConfigCa1.getCertificateDescriptor()));
    }

    @Test
    public void testX500Name() throws Exception {
        assertCertificateSubjectsMatch(this.testCertConfigCa1, this.testCertSubjX500NameCa1, null);
        assertCertificateSubjectsMatch(this.testCertConfigAddr1, this.testCertSubjX500NameAddr1, this.testSubjAltNamesAddr1);
        assertCertificateSubjectsMatch(this.testCertConfigDomain1, this.testCertSubjX500NameDomain1, this.testSubjAltNamesDomain1);
    }

    @Test
    public void testRfc822NamePresentInAddressBoundCertificate() throws Exception {
        assertCertificateSubjectAltNameValues(this.testDirectAddr1, this.generateCertificateInfo(this.testCredInfoCa1, this.testCredConfigAddr1), 1, true);
    }

    @Test
    public void testDNSNamePresentInDomainBoundCertificate() throws Exception {
        assertCertificateSubjectAltNameValues(this.testDomain1, this.generateCertificateInfo(testCredInfoCa1, this.testCredConfigDomain1), 1, true);
    }

    @Test
    public void testNoRfc822OrDNSNamePresentInCertificate() throws Exception {
        assertCertificateSubjectAltNameValues(this.testDirectAddr2, this.generateCertificateInfo(testCredInfoCa1, this.testCredConfigAddr2), 0, false,
            "subjectAltName X509v3 extension does not contain a rfc822Name or a dNSName");
    }

    @Test
    public void testMultipleRfc822NamesPresentOneMatch() throws Exception {
        assertCertificateSubjectAltNameValues(this.testDirectAddr3, this.generateCertificateInfo(testCredInfoCa1, this.testCredConfigAddr3), 2, true);
    }

    @Test
    public void testMultipleRfc822NamesPresentNoneMatch() throws Exception {
        assertCertificateSubjectAltNameValues(this.testDirectAddr4, this.generateCertificateInfo(testCredInfoCa1, this.testCredConfigAddr4), 2, false,
            String.format("subjectAltName X509v3 extension does not contain a rfc822Name value == %s", this.testDirectAddr4));
    }

    @Test
    public void testMultipleDNSNamesPresentOneMatch() throws Exception {
        assertCertificateSubjectAltNameValues(this.testDomain2, this.generateCertificateInfo(testCredInfoCa1, this.testCredConfigDomain2), 2, true);
    }

    @Test
    public void testMultipleDNSNamesPresentNoneMatch() throws Exception {
        assertCertificateSubjectAltNameValues(this.testDomain3, this.generateCertificateInfo(testCredInfoCa1, this.testCredConfigDomain3), 2, false,
            String.format("subjectAltName X509v3 extension does not contain a dNSName value == %s", this.testDomain3));
    }

    @Test
    public void testRfc822NameAndDNSNamePresentDNSNameMatch() throws Exception {
        assertCertificateSubjectAltNameValues(this.testDomain4, this.generateCertificateInfo(testCredInfoCa1, this.testCredConfigDomain4), 2, true);
    }

    private CertificateInfo generateCertificateInfo(CredentialInfo credInfoCa, CredentialConfig credConfig) throws Exception {
        return this.certGen.generateCertificate(credInfoCa, this.keyGen.generateKeys(credConfig.getKeyDescriptor()), credConfig.getCertificateDescriptor());
    }

    private static void assertCertificateSubjectsMatch(CertificateConfig testCertConfig, X500Name testCertSubjX500Name, @Nullable GeneralNames generalNames)
        throws Exception {
        CertificateName testCertSubj = testCertConfig.getSubjectName();

        Assert.assertNotNull(testCertSubj, "Certificate subject is null.");
        Assert.assertEquals(testCertSubj, new CertificateNameImpl(generalNames, testCertSubjX500Name), "Certificate subjects do not match.");
    }

    private void assertCertificateSubjectAltNameValues(MailAddress directAddr, CertificateInfo certInfo, int numAltNames, boolean isValidCert,
        String ... errorMsgStrs) throws Exception {
        CertificateName certName = certInfo.getSubjectName();

        if (numAltNames > 0) {
            // noinspection ConstantConditions
            Assert.assertTrue(certName.hasAltNames(), "Certificate does not have subject alternative names.");
            // noinspection ConstantConditions
            Assert.assertEquals(certName.getAltNames().getNames().length, numAltNames);
        } else {
            // noinspection ConstantConditions
            Assert.assertFalse(certInfo.getSubjectName().hasAltNames(), "Certificate has subject alternative names.");
        }

        CertificateValidatorContext context = this.certInfoValidator.validate(directAddr, certInfo);
        List<ToolMessage> errorMsgs = context.getMessages(ToolMessageLevel.ERROR);
        int numErrorMsgs = errorMsgs.size();

        if (isValidCert) {
            Assert.assertTrue(context.isSuccess(), "Certificate is invalid.");
            Assert.assertEquals(numErrorMsgs, 0, String.format("Certificate validation result has %d error messages.", numErrorMsgs));
        } else {
            Assert.assertFalse(context.isSuccess(), "Certificate is valid.");
            Assert.assertEquals(numErrorMsgs, 1, String.format("Certificate validation result has %d error messages.", numErrorMsgs));

            if (errorMsgStrs.length > 0) {
                Assert.assertTrue(errorMsgs.get(0).getText().contains(errorMsgStrs[0]));
            }
        }
    }
}
