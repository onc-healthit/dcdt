package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateNameImpl;
import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import javax.annotation.Resource;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.crypto.keys.all" }, groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all",
    "dcdt.test.unit.crypto.certs.all", "dcdt.test.unit.crypto.certs.name" })
public class CertificateNameUnitTests extends ToolTestNgUnitTests {
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

    @Test
    public void testX500Name() {
        assertCertificateSubjectsMatch(this.testCertConfigCa1, this.testCertSubjX500NameCa1);
        assertCertificateSubjectsMatch(this.testCertConfigAddr1, this.testCertSubjX500NameAddr1);
        assertCertificateSubjectsMatch(this.testCertConfigDomain1, this.testCertSubjX500NameDomain1);
    }

    private static void assertCertificateSubjectsMatch(CertificateConfig testCertConfig, X500Name testCertSubjX500Name) {
        CertificateName testCertSubj = testCertConfig.getSubject();

        Assert.assertNotNull(testCertSubj, "Certificate subject is null.");
        Assert.assertEquals(testCertSubj, new CertificateNameImpl(testCertSubjX500Name), "Certificate subjects do not match.");
    }
}
