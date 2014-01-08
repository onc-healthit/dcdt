package gov.hhs.onc.dcdt.web.test;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ToolWebTestNgSeleniumIntegrationTests extends ToolWebTestNgIntegrationTests {
    protected final static long SELENIUM_SETUP_TIMEOUT_MS = 15 * 1000L;

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolWebTestNgSeleniumIntegrationTests.class);

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected ToolSelenium selenium;

    public void beforeWebSeleniumIntegrationTests() throws Exception {
        this.selenium.start();

        LOGGER.info(String.format("Selenium (class=%s) started.", ToolClassUtils.getName(this.selenium)));
    }

    public void afterWebSeleniumIntegrationTests() throws Exception {
        this.selenium.stop();

        LOGGER.info(String.format("Selenium (class=%s) stopped.", ToolClassUtils.getName(this.selenium)));
    }
}
