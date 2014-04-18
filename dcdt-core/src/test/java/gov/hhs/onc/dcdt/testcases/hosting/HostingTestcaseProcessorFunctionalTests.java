package gov.hhs.onc.dcdt.testcases.hosting;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.LocationType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.test.impl.AbstractToolFunctionalTests;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.func.testcases.all", "dcdt.test.func.testcases.hosting.all", "dcdt.test.func.testcases.hosting.proc" })
public class HostingTestcaseProcessorFunctionalTests extends AbstractToolFunctionalTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private HostingTestcaseProcessor hostingTestcaseProc;

    @Value("${dcdt.test.hosting.dns.addr.bound.direct.addr.1}")
    private MailAddress testDnsAddrBoundDirectAddr;

    @Value("${dcdt.test.hosting.dns.no.bound.direct.addr.1}")
    private MailAddress testDnsNoBoundDirectAddr;

    @Value("${dcdt.test.hosting.dns.domain.bound.direct.addr.1}")
    private MailAddress testDnsDomainBoundDirectAddr;

    @Value("${dcdt.test.hosting.ldap.addr.bound.direct.addr.1}")
    private MailAddress testLdapAddrBoundDirectAddr;

    @Value("${dcdt.test.hosting.ldap.domain.bound.direct.addr.1}")
    private MailAddress testLdapDomainBoundDirectAddr;

    @Value("${dcdt.test.hosting.ldap.no.bound.direct.addr.1}")
    private MailAddress testLdapNoBoundDirectAddr;

    @Test
    public void testProcess() {
        HostingTestcaseResult hostingTestcaseResult;

        for (HostingTestcase hostingTestcase : ToolBeanFactoryUtils.getBeansOfType(this.applicationContext, HostingTestcase.class)) {
            Assert.assertTrue((hostingTestcaseResult = this.hostingTestcaseProc.process(this.buildHostingTestcaseSubmission(hostingTestcase))).isSuccess(),
                String.format("Hosting testcase (name=%s, locType=%s, bindingType=%s, neg=%s) processing failed: [%s]", hostingTestcase.getName(),
                    hostingTestcase.getLocationType().name(), hostingTestcase.getBindingType().name(), hostingTestcase.isNegative(),
                    ToolStringUtils.joinDelimit(hostingTestcaseResult.getMessages(), "; ")));
        }
    }

    private HostingTestcaseSubmission buildHostingTestcaseSubmission(HostingTestcase hostingTestcase) {
        LocationType hostingTestcaseLocType = hostingTestcase.getLocationType();
        BindingType hostingTestcaseBindingType = hostingTestcase.getBindingType();
        boolean hostingTestcaseNeg = hostingTestcase.isNegative();
        MailAddress directAddr = null;

        if (hostingTestcaseLocType == LocationType.DNS) {
            if (!hostingTestcaseNeg) {
                if (hostingTestcaseBindingType == BindingType.ADDRESS) {
                    directAddr = this.testDnsAddrBoundDirectAddr;
                } else if (hostingTestcaseBindingType == BindingType.DOMAIN) {
                    directAddr = this.testDnsDomainBoundDirectAddr;
                }
            } else {
                directAddr = this.testDnsNoBoundDirectAddr;
            }
        } else if (hostingTestcaseLocType == LocationType.LDAP) {
            if (!hostingTestcaseNeg) {
                if (hostingTestcaseBindingType == BindingType.ADDRESS) {
                    directAddr = this.testLdapAddrBoundDirectAddr;
                } else if (hostingTestcaseBindingType == BindingType.DOMAIN) {
                    directAddr = this.testLdapDomainBoundDirectAddr;
                }
            } else {
                directAddr = this.testLdapNoBoundDirectAddr;
            }
        }

        Assert.assertNotNull(
            directAddr,
            String.format("Hosting testcase (name=%s, locType=%s, bindingType=%s, neg=%s) test Direct address could not be determined.",
                hostingTestcase.getName(), hostingTestcaseLocType.name(), hostingTestcaseBindingType.name(), hostingTestcaseNeg));

        return ToolBeanFactoryUtils.createBeanOfType(this.applicationContext, HostingTestcaseSubmission.class, hostingTestcase, directAddr);
    }
}
