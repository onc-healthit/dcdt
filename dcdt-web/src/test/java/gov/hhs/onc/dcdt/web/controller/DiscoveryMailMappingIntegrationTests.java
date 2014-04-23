package gov.hhs.onc.dcdt.web.controller;

import gov.hhs.onc.dcdt.web.test.ControllerTests;
import gov.hhs.onc.dcdt.web.test.impl.AbstractToolControllerIntegrationTests;
import org.testng.annotations.Test;

@ControllerTests(titleMessageCode = ControllerTests.MSG_CODE_TITLE_PREFIX + "discovery.mail.mapping", url = "/discovery/mail/mapping")
@Test(dependsOnGroups = { "dcdt.test.it.web.controller.discovery" }, groups = { "dcdt.test.it.web.controller.discovery.mail.mapping" })
public class DiscoveryMailMappingIntegrationTests extends AbstractToolControllerIntegrationTests {
}
