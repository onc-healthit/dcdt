package gov.hhs.onc.dcdt.service.mail;

import gov.hhs.onc.dcdt.service.test.ToolServiceTestNgFunctionalTests;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration({ "spring/spring-service-mail.xml", "spring/spring-service-mail*.xml" })
@Test(groups = { "dcdt.test.all", "dcdt.test.func.all", "dcdt.test.func.service.all", "dcdt.test.func.service.mail" })
public class MailServiceFunctionalTests extends ToolServiceTestNgFunctionalTests<MailService> {
    public MailServiceFunctionalTests() {
        super(MailService.class);
    }

    @Override
    protected MailService createService() {
        return new MailService(this.applicationContext);
    }
}
