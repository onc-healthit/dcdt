package gov.hhs.onc.dcdt.web.controller;

import gov.hhs.onc.dcdt.web.test.ControllerTests;
import gov.hhs.onc.dcdt.web.test.impl.AbstractToolControllerIntegrationTests;
import org.testng.annotations.Test;

@ControllerTests(titleMessageCode = ControllerTests.MSG_CODE_TITLE_PREFIX + "admin.login", url = "/admin")
@Test(dependsOnGroups = { "dcdt.test.it.web.controller.home" }, groups = { "dcdt.test.it.web.controller.admin" })
public class AdminControllerIntegrationTests extends AbstractToolControllerIntegrationTests {
}
