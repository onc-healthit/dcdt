package gov.hhs.onc.dcdt.web.test;


import gov.hhs.onc.dcdt.test.ToolTestNgIntegrationTests;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration({ "spring/spring-service*.xml", "spring/spring-web.xml", "spring/spring-web-test.xml" })
public abstract class ToolWebTestNgIntegrationTests extends ToolTestNgIntegrationTests {
}
