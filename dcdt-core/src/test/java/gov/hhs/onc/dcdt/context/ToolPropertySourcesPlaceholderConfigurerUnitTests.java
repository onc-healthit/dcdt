package gov.hhs.onc.dcdt.context;

import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.unit.context.all", "dcdt.test.unit.context.props" })
public class ToolPropertySourcesPlaceholderConfigurerUnitTests extends AbstractToolUnitTests {
    @Value("${dcdt.test.prop.1}")
    private String testProp1Value;

    @Value("${dcdt.test.prop.1.override}")
    private String testProp1ValueOverride;

    @Value("${dcdt.test.prop.2}")
    private String testProp2Value;

    static {
        System.setProperty("dcdt.test.prop.2", "${dcdt.test.prop.1}${dcdt.test.prop.1.override}");
    }

    @Test
    public void testProcessProperties() {
        Assert
            .assertEquals(this.testProp2Value, (this.testProp1Value + this.testProp1ValueOverride), "Property value not overridden by system property value.");
    }
}
