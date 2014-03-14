package gov.hhs.onc.dcdt.testcases.hosting;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.test.impl.AbstractToolFunctionalTests;
import gov.hhs.onc.dcdt.testcases.hosting.impl.HostingTestcaseSubmissionImpl;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultGenerator;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultInfo;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseCertificateResultStep;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultException;
import gov.hhs.onc.dcdt.testcases.results.ToolTestcaseResultStep;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Test(groups = { "dcdt.test.func.testcases.all", "dcdt.test.func.testcases.hosting.all", "dcdt.test.func.testcases.hosting.testcases" })
public class HostingTestcaseFunctionalTests extends AbstractToolFunctionalTests {
    @Resource(name = "certDiscoverySteps")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private List<ToolTestcaseResultStep> certDiscoverySteps;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private HostingTestcaseResultGenerator resultGenerator;

    @Value("${dcdt.test.hosting.dns.addr.bound.direct.addr.1}")
    private MailAddress testDnsAddrBoundDirectAddr1;

    @Value("${dcdt.test.hosting.dns.addr.bound.direct.addr.2}")
    private MailAddress testDnsAddrBoundDirectAddr2;

    @Value("${dcdt.test.hosting.dns.addr.bound.direct.addr.3}")
    private MailAddress testDnsAddrBoundDirectAddr3;

    @Value("${dcdt.test.hosting.dns.no.bound.direct.addr.1}")
    private MailAddress testDnsNoBoundDirectAddr1;

    @Value("${dcdt.test.hosting.dns.domain.bound.direct.addr.1}")
    private MailAddress testDnsDomainBoundDirectAddr1;

    @Value("${dcdt.test.hosting.ldap.addr.bound.direct.addr.1}")
    private MailAddress testLdapAddrBoundDirectAddr1;

    @Value("${dcdt.test.hosting.ldap.addr.bound.direct.addr.2}")
    private MailAddress testLdapAddrBoundDirectAddr2;

    @Value("${dcdt.test.hosting.ldap.addr.bound.direct.addr.3}")
    private MailAddress testLdapAddrBoundDirectAddr3;

    @Value("${dcdt.test.hosting.ldap.addr.bound.direct.addr.4}")
    private MailAddress testLdapAddrBoundDirectAddr4;

    @Value("${dcdt.test.hosting.ldap.domain.bound.direct.addr.1}")
    private MailAddress testLdapDomainBoundDirectAddr1;

    @Value("${dcdt.test.hosting.ldap.no.bound.direct.addr.1}")
    private MailAddress testLdapNoBoundDirectAddr1;

    @Value("${dcdt.test.hosting.dns.addr.bound.common.name.1}")
    private String testDnsAddrBoundCommonName1;

    @Value("${dcdt.test.hosting.dns.addr.bound.common.name.2}")
    private String testDnsAddrBoundCommonName2;

    @Value("${dcdt.test.hosting.dns.domain.bound.common.name.1}")
    private String testDnsDomainBoundCommonName1;

    @Value("${dcdt.test.hosting.ldap.addr.bound.common.name.1}")
    private String testLdapAddrBoundCommonName1;

    @Value("${dcdt.test.hosting.ldap.addr.bound.common.name.2}")
    private String testLdapAddrBoundCommonName2;

    @Value("${dcdt.test.hosting.ldap.addr.bound.common.name.3}")
    private String testLdapAddrBoundCommonName3;

    @Value("${dcdt.test.hosting.ldap.addr.bound.common.name.4}")
    private String testLdapAddrBoundCommonName4;

    @Value("${dcdt.test.hosting.ldap.domain.bound.common.name.1}")
    private String testLdapDomainBoundCommonName1;

    @Test
    public void testHostingTestcaseConfiguration() {
        String hostingTestcaseName;

        for (HostingTestcase hostingTestcase : ToolBeanFactoryUtils.getBeansOfType(this.applicationContext, HostingTestcase.class)) {
            Assert.assertTrue(hostingTestcase.hasName(),
                String.format("Hosting testcase (name=%s) does not have a name.", hostingTestcaseName = hostingTestcase.getName()));
            Assert.assertTrue(hostingTestcase.hasDescription(), String.format("Hosting testcase (name=%s) does not have a description.", hostingTestcaseName));
            Assert.assertTrue(hostingTestcase.hasResult(), String.format("Hosting testcase (name=%s) does not have a result.", hostingTestcaseName));
        }
    }

