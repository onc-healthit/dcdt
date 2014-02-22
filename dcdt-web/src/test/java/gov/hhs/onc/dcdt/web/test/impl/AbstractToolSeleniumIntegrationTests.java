package gov.hhs.onc.dcdt.web.test.impl;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.net.URL;
import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeClass;

public abstract class AbstractToolSeleniumIntegrationTests extends AbstractToolWebIntegrationTests {
    protected static WebDriver seleniumWebDriver;

    @Value("${dcdt.test.web.selenium.web.driver.url.base}")
    protected URL seleniumWebDriverUrlBase;

    @Value("${dcdt.test.web.selenium.web.driver.url.hub}")
    protected URL seleniumWebDriverUrlHub;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected DesiredCapabilities seleniumWebDriverDesiredCapabilities;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolSeleniumIntegrationTests.class);

    @BeforeClass(groups = { "dcdt.test.it.web.all" }, timeOut = DateUtils.MILLIS_PER_SECOND * 30)
    public void buildSeleniumWebDriver() throws Exception {
        if (seleniumWebDriver == null) {
            seleniumWebDriver = new RemoteWebDriver(this.seleniumWebDriverUrlHub, this.seleniumWebDriverDesiredCapabilities);

            LOGGER.info(String.format("Selenium web driver (class=%s) created.", ToolClassUtils.getName(seleniumWebDriver)));
        }
    }

    @AfterGroups(groups = { "dcdt.test.it.web.all" }, alwaysRun = true, timeOut = DateUtils.MILLIS_PER_SECOND * 30)
    public void closeSeleniumWebDriver() throws Exception {
        if (seleniumWebDriver != null) {
            seleniumWebDriver.close();

            LOGGER.info(String.format("Selenium web driver (class=%s) closed.", ToolClassUtils.getName(seleniumWebDriver)));
        }
    }

    protected void assertTitleEquals(String titleMsgCode) {
        this.assertMessageEquals(seleniumWebDriver.getTitle(), titleMsgCode, String.format("Title (url=%s) does not match.", seleniumWebDriver.getCurrentUrl()));
    }
}
