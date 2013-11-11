package gov.hhs.onc.dcdt.testcases.discovery;


import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.testcases.discovery" }, groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.testcases.all",
    "dcdt.test.unit.testcases.discovery.dao" })
public class DiscoveryTestcaseDaoUnitTests extends ToolTestNgUnitTests {
    @Autowired
    private List<DiscoveryTestcase> discoveryTestcases;

    @Autowired
    private DiscoveryTestcaseDao discoveryTestcaseDao;

    @Test
    public void testAddBeans() {
        for (DiscoveryTestcase discoveryTestcase : this.discoveryTestcases) {
            this.discoveryTestcaseDao.addBean(discoveryTestcase);
        }
    }

    @Test(dependsOnMethods = "testAddBeans")
    public void testGetBeansById() {
        String discoveryTestcaseName;

        for (DiscoveryTestcase discoveryTestcase : this.discoveryTestcases) {
            discoveryTestcaseName = discoveryTestcase.getName();

            Assert.assertNotNull(this.discoveryTestcaseDao.getBeanById(discoveryTestcaseName),
                String.format("Unable to select Discovery testcase (name=%s) from DAO.", discoveryTestcaseName));
        }
    }

    @Test(dependsOnMethods = "testGetBeansById")
    public void testRemoveBeans() {
        for (DiscoveryTestcase discoveryTestcase : this.discoveryTestcases) {
            this.discoveryTestcaseDao.removeBean(discoveryTestcase);
        }
    }
}
