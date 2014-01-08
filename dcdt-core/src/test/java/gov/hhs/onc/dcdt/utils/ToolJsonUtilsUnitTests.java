package gov.hhs.onc.dcdt.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.json.ToolJsonException;
import gov.hhs.onc.dcdt.json.ToolObjectMapper;
import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.utils.all", "dcdt.test.unit.utils.json" })
public class ToolJsonUtilsUnitTests extends ToolTestNgUnitTests {
    private static interface ToolTestJsonBean extends ToolBean {
        public String getProp1();

        public void setProp1(String prop1);
    }

    private abstract static class AbstractToolTestJsonBean extends AbstractToolBean implements ToolTestJsonBean {
        @JsonProperty(value = TEST_JSON_BEAN_PROP1_NAME, required = true)
        protected String prop1;

        @Override
        public String getProp1() {
            return this.prop1;
        }

        @Override
        public void setProp1(String prop1) {
            this.prop1 = prop1;
        }
    }

    @Component(TEST_JSON_BEAN1_NAME)
    @JsonTypeName(TEST_JSON_BEAN1_TYPE_NAME)
    @Scope("singleton")
    private static class ToolTestJsonBeanOne extends AbstractToolTestJsonBean {
    }

    @Component(TEST_JSON_BEAN2_NAME)
    @Scope("singleton")
    private static class ToolTestJsonBeanTwo extends AbstractToolTestJsonBean {
    }

    private final static String TEST_JSON_BEAN_PROP1_NAME = "prop1";
    private final static String TEST_JSON_BEAN_PROP1_JSON = "\"%s\"";
    private final static String TEST_JSON_BEAN_JSON = "{ \"%s\": \"%s\", \"%s\": \"%s\" }";

    private final static String TEST_JSON_BEAN1_NAME = "toolTestJsonBean1";
    private final static String TEST_JSON_BEAN1_TYPE_NAME = "toolTestJsonBeanType1";
    private final static String TEST_JSON_BEAN1_PROP1_VALUE = "prop1Value1";
    private final static String TEST_JSON_BEAN1_JSON = String.format(TEST_JSON_BEAN_JSON, Id.NAME.getDefaultPropertyName(), TEST_JSON_BEAN1_TYPE_NAME,
        TEST_JSON_BEAN_PROP1_NAME, TEST_JSON_BEAN1_PROP1_VALUE);

    private final static String TEST_JSON_BEAN2_NAME = "toolTestJsonBean2";
    private final static String TEST_JSON_BEAN2_PROP1_VALUE = "prop1Value2";
    private final static String TEST_JSON_BEAN2_PROP1_JSON = String.format(TEST_JSON_BEAN_PROP1_JSON, TEST_JSON_BEAN2_PROP1_VALUE);
    private final static String TEST_JSON_BEAN2_JSON = String.format(TEST_JSON_BEAN_JSON, Id.NAME.getDefaultPropertyName(), TEST_JSON_BEAN2_NAME,
        TEST_JSON_BEAN_PROP1_NAME, TEST_JSON_BEAN2_PROP1_VALUE);

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ToolObjectMapper objMapper;

    private ToolTestJsonBean testJsonBean;

    @Test
    public void testFromJson() throws ToolJsonException {
        this.testJsonBean = ToolJsonUtils.fromJson(this.objMapper.toObjectMapper(), TEST_JSON_BEAN1_JSON, ToolTestJsonBean.class);

        Assert.assertNotNull(this.testJsonBean, String.format("Unable to create test bean 1 (class=%s) from JSON by type name:\n%s",
            ToolClassUtils.getName(this.testJsonBean), TEST_JSON_BEAN1_JSON));
        Assert.assertEquals(this.testJsonBean.getProp1(), TEST_JSON_BEAN1_PROP1_VALUE, String.format(
            "Unable to deserialize test bean 1 (class=%s) property (name=%s).", ToolClassUtils.getName(this.testJsonBean), TEST_JSON_BEAN_PROP1_NAME));

        this.testJsonBean = ToolJsonUtils.fromJson(this.objMapper.toObjectMapper(), TEST_JSON_BEAN2_JSON, ToolTestJsonBean.class);

        Assert.assertNotNull(this.testJsonBean, String.format("Unable to create test bean 2 (class=%s) from JSON by bean name:\n%s",
            ToolClassUtils.getName(this.testJsonBean), TEST_JSON_BEAN2_JSON));
        Assert.assertEquals(this.testJsonBean.getProp1(), TEST_JSON_BEAN2_PROP1_VALUE, String.format(
            "Unable to deserialize test bean 2 (class=%s) property (name=%s).", ToolClassUtils.getName(this.testJsonBean), TEST_JSON_BEAN_PROP1_NAME));
    }

    @Test(dependsOnMethods = { "testFromJson" })
    public void testToJson() throws ToolJsonException {
        String testJson = ToolJsonUtils.toJson(this.objMapper.toObjectMapper(), this.testJsonBean);

        Assert.assertNotNull(testJson, String.format("Unable to create test bean (class=%s) JSON.", ToolClassUtils.getName(this.testJsonBean)));
        Assert.assertTrue(testJson.contains(TEST_JSON_BEAN2_PROP1_JSON), String.format(
            "Unable to serialize test bean (class=%s) property (name=%s, value=%s).", ToolClassUtils.getName(this.testJsonBean), TEST_JSON_BEAN_PROP1_NAME,
            this.testJsonBean.getProp1()));
    }
}
