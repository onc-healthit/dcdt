package gov.hhs.onc.dcdt.web.controller;

import gov.hhs.onc.dcdt.utils.ToolMessageUtils;
import gov.hhs.onc.dcdt.web.test.ToolWebTestNgSeleniumIntegrationTests;
import org.testng.Assert;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.it.all", "dcdt.test.it.web.all", "dcdt.test.it.web.controller.all", "dcdt.test.it.web.controller.home" })
public class HomeControllerSeleniumIntegrationTests extends ToolWebTestNgSeleniumIntegrationTests {
    private final static String TITLE_HOME_MSG_CODE = "dcdt.web.title.home";
    
    @Test
    public void testTitle() {
        this.selenium.open();

        Assert.assertEquals(this.selenium.getTitle(), ToolMessageUtils.getMessage(this.msgSource, TITLE_HOME_MSG_CODE),
            "Home page title does not match expected value.");
    }

    @BeforeGroups(groups = { "dcdt.test.it.web.all" }, timeOut = SELENIUM_SETUP_TIMEOUT_MS)
    @Override
    public void beforeWebSeleniumIntegrationTests() throws Exception {
        super.beforeWebSeleniumIntegrationTests();
    }

    @AfterGroups(groups = { "dcdt.test.it.web.all" }, timeOut = SELENIUM_SETUP_TIMEOUT_MS)
    @Override
    public void afterWebSeleniumIntegrationTests() throws Exception {
        super.afterWebSeleniumIntegrationTests();
    }
}
