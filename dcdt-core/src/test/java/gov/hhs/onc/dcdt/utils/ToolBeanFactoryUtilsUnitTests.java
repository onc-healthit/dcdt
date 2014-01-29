package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.unit.utils.all", "dcdt.test.unit.utils.beans.all", "dcdt.test.unit.utils.beans.factory" })
public class ToolBeanFactoryUtilsUnitTests extends AbstractToolUnitTests {
    private static interface ToolBeanFactoryUtilsTestBean extends ToolBean {
        public String getProp1();

        public void setProp1(String prop1);
    }

    @Component(BEAN_FACTORY_TEST_BEAN_NAME)
    private static class ToolBeanFactoryUtilsTestBeanImpl extends AbstractToolBean implements ToolBeanFactoryUtilsTestBean {
        private String prop1 = BEAN_FACTORY_TEST_BEAN_PROP1_VALUE;

        @Override
        public String getProp1() {
            return this.prop1;
        }

        @Override
        public void setProp1(String prop1) {
            this.prop1 = prop1;
        }
    }

    private final static String BEAN_FACTORY_TEST_BEAN_NAME = "toolBeanFactoryUtilsUnitTestsBeanImpl";
    private final static String BEAN_FACTORY_TEST_BEAN_PROP1_NAME = "prop1";
    private final static String BEAN_FACTORY_TEST_BEAN_PROP1_VALUE = "propValue1";

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ToolBeanFactoryUtilsTestBean beanFactoryUtilsTestBean;

    @Test
    public void testGetBeanOfType() {
        Assert.assertSame(ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, ToolBeanFactoryUtilsTestBean.class), this.beanFactoryUtilsTestBean,
            "Unable to get bean by type.");
    }

    @Test(dependsOnMethods = { "testGetBeanOfType" })
    public void testGetBeanNameOfType() {
        Assert.assertEquals(ToolBeanFactoryUtils.getBeanNameOfType(this.applicationContext, ToolBeanFactoryUtilsTestBean.class), BEAN_FACTORY_TEST_BEAN_NAME,
            "Unable to get bean name by type.");
    }
}
