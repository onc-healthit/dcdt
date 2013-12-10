package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import gov.hhs.onc.dcdt.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.utils.msgs" }, groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.utils.all",
    "dcdt.test.unit.utils.validation" })
public class ToolValidationUtilsUnitTests extends ToolTestNgUnitTests {
    private static interface ToolValidationUtilsTestBean extends ToolBean {
        @Pattern(regexp = "^propValue\\d+$")
        public String getProp1();

        public void setProp1(String prop1);

        @Pattern(regexp = "^$", message = "{dcdt.test.unit.utils.validation.constraints.Pattern.msg}")
        public String getProp2();

        @NotBlank(message = "{dcdt.test.unit.utils.validation.constraints.NotBlank.msg}")
        public String getProp3();
    }

    @Component(VALIDATION_TEST_BEAN_NAME)
    private static class ToolValidationUtilsTestBeanImpl extends AbstractToolBean implements ToolValidationUtilsTestBean {
        private String prop1 = VALIDATION_TEST_BEAN_PROP1_VALUE;
        private String prop2 = VALIDATION_TEST_BEAN_PROP2_VALUE;
        private String prop3 = VALIDATION_TEST_BEAN_PROP3_VALUE;

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
        public String getProp3() {
            return this.prop3;
        }
    }

    private final static String VALIDATION_TEST_BEAN_NAME = "toolValidationUtilsUnitTestsBeanImpl";
    private final static String VALIDATION_TEST_BEAN_PROP1_NAME = "prop1";
    private final static String VALIDATION_TEST_BEAN_PROP1_VALUE = "propValue1";
    private final static String VALIDATION_TEST_BEAN_PROP2_NAME = "prop2";
    private final static String VALIDATION_TEST_BEAN_PROP2_VALUE = "propValue2";
    private final static String VALIDATION_TEST_BEAN_PROP2_FIELD_ERROR_MSG_CODE = "dcdt.test.unit.utils.validation.constraints.Pattern.msg";
    private final static String VALIDATION_TEST_BEAN_PROP3_NAME = "prop3";
    private final static String VALIDATION_TEST_BEAN_PROP3_VALUE = null;
    private final static String VALIDATION_TEST_BEAN_PROP3_FIELD_ERROR_MSG_CODE = "dcdt.test.unit.utils.validation.constraints.NotBlank.msg";

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private Validator validator;

    @Autowired
    @Qualifier("validation")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private MessageSource msgSourceValidation;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ToolValidationUtilsTestBean validationUtilsTestBean;

    @Test
    public void testBind() {
        BindingResult bindingResult = ToolValidationUtils.bind(validator, this.validationUtilsTestBean);

        Assert.assertFalse(bindingResult.hasGlobalErrors(),
            String.format("Binding produced global errors:\n[%s]", ToolStringUtils.joinDelimit(bindingResult.getGlobalErrors(), ",")));

        Assert.assertEquals(bindingResult.getFieldErrorCount(), 2, "Binding produced unexpected number of field errors.");

        Assert.assertNotNull(bindingResult.hasFieldErrors(VALIDATION_TEST_BEAN_PROP2_NAME),
            String.format("Binding did not produce field error for property 2 (name=%s).", VALIDATION_TEST_BEAN_PROP2_NAME));
        Assert.assertEquals(ToolMessageUtils.getMessage(this.msgSourceValidation, VALIDATION_TEST_BEAN_PROP2_FIELD_ERROR_MSG_CODE),
            ToolMessageUtils.getMessage(this.msgSourceValidation, bindingResult.getFieldError(VALIDATION_TEST_BEAN_PROP2_NAME)),
            String.format("Binding did not produce expected field error message for property 2 (name=%s).", VALIDATION_TEST_BEAN_PROP2_NAME));

        Assert.assertNotNull(bindingResult.hasFieldErrors(VALIDATION_TEST_BEAN_PROP3_NAME),
            String.format("Binding did not produce field error for property 3 (name=%s).", VALIDATION_TEST_BEAN_PROP3_NAME));
        Assert.assertEquals(ToolMessageUtils.getMessage(this.msgSourceValidation, VALIDATION_TEST_BEAN_PROP3_FIELD_ERROR_MSG_CODE),
            ToolMessageUtils.getMessage(this.msgSourceValidation, bindingResult.getFieldError(VALIDATION_TEST_BEAN_PROP3_NAME)),
            String.format("Binding did not produce expected field error message for property 3 (name=%s).", VALIDATION_TEST_BEAN_PROP3_NAME));
    }
}
