package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.stereotype.Component;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.utils.beans.factory" }, groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.utils.all",
    "dcdt.test.unit.utils.beans.all", "dcdt.test.unit.utils.beans.def" })
public class ToolBeanDefinitionUtilsUnitTests extends ToolTestNgUnitTests {
    private static interface ToolBeanDefinitionUtilsTestBean extends ToolBean {
        public String getProp1();

        public void setProp1(String prop1);
    }

    @Component(BEAN_DEF_UTILS_TEST_BEAN_NAME)
    private static class ToolBeanDefinitionUtilsTestBeanImpl extends AbstractToolBean implements ToolBeanDefinitionUtilsTestBean {
        private String prop1 = BEAN_DEF_UTILS_TEST_BEAN_PROP1_VALUE;

        @Override
        public String getProp1() {
            return this.prop1;
        }

        @Override
        public void setProp1(String prop1) {
            this.prop1 = prop1;
        }
    }

    private final static Class<?> BEAN_DEF_UTILS_TEST_BEAN_CLASS = ToolBeanDefinitionUtilsTestBean.class;
    private final static Class<?> BEAN_DEF_UTILS_TEST_BEAN_IMPL_CLASS = ToolBeanDefinitionUtilsTestBeanImpl.class;
    private final static String BEAN_DEF_UTILS_TEST_BEAN_IMPL_CLASS_NAME = ToolClassUtils.getName(BEAN_DEF_UTILS_TEST_BEAN_IMPL_CLASS);
    private final static String BEAN_DEF_UTILS_TEST_BEAN_NAME = "toolBeanDefUtilsTestBeanImpl";
    private final static String BEAN_DEF_UTILS_TEST_BEAN_PROP1_NAME = "prop1";
    private final static String BEAN_DEF_UTILS_TEST_BEAN_PROP1_VALUE = "propValue1";

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ToolBeanDefinitionUtilsTestBean beanDefUtilsTestBean;

    @Test
    public void testGetBeanDefinitionOfType() {
        Assert.assertNotNull(ToolBeanDefinitionUtils.getBeanDefinitionOfType(this.beanFactory, this.beanDefReg, BEAN_DEF_UTILS_TEST_BEAN_CLASS),
            "Unable to get bean definition by type.");
    }

    @Test(dependsOnMethods = { "testGetBeanDefinitionOfType" })
    public void testGetBeanDefinitionClassOfType() {
        Assert.assertTrue(ToolClassUtils.isAssignable(
            ToolBeanDefinitionUtils.getBeanDefinitionClassOfType((ListableBeanFactory) this.beanFactory, this.beanDefReg, BEAN_DEF_UTILS_TEST_BEAN_CLASS),
            BEAN_DEF_UTILS_TEST_BEAN_IMPL_CLASS), "Unable to get bean definition class by type.");
    }

    @Test(dependsOnMethods = { "testGetBeanDefinitionClassOfType" })
    public void testBuildBeanDefinition() {
        BeanDefinition beanDef = ToolBeanDefinitionUtils.buildBeanDefinition(this.beanDefReg, beanDefUtilsTestBean);

        Assert.assertNotNull(beanDef, "Unable to build bean definition.");
        Assert.assertEquals(beanDef.getBeanClassName(), BEAN_DEF_UTILS_TEST_BEAN_IMPL_CLASS_NAME, "Unable to build bean definition class name.");
        Assert.assertEquals(ToolBeanPropertyValuesUtils.getValue(beanDef.getPropertyValues(), BEAN_DEF_UTILS_TEST_BEAN_PROP1_NAME),
            BEAN_DEF_UTILS_TEST_BEAN_PROP1_VALUE, "Unable to build bean definition properties.");
    }
}
