package gov.hhs.onc.dcdt.beans.utils;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.utils.all" }, groups = { "dcdt.test.unit.beans.all", "dcdt.test.unit.beans.utils.all",
    "dcdt.test.unit.beans.utils.factory" })
public class ToolBeanFactoryUtilsUnitTests extends AbstractToolUnitTests {
    private static interface ToolTestBeanFactoryUtilsBean extends ToolBean {
        public String getProp1();

        public void setProp1(String prop1);
    }

    @Component(TEST_BEAN_FACTORY_UTILS_BEAN_NAME)
    private static class ToolTestBeanFactoryUtilsBeanImpl extends AbstractToolBean implements ToolTestBeanFactoryUtilsBean {
        @Value("${dcdt.test.beans.bean.1.prop.1.value}")
        private String prop1;

        @Override
        public String getProp1() {
            return this.prop1;
        }

        @Override
        public void setProp1(String prop1) {
            this.prop1 = prop1;
        }
    }

    private final static String TEST_BEAN_FACTORY_UTILS_BEAN_NAME = "testBeanFactoryUtilsImpl";

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ToolTestBeanFactoryUtilsBean testBeanFactoryUtilsBean;

    @Test(dependsOnMethods = { "testGetBeanOfType" })
    public void testGetBeanNameOfType() {
        Assert.assertEquals(ToolBeanFactoryUtils.getBeanNameOfType(this.applicationContext, ToolTestBeanFactoryUtilsBean.class),
            TEST_BEAN_FACTORY_UTILS_BEAN_NAME, "Unable to get bean name by type.");
    }

    @Test
    public void testGetBeanOfType() {
        Assert.assertSame(ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, ToolTestBeanFactoryUtilsBean.class), this.testBeanFactoryUtilsBean,
            "Unable to get bean by type.");
    }
}
