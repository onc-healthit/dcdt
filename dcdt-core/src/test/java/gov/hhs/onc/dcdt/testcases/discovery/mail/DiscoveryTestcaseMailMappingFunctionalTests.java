package gov.hhs.onc.dcdt.testcases.discovery.mail;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.test.impl.AbstractToolFunctionalTests;
import gov.hhs.onc.dcdt.testcases.discovery.mail.DiscoveryTestcaseMailMapping.DiscoveryTestcaseMailMappingPredicate;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.func.config.all" }, groups = { "dcdt.test.func.testcases.all", "dcdt.test.func.testcases.discovery.all",
    "dcdt.test.func.testcases.discovery.testcases", "dcdt.test.func.testcases.discovery.mail.mapping" })
public class DiscoveryTestcaseMailMappingFunctionalTests extends AbstractToolFunctionalTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private DiscoveryTestcaseMailMappingRegistry discoveryTestcaseMailMappingReg;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private DiscoveryTestcaseMailMappingService discoveryTestcaseMailMappingService;

    @Value("${dcdt.test.discovery.mail.mapping.results.addr}")
    private MailAddress testMailMappingResultsAddr;

    @Value("${dcdt.test.discovery.mail.mapping.direct.addr.rm}")
    private MailAddress testMailMappingDirectAddrRm;

    @Value("${dcdt.test.discovery.mail.mapping.results.addr.rm}")
    private MailAddress testMailMappingResultsAddrRm;

    private DiscoveryTestcaseMailMapping mailMapping;

    @BeforeClass
    public void testSetupMailMapping() {
        this.mailMapping = ToolBeanFactoryUtils.createBeanOfType(this.applicationContext, DiscoveryTestcaseMailMapping.class);
        // noinspection ConstantConditions
        this.mailMapping.setDirectAddress(this.testMailMappingDirectAddrRm);
        this.mailMapping.setResultsAddress(this.testMailMappingResultsAddr);
    }

    @Test
    public void testSetBeans() {
        this.discoveryTestcaseMailMappingService.setBean(this.mailMapping);
        assertMailMappingProperties(this.testMailMappingDirectAddrRm, this.testMailMappingResultsAddr);
    }

    @Test(dependsOnMethods = { "testSetBeans" })
    public void testRegisterBeans() {
        this.mailMapping.setResultsAddress(this.testMailMappingResultsAddrRm);
        this.discoveryTestcaseMailMappingReg.registerBeans(this.mailMapping);
        assertMailMappingProperties(this.testMailMappingDirectAddrRm, this.testMailMappingResultsAddrRm);
    }

    @Test(dependsOnMethods = { "testRegisterBeans" })
    public void testRemoveBeans() {
        this.discoveryTestcaseMailMappingReg.removeBeans(this.mailMapping);
        Assert.assertNull(getMailMapping(this.testMailMappingDirectAddrRm),
            String.format("A discovery testcase mail mapping with a Direct address=%s should not have been found.", this.testMailMappingDirectAddrRm));
    }

    private void assertMailMappingProperties(MailAddress directAddr, MailAddress resultsAddr) {
        DiscoveryTestcaseMailMapping mailMapping = getMailMapping(directAddr);
        Assert.assertEquals(mailMapping.getDirectAddress(), directAddr, "Direct addresses are not equal.");
        Assert.assertEquals(mailMapping.getResultsAddress(), resultsAddr, "Results addresses are not equal.");
    }

    private DiscoveryTestcaseMailMapping getMailMapping(MailAddress mailAddr) {
        return CollectionUtils.find(discoveryTestcaseMailMappingService.getBeans(), new DiscoveryTestcaseMailMappingPredicate(mailAddr));
    }
}
