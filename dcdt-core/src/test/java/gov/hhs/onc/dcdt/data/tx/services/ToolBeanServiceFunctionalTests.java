package gov.hhs.onc.dcdt.data.tx.services;

import gov.hhs.onc.dcdt.data.ToolBeanDataAccessException;
import gov.hhs.onc.dcdt.test.ToolTestNgFunctionalTests;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseService;
import gov.hhs.onc.dcdt.testcases.discovery.impl.DiscoveryTestcaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.func.all", "dcdt.test.func.tx.all", "dcdt.test.func.tx.services" })
public class ToolBeanServiceFunctionalTests extends ToolTestNgFunctionalTests {
    private final static String TEST_BEAN_NAME = "testDiscoveryTestcase";
    private final static String TEST_BEAN_MAIL_ADDR = "discovery-testcase@test.direct-test.com";

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private DiscoveryTestcaseService beanService;

    @Test(dependsOnMethods = { "testContainsBean" })
    public void testUpdateBean() throws ToolBeanDataAccessException {
        DiscoveryTestcase bean = this.getBean();
        Assert.assertNull(bean.getMailAddress());

        bean.setMailAddress(TEST_BEAN_MAIL_ADDR);
        this.beanService.updateBean(bean);
        bean = this.getBean();
        Assert.assertNotNull(bean);
        Assert.assertEquals(bean.getMailAddress(), TEST_BEAN_MAIL_ADDR);
    }

    @Test
    public void testContainsBean() throws ToolBeanDataAccessException {
        Assert.assertTrue(this.beanService.containsBean(TEST_BEAN_NAME));

        this.removeBean();
        Assert.assertFalse(this.beanService.containsBean(TEST_BEAN_NAME));
    }

    @BeforeMethod
    public void addBean() throws ToolBeanDataAccessException {
        DiscoveryTestcase bean = new DiscoveryTestcaseImpl();
        bean.setName(TEST_BEAN_NAME);
        this.beanService.addBean(bean);

        Assert.assertNotNull(this.getBean());
    }

    @AfterMethod
    public void removeBean() throws ToolBeanDataAccessException {
        DiscoveryTestcase bean = this.getBean();

        if (bean != null) {
            this.beanService.removeBeanById(TEST_BEAN_NAME);
            bean = this.getBean();
        }

        Assert.assertNull(bean);
    }

    private DiscoveryTestcase getBean() throws ToolBeanDataAccessException {
        return this.beanService.getBeanById(TEST_BEAN_NAME);
    }
}
