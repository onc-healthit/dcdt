package gov.hhs.onc.dcdt.service.ldap;

import gov.hhs.onc.dcdt.service.test.impl.AbstractToolServiceFunctionalTests;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration({ "spring/spring-service-ldap.xml", "spring/spring-service-ldap*.xml" })
@Test(groups = { "dcdt.test.func.service.ldap" })
public class LdapServiceFunctionalTests extends AbstractToolServiceFunctionalTests<LdapService> {
    public LdapServiceFunctionalTests() {
        super(LdapService.class);
    }

    @Override
    protected LdapService createService() {
        return new LdapService(this.applicationContext);
    }
}
