package gov.hhs.onc.dcdt.tx.services;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.test.ToolTestNgFunctionalTests;
import gov.hhs.onc.dcdt.testcases.discovery.impl.DiscoveryTestcaseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all" })
public class ToolBeanServiceTest extends ToolTestNgFunctionalTests {

    private static final String FOO = "foo";
    private static final String BAR = "bar";

    @Autowired
    private ToolBeanService<ToolBean> toolBeanToolBeanService;

    @Test(expectedExceptions = NullPointerException.class)
    public void testRemoveNullBeansList() throws Exception {
        toolBeanToolBeanService.removeBeans(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void  testRemoveNullBeansInList() throws Exception {
        List<ToolBean> toolBeansList = new ArrayList<ToolBean>();
        toolBeansList.add(null);
        toolBeanToolBeanService.removeBeans(toolBeansList);
    }

    @Test
    public void testAddAndRemoveBean() throws Exception {
        List<ToolBean> resultsList = getBean(FOO);
        Assert.assertEquals(resultsList.size(), 0);
        List<ToolBean> toolBeansList = new ArrayList<>();
        DiscoveryTestcaseImpl discoveryTestcaseImpl = new DiscoveryTestcaseImpl();
        discoveryTestcaseImpl.setName(FOO);
        toolBeansList.add(discoveryTestcaseImpl);
        toolBeanToolBeanService.addBeans(toolBeansList);
        resultsList = getBean(FOO);
        Assert.assertEquals(resultsList.size(), 1);
        removeBean(FOO);
        resultsList = getBean(FOO);
        Assert.assertEquals(resultsList.size(), 0);
    }

    @Test
    public void testAddModifyAndRemoveBean() throws Exception {
        List<ToolBean> resultsList = getBean(FOO);
        Assert.assertEquals(resultsList.size(), 0);
        List<ToolBean> toolBeansList = new ArrayList<>();
        DiscoveryTestcaseImpl discoveryTestcaseImpl = new DiscoveryTestcaseImpl();
        discoveryTestcaseImpl.setName(FOO);
        discoveryTestcaseImpl.setMailAddress(FOO);
        toolBeansList.add(discoveryTestcaseImpl);
        toolBeanToolBeanService.addBeans(toolBeansList);
        resultsList = getBean(FOO);
        Assert.assertEquals(resultsList.size(), 1);
        discoveryTestcaseImpl = (DiscoveryTestcaseImpl)resultsList.get(0);
        Assert.assertEquals(discoveryTestcaseImpl.getMailAddress(), FOO);
        discoveryTestcaseImpl.setMailAddress(BAR);
        toolBeansList = new ArrayList<>();
        toolBeansList.add(discoveryTestcaseImpl);
        toolBeanToolBeanService.updateBeans(toolBeansList);
        resultsList = getBean(FOO);
        Assert.assertEquals(resultsList.size(), 1);
        discoveryTestcaseImpl = (DiscoveryTestcaseImpl)resultsList.get(0);
        Assert.assertEquals(discoveryTestcaseImpl.getMailAddress(), BAR);
        removeBean(FOO);
        resultsList = getBean(FOO);
        Assert.assertEquals(resultsList.size(), 0);
    }

    private  List<ToolBean> getBean(final String id) {
        List<String> idsList = new ArrayList<>();
        idsList.add(id);
        return toolBeanToolBeanService.getBeansById(idsList);
    }

    private void removeBean(final String id) {
        List<String> idsList = new ArrayList<>();
        idsList.add(id);
        List<ToolBean> resultsList = toolBeanToolBeanService.getBeansById(idsList);
        if (null != resultsList && resultsList.size() > 0) {
            toolBeanToolBeanService.removeBeans(resultsList);
        }
    }

}
