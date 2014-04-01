package gov.hhs.onc.dcdt.testcases.hosting;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.test.impl.AbstractToolFunctionalTests;
import gov.hhs.onc.dcdt.testcases.hosting.impl.HostingTestcaseSubmissionImpl;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseCertificateStep;
import gov.hhs.onc.dcdt.testcases.steps.ToolTestcaseStep;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.func.testcases.all", "dcdt.test.func.testcases.hosting.all", "dcdt.test.func.testcases.hosting.testcases" })
public class HostingTestcaseFunctionalTests extends AbstractToolFunctionalTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private HostingTestcaseProcessor testcaseProcessor;

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

    @Value("${dcdt.test.lookup.domain.1.name}")
    private MailAddress testDnsDomainBoundDirectAddrDomain;

    @Value("${dcdt.test.lookup.domain.2.name}")
    private MailAddress testLdapDomainBoundDirectAddrDomain;

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

    private final static String CERT_DISCOVERY_STEPS = "certDiscoverySteps";

    @Test
    public void testHostingTestcaseConfiguration() {
        String hostingTestcaseName;

        for (HostingTestcase hostingTestcase : ToolBeanFactoryUtils.getBeansOfType(this.applicationContext, HostingTestcase.class)) {
            Assert.assertTrue(hostingTestcase.hasName(),
                String.format("Hosting testcase (name=%s) does not have a name.", hostingTestcaseName = hostingTestcase.getName()));
            Assert.assertTrue(hostingTestcase.hasDescription(), String.format("Hosting testcase (name=%s) does not have a description.", hostingTestcaseName));
            Assert.assertTrue(hostingTestcase.hasConfig(), String.format("Hosting testcase (name=%s) does not have a config.", hostingTestcaseName));
        }
    }

    @Test
    public void testHostingTestcase1() throws Exception {
        String hostingTestcase1 = "hostingTestcase1";
        testHostingTestcase(hostingTestcase1, this.testDnsAddrBoundDirectAddr1, true, 0, this.testDnsAddrBoundCommonName1);
        testHostingTestcase(hostingTestcase1, this.testDnsAddrBoundDirectAddr2, true, 0, this.testDnsAddrBoundCommonName2);
        testHostingTestcase(hostingTestcase1, this.testDnsAddrBoundDirectAddr3, false, 2);
        testHostingTestcase(hostingTestcase1, this.testDnsDomainBoundDirectAddrDomain, false, 1, this.testDnsDomainBoundCommonName1);
    }

    @Test
    public void testHostingTestcase2() throws Exception {
        String hostingTestcase2 = "hostingTestcase2";
        testHostingTestcase(hostingTestcase2, this.testDnsDomainBoundDirectAddr1, true, 0, this.testDnsDomainBoundCommonName1);
        testHostingTestcase(hostingTestcase2, this.testDnsAddrBoundDirectAddr1, false, 1, this.testDnsAddrBoundCommonName1);
        testHostingTestcase(hostingTestcase2, this.testDnsNoBoundDirectAddr1, false, 1);
        testHostingTestcase(hostingTestcase2, this.testDnsDomainBoundDirectAddrDomain, true, 0, this.testDnsDomainBoundCommonName1);
    }

    @Test
    public void testHostingTestcase3() throws Exception {
        String hostingTestcase3 = "hostingTestcase3";
        testHostingTestcase(hostingTestcase3, this.testLdapAddrBoundDirectAddr1, true, 0, this.testLdapAddrBoundCommonName1);
        testHostingTestcase(hostingTestcase3, this.testLdapAddrBoundDirectAddr2, true, 0, this.testLdapAddrBoundCommonName2);
        testHostingTestcase(hostingTestcase3, this.testLdapAddrBoundDirectAddr3, true, 0, this.testLdapAddrBoundCommonName3);
        testHostingTestcase(hostingTestcase3, this.testLdapAddrBoundDirectAddr4, true, 0, this.testLdapAddrBoundCommonName4);
        testHostingTestcase(hostingTestcase3, this.testLdapDomainBoundDirectAddrDomain, false, 7, this.testLdapDomainBoundCommonName1);
    }

    @Test
    public void testHostingTestcase4() throws Exception {
        String hostingTestcase4 = "hostingTestcase4";
        testHostingTestcase(hostingTestcase4, this.testLdapDomainBoundDirectAddr1, true, 0, this.testLdapDomainBoundCommonName1);
        testHostingTestcase(hostingTestcase4, this.testLdapDomainBoundDirectAddrDomain, true, 0, this.testLdapDomainBoundCommonName1);
    }

    @Test
    public void testHostingTestcase5() throws Exception {
        String hostingTestcase5 = "hostingTestcase5";
        testHostingTestcase(hostingTestcase5, this.testLdapNoBoundDirectAddr1, false, 5);
        testHostingTestcase(hostingTestcase5, this.testDnsAddrBoundDirectAddr1, false, 1, this.testDnsAddrBoundCommonName1);
    }

    private void testHostingTestcase(String testcaseName, MailAddress directAddress, boolean successExpected, int errorStepPos) throws Exception {
        testHostingTestcase(testcaseName, directAddress, successExpected, errorStepPos, null);
    }

    @SuppressWarnings({ "unchecked" })
    private void
        testHostingTestcase(String testcaseName, MailAddress directAddress, boolean successExpected, int errorStepPos, @Nullable String certCommonName)
            throws Exception {
        HostingTestcase hostingTestcase = this.applicationContext.getBean(testcaseName, HostingTestcase.class);
        HostingTestcaseSubmission hostingTestcaseSubmission = new HostingTestcaseSubmissionImpl();
        hostingTestcaseSubmission.setTestcase(hostingTestcase);
        hostingTestcaseSubmission.setDirectAddress(directAddress);

        List<ToolTestcaseStep> steps = (List<ToolTestcaseStep>) this.applicationContext.getBean(CERT_DISCOVERY_STEPS);
        HostingTestcaseResult result = this.testcaseProcessor.generateTestcaseResult(hostingTestcaseSubmission, steps);
        Assert.assertEquals(result.isSuccessful(), successExpected);

        assertCertificateProperties(ToolListUtils.getLast(result.getInfoSteps()), certCommonName);
        Assert.assertEquals(this.testcaseProcessor.getErrorStepPosition(hostingTestcase.getConfig(), result), errorStepPos);
    }

    private void assertCertificateProperties(ToolTestcaseStep lastStep, @Nullable String certCommonName) throws Exception {
        if (lastStep instanceof ToolTestcaseCertificateStep) {
            ToolTestcaseCertificateStep certInfoStep = ((ToolTestcaseCertificateStep) lastStep);

            if (certInfoStep.hasCertificateInfo()) {
                CertificateInfo certInfo = certInfoStep.getCertificateInfo();
                // noinspection ConstantConditions
                Assert.assertEquals(certInfo.getSubject().getCommonName(), certCommonName);
                // noinspection ConstantConditions
                Assert.assertTrue(certInfo.getValidInterval().isValid(new Date()));
            }
        }
    }
}
