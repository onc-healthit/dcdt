package gov.hhs.onc.dcdt.service.ldap;


import gov.hhs.onc.dcdt.service.test.ToolServiceTestNgFunctionalTests;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration({ "spring/spring-service-ldap*.xml" })
@Test(groups = { "dcdt.test.all", "dcdt.test.func.all", "dcdt.test.func.service.all", "dcdt.test.func.service.ldap" })
public class LdapServiceFunctionalTests extends ToolServiceTestNgFunctionalTests<LdapService> {
    public LdapServiceFunctionalTests() {
        super(LdapService.class);
    }

    @Override
    protected LdapService createService() {
        return new LdapService(this.applicationContext);
    }
}
