package gov.hhs.onc.dcdt.testcases.discovery.results;

import gov.hhs.onc.dcdt.config.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.config.MailGatewayConfig;
import gov.hhs.onc.dcdt.mail.config.MailGatewayCredentialConfig;
import gov.hhs.onc.dcdt.test.impl.AbstractToolFunctionalTests;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.results.impl.DiscoveryTestcaseResultImpl;
import gov.hhs.onc.dcdt.testcases.discovery.results.impl.DiscoveryTestcaseResultInfoImpl;
import gov.hhs.onc.dcdt.testcases.discovery.results.sender.DiscoveryTestcaseResultSenderService;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.func.testcases.discovery.testcases" }, groups = { "dcdt.test.func.testcases.all",
    "dcdt.test.func.testcases.discovery.all", "dcdt.test.func.testcases.discovery.results.all", "dcdt.test.func.testcases.discovery.results.sender.service" })
public class DiscoveryTestcaseResultSenderServiceFunctionalTests extends AbstractToolFunctionalTests {
    private final static Logger LOGGER = LoggerFactory.getLogger(DiscoveryTestcaseResultSenderServiceFunctionalTests.class);

    @Resource(name = "testDiscoveryTestcaseResultSenderService")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private DiscoveryTestcaseResultSenderService testDiscoveryTestcaseResultSenderService;

    @Resource(name = "discoveryTestcase1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private DiscoveryTestcase testDiscoveryTestcase;

    @Test
    public void testSend() throws Exception {
        InstanceMailAddressConfig testFromConfig = this.testDiscoveryTestcaseResultSenderService.getFromConfig();
        MailGatewayConfig testFromGatewayConfig = testFromConfig.getGatewayConfig();
        MailGatewayCredentialConfig testFromGatewayCredConfig = testFromConfig.getGatewayCredentialConfig();

        if (!testFromGatewayConfig.hasHost() || !testFromGatewayCredConfig.hasId() || !testFromGatewayCredConfig.hasSecret()) {
            LOGGER.info(String.format(
                "Unable to test Discovery testcase result sender service - set the following JVM properties to enable:\n" + StringUtils.repeat("  %s\n", 4),
                "dcdt.test.mail.addr.results", "dcdt.test.mail.addr.results.gateway.host", "dcdt.test.mail.addr.results.gateway.cred.id",
                "dcdt.test.mail.addr.results.gateway.cred.secret"));

            return;
        }

        DiscoveryTestcaseResultInfo testDiscoveryTestcaseResultInfo = new DiscoveryTestcaseResultInfoImpl();
        // noinspection ConstantConditions
        testDiscoveryTestcaseResultInfo.setCredentialFound(testDiscoveryTestcase.getTargetCredentials().iterator().next());
        testDiscoveryTestcaseResultInfo.setSuccessful(true);

        DiscoveryTestcaseResult testDiscoveryTestcaseResult = new DiscoveryTestcaseResultImpl();
        testDiscoveryTestcaseResult.setResultInfo(testDiscoveryTestcaseResultInfo);

        this.testDiscoveryTestcaseResultSenderService.send(this.testDiscoveryTestcase, testDiscoveryTestcaseResult, testFromConfig.getMailAddress());
    }
}
