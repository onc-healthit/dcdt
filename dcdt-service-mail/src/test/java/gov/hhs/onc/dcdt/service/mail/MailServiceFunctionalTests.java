package gov.hhs.onc.dcdt.service.mail;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.service.test.impl.AbstractToolServiceFunctionalTests;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.discovery.results.impl.DiscoveryTestcaseResultImpl;
import gov.hhs.onc.dcdt.testcases.discovery.results.sender.DiscoveryTestcaseResultSenderService;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration({ "spring/spring-service-mail.xml", "spring/spring-service-mail-*.xml" })
@SuppressWarnings({ "SpringContextConfigurationInspection" })
@Test(groups = { "dcdt.test.func.service.mail" })
public class MailServiceFunctionalTests extends AbstractToolServiceFunctionalTests<MailService> {
    private final static String TEST_DISCOVERY_TESTCASE_BEAN_NAME = "discoveryTestcase1";

    public MailServiceFunctionalTests() {
        super(MailService.class);
    }

    @Test
    public void testSendDiscoveryTestcaseResults() throws Exception {
        DiscoveryTestcase testDiscoveryTestcase = this.applicationContext.getBean(TEST_DISCOVERY_TESTCASE_BEAN_NAME, DiscoveryTestcase.class);
        // noinspection ConstantConditions
        DiscoveryTestcaseCredential testDiscoveryTestcaseCred = testDiscoveryTestcase.getTargetCredentials().iterator().next();

        DiscoveryTestcaseResult testDiscoveryTestcaseResult = new DiscoveryTestcaseResultImpl();
        testDiscoveryTestcaseResult.setCredentialExpected(testDiscoveryTestcaseCred);
        testDiscoveryTestcaseResult.setCredentialFound(testDiscoveryTestcaseCred);
        testDiscoveryTestcaseResult.setSuccessful(true);
        testDiscoveryTestcaseResult.setTestcase(testDiscoveryTestcase);

        DiscoveryTestcaseResultSenderService discoveryTestcaseResultSenderService =
            ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, DiscoveryTestcaseResultSenderService.class);
        // noinspection ConstantConditions
        discoveryTestcaseResultSenderService.send(testDiscoveryTestcaseResult, discoveryTestcaseResultSenderService.getFromConfig().getMailAddress());
    }

    @BeforeClass(dependsOnMethods = { "registerInstanceConfig" }, groups = { "dcdt.test.func.service.mail" })
    @Override
    public void startService() {
        super.startService();
    }
}
