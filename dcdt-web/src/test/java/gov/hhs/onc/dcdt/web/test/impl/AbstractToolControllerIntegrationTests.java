package gov.hhs.onc.dcdt.web.test.impl;

import gov.hhs.onc.dcdt.net.ToolUrlException;
import gov.hhs.onc.dcdt.net.utils.ToolUrlUtils;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.web.test.ControllerTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.it.web.controller.all" })
public abstract class AbstractToolControllerIntegrationTests extends AbstractToolSeleniumIntegrationTests {
    @Test
    public void testLoad() throws ToolUrlException {
        ControllerTests controllerTestsAnno = ToolAnnotationUtils.findAnnotation(ControllerTests.class, this.getClass());
        Assert.assertNotNull(controllerTestsAnno,
            String.format("Controller tests annotation not found on controller integration tests class (name=%s).", ToolClassUtils.getName(this)));

        seleniumWebDriver.navigate().to(ToolUrlUtils.contextRelative(this.seleniumWebDriverUrlBase, controllerTestsAnno.url()));

        this.assertTitleEquals(controllerTestsAnno.titleMessageCode());
    }
}
