package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.unit.utils.all", "dcdt.test.unit.utils.msgs" })
public class ToolMessageUtilsUnitTests extends AbstractToolUnitTests {
    private final static String TEST_MSG_CODE = "dcdt.test.unit.utils.msgs.msg.2";
    private final static String TEST_MSG = "msg1value,msg2value";

    @Test
    public void testGetMessage() {
        Assert.assertEquals(ToolMessageUtils.getMessage(this.msgSource, TEST_MSG_CODE), TEST_MSG,
            String.format("Unable to get message (code=%s).", TEST_MSG_CODE));
    }
}
