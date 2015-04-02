package gov.hhs.onc.dcdt.beans.utils;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.collections.ToolTransformer;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.stream.Stream;
import javax.persistence.Id;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.utils.all" }, groups = { "dcdt.test.unit.beans.all", "dcdt.test.unit.beans.utils.all",
    "dcdt.test.unit.beans.utils.prop" })
public class ToolBeanPropertyUtilsUnitTests extends AbstractToolUnitTests {
    private static interface ToolTestBeanPropertyUtilsBean extends ToolBean {
        public String getProp1();

        public void setProp1(String prop1);

        public String getProp2();

        public void setProp3(String prop3);

        public boolean getProp4();

        public void setProp4(boolean prop4);
    }

    @Component("testBeanPropUtilsBean")
    private static class ToolTestBeanPropertyUtilsBeanImpl extends AbstractToolBean implements ToolTestBeanPropertyUtilsBean {
        @Value("${dcdt.test.beans.bean.1.prop.1.value}")
        private String prop1;

        @Value("${dcdt.test.beans.bean.1.prop.2.value}")
        private String prop2;

        @Value("${dcdt.test.beans.bean.1.prop.3.value}")
        private String prop3;

        @Value("${dcdt.test.beans.bean.1.prop.4.value}")
        private boolean prop4;

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

        @Override
        public void setProp3(String prop3) {
            this.prop3 = prop3;
        }

        @Override
        public boolean getProp4() {
            return this.prop4;
        }

        @Override
        public void setProp4(boolean prop4) {
            this.prop4 = prop4;
        }
    }

    @Value("${dcdt.test.beans.bean.1.prop.1.name}")
    private String testBeanPropUtilsBeanProp1Name;

    @Value("${dcdt.test.beans.bean.1.prop.2.name}")
    private String testBeanPropUtilsBeanProp2Name;

    @Value("${dcdt.test.beans.bean.1.prop.3.name}")
    private String testBeanPropUtilsBeanProp3Name;

    @Value("${dcdt.test.beans.bean.1.prop.4.name}")
    private String testBeanPropUtilsBeanProp4Name;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ToolTestBeanPropertyUtilsBean testBeanPropUtilsBean;

    private BeanWrapper testBeanPropUtilsBeanWrapper;

    @Test(dependsOnMethods = { "testDescribeReadable" })
    public void testGetId() {
        Assert.assertEquals(ToolBeanUtils.getId(this.testBeanPropUtilsBeanWrapper), this.testBeanPropUtilsBean.getProp1(), "Unable to get bean ID.");
    }

    @Test(dependsOnMethods = { "testDescribeReadable", "testDescribeWriteable" })
    public void testCopy() {
        ToolTestBeanPropertyUtilsBean testBeanPropUtilsBeanCopy = new ToolTestBeanPropertyUtilsBeanImpl();
        BeanWrapper testBeanPropUtilsBeanWrapperCopy = ToolBeanUtils.wrap(testBeanPropUtilsBeanCopy, this.convService);

        ToolBeanPropertyUtils.copy(this.testBeanPropUtilsBeanWrapper, testBeanPropUtilsBeanWrapperCopy);

        Assert.assertEquals(testBeanPropUtilsBeanCopy.getProp1(), this.testBeanPropUtilsBean.getProp1(), "Unable to copy bean property 1.");
        Assert.assertEquals(testBeanPropUtilsBeanCopy.getProp4(), this.testBeanPropUtilsBean.getProp4(), "Unable to copy bean property 4.");
    }

    @Test
    public void testDescribeReadable() {
        assertTestBeanPropUtilsBeanPropertyDescriptorsMatch(
            this.describeTestBeanPropUtilsBean(this.testBeanPropUtilsBeanProp1Name, this.testBeanPropUtilsBeanProp2Name, this.testBeanPropUtilsBeanProp4Name),
            ToolBeanPropertyUtils.describeReadable(this.testBeanPropUtilsBeanWrapper));

    }

    @Test
    public void testDescribeWriteable() {
        assertTestBeanPropUtilsBeanPropertyDescriptorsMatch(
            this.describeTestBeanPropUtilsBean(this.testBeanPropUtilsBeanProp1Name, this.testBeanPropUtilsBeanProp3Name, testBeanPropUtilsBeanProp4Name),
            ToolBeanPropertyUtils.describeWriteable(this.testBeanPropUtilsBeanWrapper, null));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        this.testBeanPropUtilsBeanWrapper = ToolBeanUtils.wrap(this.testBeanPropUtilsBean, this.convService);
    }

    private static void assertTestBeanPropUtilsBeanPropertyDescriptorsMatch(PropertyDescriptor[] testBeanPropUtilsBeanDescs,
        Collection<PropertyDescriptor> testBeanPropUtilsBeanDescsActual) {
        Assert.assertTrue(testBeanPropUtilsBeanDescsActual.containsAll(ToolArrayUtils.asList(testBeanPropUtilsBeanDescs)), String.format(
            "Bean properties descriptors do not match: expected=[%s], actual=[%s]", ToolStringUtils.joinDelimit(testBeanPropUtilsBeanDescs, ", "),
            ToolStringUtils.joinDelimit(testBeanPropUtilsBeanDescsActual, ", ")));
    }

    private PropertyDescriptor[] describeTestBeanPropUtilsBean(String ... beanPropNames) {
        return Stream.of(beanPropNames).map(ToolTransformer.wrap(this.testBeanPropUtilsBeanWrapper::getPropertyDescriptor)).toArray(
            PropertyDescriptor[]::new);
    }
}
