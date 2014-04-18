package gov.hhs.onc.dcdt.service.mail;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.crypto.MailEncryptionAlgorithm;
import gov.hhs.onc.dcdt.service.test.impl.AbstractToolServiceFunctionalTests;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential.DiscoveryTestcaseCredentialValidPredicate;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMapping;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMappingRegistry;
import gov.hhs.onc.dcdt.testcases.discovery.mail.sender.DiscoveryTestcaseSubmissionSenderService;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.discovery.results.sender.DiscoveryTestcaseResultSenderService;
import gov.hhs.onc.dcdt.utils.ToolDateUtils;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration({ "spring/spring-service-mail.xml", "spring/spring-service-mail-*.xml" })
@SuppressWarnings({ "SpringContextConfigurationInspection" })
@Test(groups = { "dcdt.test.func.service.mail" })
public class MailServiceFunctionalTests extends AbstractToolServiceFunctionalTests<MailService> {
    @Value("${dcdt.test.discovery.mail.mapping.results.addr}")
    private MailAddress testToAddr;

    private DiscoveryTestcaseSubmissionSenderService discoveryTestcaseSubmissionSenderService;
    private DiscoveryTestcaseResultSenderService discoveryTestcaseResultSenderService;
    private List<DiscoveryTestcase> discoveryTestcases;
    private MailAddress testResultsAddr;

    public MailServiceFunctionalTests() {
        super(MailService.class);
    }

    @Test(dependsOnMethods = "testSendDiscoveryTestcaseResults")
    public void testSendDiscoveryTestcaseSubmission() throws Exception {
        for (DiscoveryTestcase discoveryTestcase : this.discoveryTestcases) {
            DiscoveryTestcaseCredential discoveryTestcaseCred =
                CollectionUtils.find(discoveryTestcase.getTargetCredentials(), DiscoveryTestcaseCredentialValidPredicate.INSTANCE);
            DiscoveryTestcaseSubmission discoveryTestcaseSubmission =
                ToolBeanFactoryUtils.createBeanOfType(this.applicationContext, DiscoveryTestcaseSubmission.class, discoveryTestcase);
            // noinspection ConstantConditions
            discoveryTestcaseSubmission.setTestcase(discoveryTestcase);

            // noinspection ConstantConditions
            this.discoveryTestcaseSubmissionSenderService.send(discoveryTestcaseSubmission, discoveryTestcase.getMailAddress(), discoveryTestcaseCred != null
                ? discoveryTestcaseCred.getCredentialInfo() : null, discoveryTestcaseCred != null ? discoveryTestcaseCred.getCredentialInfo()
                .getCertificateDescriptor() : null, MailEncryptionAlgorithm.AES256);
        }

        this.discoveryTestcaseSubmissionSenderService.send(ToolBeanFactoryUtils.createBeanOfType(this.applicationContext, DiscoveryTestcaseSubmission.class),
            this.testToAddr);
    }

    @Test
    public void testSendDiscoveryTestcaseResults() throws Exception {
        this.discoveryTestcases = ToolBeanFactoryUtils.getBeansOfType(this.applicationContext, DiscoveryTestcase.class);
        DiscoveryTestcase testDiscoveryTestcase = this.discoveryTestcases.get(0);
        // noinspection ConstantConditions
        DiscoveryTestcaseCredential testDiscoveryTestcaseCred = testDiscoveryTestcase.getTargetCredentials().iterator().next();
        DiscoveryTestcaseSubmission testDiscoveryTestcaseSubmission =
            ToolBeanFactoryUtils.createBeanOfType(this.applicationContext, DiscoveryTestcaseSubmission.class, testDiscoveryTestcase);

        DiscoveryTestcaseResult testDiscoveryTestcaseResult =
            ToolBeanFactoryUtils.createBeanOfType(this.applicationContext, DiscoveryTestcaseResult.class, testDiscoveryTestcaseSubmission, null);
        // noinspection ConstantConditions
        testDiscoveryTestcaseResult.setExpectedDecryptionCredential(testDiscoveryTestcaseCred);
        testDiscoveryTestcaseResult.setDecryptionCredential(testDiscoveryTestcaseCred);

        this.discoveryTestcaseResultSenderService.send(testDiscoveryTestcaseResult, this.testResultsAddr);
    }

    @AfterGroups(groups = { "dcdt.test.func.service.mail" }, alwaysRun = true, timeOut = ToolDateUtils.MS_IN_SEC * 30)
    @Override
    public void stopService() {
        try {
            Thread.sleep(ToolDateUtils.MS_IN_SEC * 5);
        } catch (InterruptedException ignored) {
        }

        super.stopService();
    }

    @BeforeClass(dependsOnMethods = { "registerInstanceConfig" }, groups = { "dcdt.test.func.service.mail" })
    @Override
    public void startService() {
        super.startService();
        this.setupMailMapping();
    }

    private void setupMailMapping() {
        this.discoveryTestcaseSubmissionSenderService =
            ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, DiscoveryTestcaseSubmissionSenderService.class);
        this.discoveryTestcaseResultSenderService = ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, DiscoveryTestcaseResultSenderService.class);
        // noinspection ConstantConditions
        this.testResultsAddr = this.discoveryTestcaseResultSenderService.getFromConfig().getMailAddress();

        DiscoveryTestcaseMailMapping mailMapping = ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, DiscoveryTestcaseMailMapping.class);
        // noinspection ConstantConditions
        mailMapping.setDirectAddress(this.discoveryTestcaseSubmissionSenderService.getFromConfig().getMailAddress());
        mailMapping.setResultsAddress(this.testResultsAddr);
        // noinspection ConstantConditions
        ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, DiscoveryTestcaseMailMappingRegistry.class).registerBeans(mailMapping);
    }
}
