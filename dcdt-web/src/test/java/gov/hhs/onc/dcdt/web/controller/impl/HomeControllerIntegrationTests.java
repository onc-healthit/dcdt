package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.web.test.ControllerTests;
import gov.hhs.onc.dcdt.web.test.impl.AbstractToolControllerIntegrationTests;
import org.testng.annotations.Test;

@ControllerTests(titleMessageCode = ControllerTests.MSG_CODE_TITLE_PREFIX + "home", url = "/")
@Test(groups = { "dcdt.test.it.web.controller.home" })
public class HomeControllerIntegrationTests extends AbstractToolControllerIntegrationTests {
}
