package gov.hhs.onc.dcdt.testcases.discovery.mail;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.test.impl.AbstractToolFunctionalTests;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
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

    private final static String DIRECT_ADDR_PROP_NAME = "directAddress";

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
        Criterion criterion = Restrictions.eq(DIRECT_ADDR_PROP_NAME, this.testMailMappingDirectAddrRm);

        Assert.assertFalse(this.discoveryTestcaseMailMappingService.containsBean(criterion),
            String.format("A discovery testcase mail mapping with a Direct address=%s should not have been found.", this.testMailMappingDirectAddrRm));
    }

    private void assertMailMappingProperties(MailAddress directAddr, MailAddress resultsAddr) {
        Criterion criterion = Restrictions.eq(DIRECT_ADDR_PROP_NAME, directAddr);
        Assert.assertTrue(this.discoveryTestcaseMailMappingService.containsBean(criterion),
            String.format("Unable to find a discovery testcase mail mapping bean with Direct address=%s.", directAddr));

        List<DiscoveryTestcaseMailMapping> mailMappings = this.discoveryTestcaseMailMappingService.getBeansBy(criterion);
        Assert.assertEquals(mailMappings.size(), 1);
        DiscoveryTestcaseMailMapping mailMapping = ToolListUtils.getFirst(mailMappings);
        // noinspection ConstantConditions
        Assert.assertEquals(mailMapping.getDirectAddress(), directAddr);
        Assert.assertEquals(mailMapping.getResultsAddress(), resultsAddr);
    }
}
