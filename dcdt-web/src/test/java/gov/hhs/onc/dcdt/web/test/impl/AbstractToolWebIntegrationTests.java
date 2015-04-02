package gov.hhs.onc.dcdt.web.test.impl;

import gov.hhs.onc.dcdt.test.impl.AbstractToolIntegrationTests;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration({ "spring/spring-service*.xml", "spring/spring-web.xml", "spring/spring-web-security.xml", "spring/spring-web-test.xml" })
@SuppressWarnings({ "SpringContextConfigurationInspection" })
@Test(groups = { "dcdt.test.it.web.all" })
public abstract class AbstractToolWebIntegrationTests extends AbstractToolIntegrationTests {
}
