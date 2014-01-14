package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateNameImpl;
import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.crypto.keys.all" }, groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all",
    "dcdt.test.unit.crypto.certs.all", "dcdt.test.unit.crypto.certs.name" })
public class CertificateNameUnitTests extends ToolTestNgUnitTests {
    @Value("${dcdt.test.crypto.ca.1.subject.x500.name}")
    private String testCa1CertSubjX500NameStr;

    @Value("${dcdt.test.crypto.addr.1.subject.x500.name}")
    private String testAddr1CertSubjX500NameStr;

    @Value("${dcdt.test.crypto.domain.1.subject.x500.name}")
    private String testDomain1CertSubjX500NameStr;

    @Resource(name = "testCa1CertConfig")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificateConfig testCa1CertConfig;

    @Resource(name = "testAddr1CertConfig")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificateConfig testAddr1CertConfig;

    @Resource(name = "testDomain1CertConfig")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificateConfig testDomain1CertConfig;

    @Test
    public void testX500Name() {
        assertCertificateSubjectsMatch(this.testCa1CertConfig, this.testCa1CertSubjX500NameStr);
        assertCertificateSubjectsMatch(this.testAddr1CertConfig, this.testAddr1CertSubjX500NameStr);
        assertCertificateSubjectsMatch(this.testDomain1CertConfig, this.testDomain1CertSubjX500NameStr);
    }

    private static void assertCertificateSubjectsMatch(CertificateConfig testCertConfig, String testCertSubjX500NameStr) {
        CertificateName testCertSubj = testCertConfig.getSubject();

        Assert.assertNotNull(testCertSubj, "Certificate subject is null.");
        Assert.assertEquals(testCertSubj, new CertificateNameImpl(testCertSubjX500NameStr), "Certificate subjects do not match.");
    }
}
