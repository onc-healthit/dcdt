package gov.hhs.onc.dcdt.web.controller;


import gov.hhs.onc.dcdt.web.test.ToolWebTestNgSeleniumIntegrationTests;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.it.all", "dcdt.test.it.web.all", "dcdt.test.it.web.controller.all", "dcdt.test.it.web.controller.home" })
public class HomeControllerSeleniumIntegrationTests extends ToolWebTestNgSeleniumIntegrationTests {
    @Test
    public void testPageLoad() {
        try {
            this.selenium.start();
            this.selenium.open(StringUtils.EMPTY);
            this.selenium.waitForPageToLoad("30000");

            // TODO: switch to using message source instead of hardcoded string: this.msgSource.getMessage("dcdt.web.home.title", null, null)
            Assert.assertEquals(this.selenium.getTitle(), "Direct Certificate Discovery Tool - Home", "Home page title does not match expected value.");
        } finally {
            this.selenium.stop();
        }
    }
}
