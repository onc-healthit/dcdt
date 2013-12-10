package gov.hhs.onc.dcdt.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.json.ToolJsonException;
import gov.hhs.onc.dcdt.json.ToolObjectMapper;
import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.utils.all", "dcdt.test.unit.utils.json" })
public class ToolJsonUtilsUnitTests extends ToolTestNgUnitTests {
    @JsonSubTypes({ @Type(ToolTestJsonBeanImpl.class) })
    @JsonTypeInfo(use = Id.CLASS)
    private static interface ToolTestJsonBean extends ToolBean {
        public String getProp1();

        public void setProp1(String prop1);
    }

    private static class ToolTestJsonBeanImpl extends AbstractToolBean implements ToolTestJsonBean {
        @JsonProperty(value = TEST_JSON_BEAN_PROP1_NAME, required = true)
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

    private final static String TEST_JSON_BEAN_PROP1_NAME = "prop1";
    private final static String TEST_JSON_BEAN_PROP1_VALUE = "propValue1";
    private final static String TEST_JSON = "{ \"" + Id.CLASS.getDefaultPropertyName() + "\": \"" + ToolClassUtils.getName(ToolTestJsonBeanImpl.class)
        + "\", \"" + TEST_JSON_BEAN_PROP1_NAME + "\": \"" + TEST_JSON_BEAN_PROP1_VALUE + "\" }";

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ToolObjectMapper toolObjMapper;

    private ToolTestJsonBean testJsonBean;

    @Test
    public void testFromJson() throws ToolJsonException {
        this.testJsonBean = ToolJsonUtils.fromJson(this.toolObjMapper.toObjectMapper(), TEST_JSON, ToolTestJsonBean.class);

        Assert.assertNotNull(this.testJsonBean,
            String.format("Unable to create test bean (class=%s) from JSON:\n%s", ToolClassUtils.getName(ToolTestJsonBean.class), TEST_JSON));
        Assert.assertEquals(this.testJsonBean.getProp1(), TEST_JSON_BEAN_PROP1_VALUE, String.format(
            "Unable to deserialize test bean (class=%s) property (name=%s).", ToolClassUtils.getName(ToolTestJsonBean.class), TEST_JSON_BEAN_PROP1_NAME));
    }

    @Test(dependsOnMethods = { "testFromJson" })
    public void testToJson() throws ToolJsonException {
        String testJsonBeanProp1Value = this.testJsonBean.getProp1(), testJson = ToolJsonUtils.toJson(this.toolObjMapper.toObjectMapper(), this.testJsonBean);

        Assert.assertNotNull(testJson, String.format("Unable to create test bean (class=%s) JSON.", ToolClassUtils.getName(this.testJsonBean)));
        Assert.assertTrue(testJson.contains("\"" + testJsonBeanProp1Value + "\""), String.format(
            "Unable to serialize test bean (class=%s) property (name=%s, value=%s).", ToolClassUtils.getName(this.testJsonBean), TEST_JSON_BEAN_PROP1_NAME,
            testJsonBeanProp1Value));
    }
}
