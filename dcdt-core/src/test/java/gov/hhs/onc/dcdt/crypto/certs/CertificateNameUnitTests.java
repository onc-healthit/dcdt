package gov.hhs.onc.dcdt.crypto.certs;

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
import org.apache.commons.lang3.tuple.Pair;
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

    @Resource(name = "testCredConfigDomain1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testCredConfigDomain1;

    @Resource(name = "testCertConfigAddr1SubjAltName")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private GeneralNames subjAltNamesAddr1;

    @Resource(name = "testCertConfigDomain1SubjAltName")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private GeneralNames subjAltNamesDomain1;

    @Value("${dcdt.test.instance.direct.addr.1}")
    private MailAddress testDirectAddr1;

    @Value("${dcdt.test.instance.direct.addr.2}")
    private MailAddress testDirectAddr2;

    private CertificateInfo testCertInfoAddr1;
    private CertificateInfo testCertInfoAddr2;
    private CertificateInfo testCertInfoDomain1;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificateInfoValidator certInfoValidator;

    @BeforeClass
    public void setupCertificateInfos() throws Exception {
        KeyInfo testCa1KeyPairInfo = this.keyGen.generateKeys(this.testCredConfigCa1.getKeyDescriptor());
        CredentialInfo testCredInfoCa1 =
            new CredentialInfoImpl(testCa1KeyPairInfo, this.certGen.generateCertificate(testCa1KeyPairInfo, this.testCredConfigCa1.getCertificateDescriptor()));

        this.testCertInfoAddr1 = this.generateCertificateInfo(testCredInfoCa1, this.testCredConfigAddr1);
        this.testCertInfoAddr2 = this.generateCertificateInfo(testCredInfoCa1, this.testCredConfigAddr2);
        this.testCertInfoDomain1 = this.generateCertificateInfo(testCredInfoCa1, this.testCredConfigDomain1);
    }

    @Test
    public void testX500Name() throws Exception {
        assertCertificateSubjectsMatch(this.testCertConfigCa1, this.testCertSubjX500NameCa1, null);
        assertCertificateSubjectsMatch(this.testCertConfigAddr1, this.testCertSubjX500NameAddr1, this.subjAltNamesAddr1);
        assertCertificateSubjectsMatch(this.testCertConfigDomain1, this.testCertSubjX500NameDomain1, this.subjAltNamesDomain1);
    }

    @Test
    public void testRfc822NamePresentInAddressBoundCertificate() throws Exception {
        assertCertificateSubjectAltNameValues(this.testDirectAddr1, this.testCertInfoAddr1);
    }

    @Test
    public void testDNSNamePresentInDomainBoundCertificate() throws Exception {
        assertCertificateSubjectAltNameValues(this.testDirectAddr1, this.testCertInfoDomain1);
    }

    @Test
    public void testNoRfc822OrDNSNamePresentInCertificate() throws Exception {
        // noinspection ConstantConditions
        Assert.assertFalse(this.testCertInfoAddr2.getSubjectName().hasAltNames(), "Certificate has subject alternative names.");

        Pair<Boolean, List<String>> result = this.certInfoValidator.validate(this.testDirectAddr2, this.testCertInfoAddr2);
        List<String> errorMsgs = result.getRight();

        Assert.assertFalse(result.getLeft(), "Certificate is valid.");
        Assert.assertEquals(errorMsgs.size(), 1, String.format("Certificate validation result has %d error messages.", errorMsgs.size()));
        Assert.assertTrue(errorMsgs.get(0).contains("subjectAltName X509v3 extension does not contain a rfc822Name or a dNSName"),
            "subjectAltName X509v3 extension contains a rfc822Name or a dNSName.");
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

    private void assertCertificateSubjectAltNameValues(MailAddress directAddr, CertificateInfo certInfo) throws Exception {
        // noinspection ConstantConditions
        Assert.assertTrue(certInfo.getSubjectName().hasAltNames(), "Certificate does not have subject alternative names.");

        Pair<Boolean, List<String>> result = this.certInfoValidator.validate(directAddr, certInfo);
        Assert.assertTrue(result.getLeft(), "Certificate is invalid.");
        Assert.assertEquals(result.getRight().size(), 0, "Certificate validation result has error messages.");
    }
}
