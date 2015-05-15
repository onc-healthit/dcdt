package gov.hhs.onc.dcdt.testcases.hosting;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigRegistry;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigService;
import gov.hhs.onc.dcdt.discovery.CertificateDiscoveryService;
import gov.hhs.onc.dcdt.discovery.steps.CertificateValidationStep;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import gov.hhs.onc.dcdt.test.impl.AbstractToolFunctionalTests;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.hosting.HostingTestcase.HostingTestcaseLocationBindingPredicate;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResult;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.net.InetAddress;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xbill.DNS.Name;

@Test(groups = { "dcdt.test.func.testcases.all", "dcdt.test.func.testcases.hosting.all", "dcdt.test.func.testcases.hosting.proc" })
public class HostingTestcaseProcessorFunctionalTests extends AbstractToolFunctionalTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private HostingTestcaseProcessor hostingTestcaseProc;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificateDiscoveryService certDiscoveryService;

    @Value("${dcdt.test.instance.domain.name}")
    private Name testInstanceConfigDomainName;

    @Value("${dcdt.test.instance.ip.addr}")
    private InetAddress testInstanceConfigIpAddr;

    @Value("${dcdt.test.lookup.domain.name}")
    private Name testLookupDomainName;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected InstanceConfigRegistry instanceConfigReg;

    @Test
    public void testProcess() {
        for (DiscoveryTestcase discoveryTestcase : ToolBeanFactoryUtils.getBeansOfType(this.applicationContext, DiscoveryTestcase.class)) {
            // noinspection ConstantConditions
            MailAddress directAddr =
                new MailAddressImpl(discoveryTestcase.getMailAddress().toAddress()
                    .replace(this.testInstanceConfigDomainName.toString(), this.testLookupDomainName.toString()));
            CertificateValidationStep certValidationStep;

            if (discoveryTestcase.hasTargetCredentials()) {
                if (!discoveryTestcase.isNegative()) {
                    DiscoveryTestcaseCredential discoveryTestcaseCred = discoveryTestcase.getTargetCredentials().iterator().next();

                    // noinspection ConstantConditions
                    HostingTestcase hostingTestcase =
                        CollectionUtils.find(this.getHostingTestcases(), new HostingTestcaseLocationBindingPredicate(discoveryTestcaseCred.getLocation()
                            .getType(), discoveryTestcaseCred.getBindingType()));
                    HostingTestcaseResult hostingTestcaseResult =
                        this.hostingTestcaseProc.process(ToolBeanFactoryUtils.createBeanOfType(this.applicationContext, HostingTestcaseSubmission.class,
                            hostingTestcase, directAddr));

                    Assert.assertTrue(
                        hostingTestcaseResult.isSuccess(),
                        String.format("Hosting testcase (name=%s, locType=%s, bindingType=%s, neg=%s, directAddr=%s) processing failed: [%s]",
                            hostingTestcase.getName(), hostingTestcase.getLocationType().name(), hostingTestcase.getBindingType().name(),
                            hostingTestcase.isNegative(), directAddr.toAddress(), ToolStringUtils.joinDelimit(hostingTestcaseResult.getMessages(), "; ")));

                    Assert.assertTrue(
                        (certValidationStep = discoverCertificates(directAddr)) != null && certValidationStep.isSuccess()
                            && certValidationStep.hasValidCertificateInfo(), String.format("A certificate from %s was not discovered", directAddr.toAddress()));
                } else {
                    Assert.assertTrue((certValidationStep = discoverCertificates(directAddr)) != null && !certValidationStep.isSuccess(),
                        String.format("No valid certificates from %s should have been discovered: ", directAddr.toAddress()));
                }
            }
        }
    }

    @BeforeClass
    public void registerInstanceConfig() {
        if (this.getExistingInstanceConfigBean() != null) {
            this.instanceConfigReg.removeAllBeans();
        }

        InstanceConfig instanceConfig = ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, InstanceConfig.class);
        // noinspection ConstantConditions
        instanceConfig.setDomainName(this.testInstanceConfigDomainName);
        instanceConfig.setIpAddress(this.testInstanceConfigIpAddr);

        this.instanceConfigReg.registerBeans(instanceConfig);
    }

    private List<HostingTestcase> getHostingTestcases() {
        return ToolBeanFactoryUtils.getBeansOfType(this.applicationContext, HostingTestcase.class);
    }

    private InstanceConfig getExistingInstanceConfigBean() {
        // noinspection ConstantConditions
        return ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, InstanceConfigService.class).getBean();
    }

    public CertificateValidationStep discoverCertificates(MailAddress directAddr) {
        return ToolCollectionUtils.findAssignable(CertificateValidationStep.class, this.certDiscoveryService.discoverCertificates(directAddr));
    }
}
