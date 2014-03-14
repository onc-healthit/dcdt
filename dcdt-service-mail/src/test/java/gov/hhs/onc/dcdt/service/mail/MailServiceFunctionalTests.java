package gov.hhs.onc.dcdt.service.mail;

import gov.hhs.onc.dcdt.service.test.impl.AbstractToolServiceFunctionalTests;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration({ "spring/spring-service-mail.xml", "spring/spring-service-mail-*.xml" })
@SuppressWarnings({ "SpringContextConfigurationInspection" })
@Test(groups = { "dcdt.test.func.service.mail" })
public class MailServiceFunctionalTests extends AbstractToolServiceFunctionalTests<MailService> {
    public MailServiceFunctionalTests() {
        super(MailService.class);
    }

    @BeforeClass(dependsOnMethods = { "registerInstanceConfig" }, groups = { "dcdt.test.func.service.mail" })
    @Override
    public void startService() {
        super.startService();
    }
}