    @Test
    public void testHostingTestcase1() throws ToolTestcaseResultException {
        String hostingTestcase1 = "hostingTestcase1";
        testHostingTestcase(hostingTestcase1, this.testDnsAddrBoundDirectAddr1, true, 0, this.testDnsAddrBoundCommonName1);
        testHostingTestcase(hostingTestcase1, this.testDnsAddrBoundDirectAddr2, true, 0, this.testDnsAddrBoundCommonName2);
        testHostingTestcase(hostingTestcase1, this.testDnsAddrBoundDirectAddr3, false, 2);
    }

    @Test
    public void testHostingTestcase2() throws ToolTestcaseResultException {
        String hostingTestcase2 = "hostingTestcase2";
        testHostingTestcase(hostingTestcase2, this.testDnsDomainBoundDirectAddr1, true, 0, this.testDnsDomainBoundCommonName1);
        testHostingTestcase(hostingTestcase2, this.testDnsAddrBoundDirectAddr1, false, 1, this.testDnsAddrBoundCommonName1);
        testHostingTestcase(hostingTestcase2, this.testDnsNoBoundDirectAddr1, false, 1);
    }

    @Test
    public void testHostingTestcase3() throws ToolTestcaseResultException {
        String hostingTestcase3 = "hostingTestcase3";
        testHostingTestcase(hostingTestcase3, this.testLdapAddrBoundDirectAddr1, true, 0, this.testLdapAddrBoundCommonName1);
        testHostingTestcase(hostingTestcase3, this.testLdapAddrBoundDirectAddr2, true, 0, this.testLdapAddrBoundCommonName2);
        testHostingTestcase(hostingTestcase3, this.testLdapAddrBoundDirectAddr3, true, 0, this.testLdapAddrBoundCommonName3);
        testHostingTestcase(hostingTestcase3, this.testLdapAddrBoundDirectAddr4, true, 0, this.testLdapAddrBoundCommonName4);
    }

    @Test
    public void testHostingTestcase4() throws ToolTestcaseResultException {
        testHostingTestcase("hostingTestcase4", this.testLdapDomainBoundDirectAddr1, true, 0, this.testLdapDomainBoundCommonName1);
    }

    @Test
    public void testHostingTestcase5() throws ToolTestcaseResultException {
        String hostingTestcase5 = "hostingTestcase5";
        testHostingTestcase(hostingTestcase5, this.testLdapNoBoundDirectAddr1, false, 5);
        testHostingTestcase(hostingTestcase5, this.testDnsAddrBoundDirectAddr1, false, 1, this.testDnsAddrBoundCommonName1);
    }

    private void testHostingTestcase(String testcaseName, MailAddress directAddress, boolean successExpected, int errorStepPos)
        throws ToolTestcaseResultException {
        testHostingTestcase(testcaseName, directAddress, successExpected, errorStepPos, null);
    }

    private void
        testHostingTestcase(String testcaseName, MailAddress directAddress, boolean successExpected, int errorStepPos, @Nullable String certCommonName)
            throws ToolTestcaseResultException {
        HostingTestcase hostingTestcase = (HostingTestcase) this.applicationContext.getBean(testcaseName);
        HostingTestcaseSubmission hostingTestcaseSubmission = new HostingTestcaseSubmissionImpl();
        hostingTestcaseSubmission.setHostingTestcase(hostingTestcase);
        hostingTestcaseSubmission.setDirectAddress(directAddress);
        this.resultGenerator.setSubmission(hostingTestcaseSubmission);
        this.resultGenerator.generateTestcaseResult(this.certDiscoverySteps);

        HostingTestcaseResult hostingTestcaseResult = hostingTestcase.getResult();
        HostingTestcaseResultInfo resultInfo = hostingTestcaseResult.getResultInfo();
        Assert.assertEquals(resultInfo.isSuccessful(), successExpected);

        assertCertificateProperties(ToolListUtils.getLast(resultInfo.getResults()), certCommonName);
        Assert.assertEquals(this.resultGenerator.getErrorStepPosition(hostingTestcaseResult), errorStepPos);
    }

    private void assertCertificateProperties(ToolTestcaseResultStep lastStep, @Nullable String certCommonName) {
        if (lastStep instanceof ToolTestcaseCertificateResultStep) {
            ToolTestcaseCertificateResultStep certResultStep = ((ToolTestcaseCertificateResultStep) lastStep);

            if (certResultStep.hasCertificateInfo()) {
                CertificateInfo certInfo = certResultStep.getCertificateInfo();
                Assert.assertEquals(certInfo.getSubject().getCommonName(), certCommonName);
                Assert.assertTrue(certInfo.getValidInterval().isValid(new Date()));
            } else {
                Assert.assertNull(certCommonName);
            }
        }
    }
}
