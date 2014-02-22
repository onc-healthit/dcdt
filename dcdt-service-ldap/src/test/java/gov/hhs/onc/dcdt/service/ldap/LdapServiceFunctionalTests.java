package gov.hhs.onc.dcdt.service.ldap;

import gov.hhs.onc.dcdt.service.test.impl.AbstractToolServiceFunctionalTests;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration({ "spring/spring-service-ldap.xml", "spring/spring-service-ldap-*.xml" })
@SuppressWarnings({ "SpringContextConfigurationInspection" })
@Test(groups = { "dcdt.test.func.service.ldap" })
public class LdapServiceFunctionalTests extends AbstractToolServiceFunctionalTests<LdapService> {
    public LdapServiceFunctionalTests() {
        super(LdapService.class);
    }

    @BeforeClass(dependsOnMethods = { "registerInstanceConfig" }, groups = { "dcdt.test.func.service.ldap" })
    @Override
    public void startService() {
        super.startService();
    }
}
