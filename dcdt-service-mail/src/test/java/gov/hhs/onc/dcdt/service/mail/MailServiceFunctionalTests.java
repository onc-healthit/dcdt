package gov.hhs.onc.dcdt.service.mail;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.crypto.MailEncryptionAlgorithm;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
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
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.Session;
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
    @Resource(name = "mailSessionPlain")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private Session mailSession;

    @Resource(name = "charsetUtf8")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private Charset mailEnc;

    @Value("${dcdt.test.discovery.mail.mapping.results.addr}")
    private MailAddress testResultsAddr;

    private DiscoveryTestcaseSubmissionSenderService discoveryTestcaseSubmissionSenderService;
    private DiscoveryTestcaseResultSenderService discoveryTestcaseResultSenderService;
    private List<DiscoveryTestcase> discoveryTestcases;

    public MailServiceFunctionalTests() {
        super(MailService.class);
    }

    @Test(dependsOnMethods = "testSendDiscoveryTestcaseResults")
    public void testSendDiscoveryTestcaseSubmission() throws Exception {
        MailAddress to;
        DiscoveryTestcaseCredential discoveryTestcaseCred;
        CredentialInfo discoveryTestcaseCredInfo;
        DiscoveryTestcaseSubmission discoveryTestcaseSubmission;

        for (DiscoveryTestcase discoveryTestcase : this.discoveryTestcases) {
            if ((discoveryTestcaseCred = CollectionUtils.find(discoveryTestcase.getTargetCredentials(), DiscoveryTestcaseCredentialValidPredicate.INSTANCE)) == null) {
                continue;
            }

            // noinspection ConstantConditions
            discoveryTestcaseSubmission =
                ToolBeanFactoryUtils.createBeanOfType(this.applicationContext, DiscoveryTestcaseSubmission.class, discoveryTestcase,
                    this.createMessageHelper((to = discoveryTestcase.getMailAddress())));

            // noinspection ConstantConditions
            this.discoveryTestcaseSubmissionSenderService.send(discoveryTestcaseSubmission, to, (discoveryTestcaseCredInfo =
                (discoveryTestcaseCred != null) ? discoveryTestcaseCred.getCredentialInfo() : null), ((discoveryTestcaseCredInfo != null)
                ? discoveryTestcaseCredInfo.getCertificateDescriptor() : null), MailEncryptionAlgorithm.AES256);
        }
    }

    @Test
    public void testSendDiscoveryTestcaseResults() throws Exception {
        this.discoveryTestcases = ToolBeanFactoryUtils.getBeansOfType(this.applicationContext, DiscoveryTestcase.class);

        DiscoveryTestcase testDiscoveryTestcase = this.discoveryTestcases.get(0);
        // noinspection ConstantConditions
        DiscoveryTestcaseCredential testDiscoveryTestcaseCred = testDiscoveryTestcase.getTargetCredentials().iterator().next();

        DiscoveryTestcaseSubmission testDiscoveryTestcaseSubmission =
            ToolBeanFactoryUtils.createBeanOfType(this.applicationContext, DiscoveryTestcaseSubmission.class, testDiscoveryTestcase,
                this.createMessageHelper(this.testResultsAddr));

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

    @BeforeClass(dependsOnMethods = { "startService" }, groups = { "dcdt.test.func.service.mail" })
    public void setupMailMapping() {
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

    @BeforeClass(dependsOnMethods = { "registerInstanceConfig" }, groups = { "dcdt.test.func.service.mail" })
    @Override
    public void startService() {
        super.startService();
    }

    private ToolMimeMessageHelper createMessageHelper(MailAddress to) throws MessagingException {
        String toAddr = to.toAddress();

        ToolMimeMessageHelper msgHelper = new ToolMimeMessageHelper(this.mailSession, this.mailEnc);
        msgHelper.setFrom(this.testResultsAddr);
        msgHelper.setTo(to);
        msgHelper.setSubject(toAddr);
        msgHelper.setText(toAddr);
        msgHelper.setSentDate(new Date());
        msgHelper.getMimeMessage().saveChanges();

        return msgHelper;
    }
}
