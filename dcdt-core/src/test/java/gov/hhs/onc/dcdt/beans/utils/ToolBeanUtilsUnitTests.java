package gov.hhs.onc.dcdt.beans.utils;

import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolNamedBean;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import javax.annotation.Nullable;
import javax.persistence.Id;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.utils.all" }, groups = { "dcdt.test.unit.beans.all", "dcdt.test.unit.beans.utils.all",
    "dcdt.test.unit.beans.utils.beans" })
public class ToolBeanUtilsUnitTests extends AbstractToolUnitTests {
    private static interface ToolTestBeanUtilsBean extends ToolNamedBean {
        public String getProp1();

        public void setProp1(String prop1);

        public String getProp2();
    }

    @Component("testBeanUtilsBean")
    private static class ToolTestBeanUtilsBeanImpl extends AbstractToolNamedBean implements ToolTestBeanUtilsBean {
        @Value("${dcdt.test.beans.bean.1.prop.1.value}")
        private String prop1;

        @Value("${dcdt.test.beans.bean.1.prop.2.value}")
        private String prop2;

        @Autowired
        @Override
        @SuppressWarnings({ "SpringJavaAutowiringInspection" })
        public void setName(@Nullable @Value("${dcdt.test.beans.bean.1.name}") String name) {
            super.setName(name);
        }

        @Id
        @Override
        public String getProp1() {
            return this.prop1;
        }

        @Override
        public void setProp1(String prop1) {
            this.prop1 = prop1;
        }

        @Override
        public String getProp2() {
            return this.prop2;
        }
    }

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ToolTestBeanUtilsBean testBeanUtilsBean;

    private BeanWrapper testBeanUtilsBeanWrapper;

    @Test(dependsOnMethods = { "testWrap" })
    public void testGetId() {
        Assert.assertEquals(ToolBeanUtils.getId(this.testBeanUtilsBeanWrapper), this.testBeanUtilsBean.getProp1(), "Unable to get bean ID.");
    }

    @Test(dependsOnMethods = { "testWrap" })
    public void testFindNamed() {
        Assert.assertSame(ToolBeanUtils.findNamed(this.applicationContext, this.testBeanUtilsBean.getName()), testBeanUtilsBean, "Unable to find named bean.");
    }

    @Test
    public void testWrap() {
        BeanWrapper testBeanUtilsBeanWrapper = ToolBeanUtils.wrap(this.testBeanUtilsBean, this.convService);

        Assert.assertNotNull(testBeanUtilsBeanWrapper, "Unable to wrap bean.");
        Assert.assertSame(ToolBeanUtils.wrap(testBeanUtilsBeanWrapper), testBeanUtilsBeanWrapper, "Wrapped bean wrapper is not a reference to itself.");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        this.testBeanUtilsBeanWrapper = ToolBeanUtils.wrap(this.testBeanUtilsBean, this.convService);
    }
}
