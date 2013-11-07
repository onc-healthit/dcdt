package gov.hhs.onc.dcdt.web.test;


import org.springframework.beans.factory.annotation.Autowired;

public abstract class ToolWebTestNgSeleniumIntegrationTests extends ToolWebTestNgIntegrationTests {
    @Autowired
    protected ToolSelenium selenium;
}
