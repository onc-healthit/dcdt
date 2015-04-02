package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.ToolMailAddressException;
import gov.hhs.onc.dcdt.test.impl.AbstractToolFunctionalTests;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xbill.DNS.Name;

@Test(dependsOnGroups = { "dcdt.test.func.config.all" }, groups = { "dcdt.test.func.testcases.all", "dcdt.test.func.testcases.discovery.all",
    "dcdt.test.func.testcases.discovery.testcases" })
public class DiscoveryTestcaseFunctionalTests extends AbstractToolFunctionalTests {
    private List<DiscoveryTestcase> discoveryTestcases;

    @Test(dependsOnMethods = { "testDiscoveryTestcaseConfigurations" })
    public void testDiscoveryTestcaseMailAddresses() throws DnsException, ToolMailAddressException {
        // noinspection ConstantConditions
        Name instanceDomainName =
            ToolDnsNameUtils.toAbsolute(ToolBeanFactoryUtils.getBeanOfType(this.applicationContext.getBeanFactory(), InstanceConfig.class).getDomainName()), discoveryTestcaseMailAddrName;
        MailAddress discoveryTestcaseMailAddr;

        for (DiscoveryTestcase discoveryTestcase : this.discoveryTestcases) {
            // noinspection ConstantConditions
            Assert.assertTrue(
                (discoveryTestcaseMailAddrName = ToolDnsNameUtils.toAbsolute((discoveryTestcaseMailAddr = discoveryTestcase.getMailAddress()).toAddressName()))
                    .subdomain(instanceDomainName), String.format(
                    "Discovery testcase (name=%s) mail address (%s) DNS name (%s) is not a subdomain of the instance configuration domain name (%s).",
                    discoveryTestcase.getName(), discoveryTestcaseMailAddr, discoveryTestcaseMailAddrName, instanceDomainName));

            if (discoveryTestcase.hasCredentials()) {
                Name discoveryTestcaseCredLocMailAddrName;
                MailAddress discoveryTestcaseCredLocMailAddr;

                // noinspection ConstantConditions
                for (DiscoveryTestcaseCredential discoveryTestcaseCred : discoveryTestcase.getCredentials()) {
                    if (discoveryTestcaseCred.hasLocation()) {
                        // noinspection ConstantConditions
                        Assert
                            .assertTrue(
                                (discoveryTestcaseCredLocMailAddrName =
                                    ToolDnsNameUtils.toAbsolute((discoveryTestcaseCredLocMailAddr = discoveryTestcaseCred.getLocation().getMailAddress())
                                        .toAddressName())).subdomain(instanceDomainName),
                                String
                                    .format(
                                        "Discovery testcase (name=%s) credential (name=%s) location mail address (%s) DNS name (%s) is not a subdomain of the instance configuration domain name (%s).",
                                        discoveryTestcase.getName(), discoveryTestcaseCred.getName(), discoveryTestcaseCredLocMailAddr,
                                        discoveryTestcaseCredLocMailAddrName, instanceDomainName));
                    }
                }
            }
        }
    }

    @Test
    public void testDiscoveryTestcaseConfigurations() {
        String discoveryTestcaseName, discoveryTestcaseCredName;

        for (DiscoveryTestcase discoveryTestcase : (this.discoveryTestcases =
            ToolBeanFactoryUtils.getBeansOfType(this.applicationContext.getBeanFactory(), DiscoveryTestcase.class))) {
            Assert.assertTrue(discoveryTestcase.hasName(),
                String.format("Discovery testcase (name=%s) does not have a name.", (discoveryTestcaseName = discoveryTestcase.getName())));
            Assert.assertTrue(discoveryTestcase.hasDescription(),
                String.format("Discovery testcase (name=%s) does not have a description.", discoveryTestcaseName));
            Assert.assertTrue(discoveryTestcase.hasMailAddress(),
                String.format("Discovery testcase (name=%s) does not have a mail address.", discoveryTestcaseName));

            if (!discoveryTestcase.isNegative()) {
                Assert.assertTrue(discoveryTestcase.hasCredentials(),
                    String.format("Discovery testcase (name=%s) does not have any credentials.", discoveryTestcaseName));

                // noinspection ConstantConditions
                for (DiscoveryTestcaseCredential discoveryTestcaseCred : discoveryTestcase.getCredentials()) {
                    Assert.assertTrue(discoveryTestcaseCred.hasLocation(), String.format(
                        "Discovery testcase (name=%s) credential (name=%s) does not have a location.", discoveryTestcaseName, (discoveryTestcaseCredName =
                            discoveryTestcaseCred.getName())));
                    // noinspection ConstantConditions
                    Assert.assertTrue(discoveryTestcaseCred.getLocation().hasMailAddress(), String.format(
                        "Discovery testcase (name=%s) credential (name=%s) location does not have a mail address.", discoveryTestcaseName,
                        discoveryTestcaseCredName));
                }
            }
        }
    }
}
