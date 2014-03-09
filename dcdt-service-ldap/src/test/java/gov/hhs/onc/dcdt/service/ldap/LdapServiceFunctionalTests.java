package gov.hhs.onc.dcdt.service.ldap;

import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupService;
import gov.hhs.onc.dcdt.service.ldap.config.impl.ToolDirectoryServiceBean;
import gov.hhs.onc.dcdt.service.test.impl.AbstractToolServiceFunctionalTests;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Value;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration({ "spring/spring-service-ldap.xml", "spring/spring-service-ldap-*.xml" })
@SuppressWarnings({ "SpringContextConfigurationInspection" })
@Test(groups = { "dcdt.test.func.service.ldap" })
public class LdapServiceFunctionalTests extends AbstractToolServiceFunctionalTests<LdapService> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private LdapLookupService ldapLookupService;

    public LdapServiceFunctionalTests() {
        super(LdapService.class);
    }

    @Test
    public void testLookupLdapEntries() throws Exception {
        if (!this.service.hasDirectoryServiceBeans()) {
            return;
        }

        LdapConnectionConfig ldapSearchConnConfig;

        // noinspection ConstantConditions
        for (ToolDirectoryServiceBean dirServiceBean : this.service.getDirectoryServiceBeans()) {
            if (!dirServiceBean.hasDataEntries()) {
                continue;
            }

            ldapSearchConnConfig = dirServiceBean.getLdapConfig().toConnectionConfigAnonymous();

            Entry dataResultEntry;
            String dataEntryAttrUpId, dataEntryAttrValueStr;
            byte[] dataEntryAttrValueData;

            // noinspection ConstantConditions
            for (Entry dataEntry : dirServiceBean.getDataEntries()) {
                List<Entry> dataResultEntries = this.ldapLookupService.search(ldapSearchConnConfig, dataEntry.getDn(), SearchScope.OBJECT, null);

                Assert.assertFalse(dataResultEntries.isEmpty(), String.format("No LDAP service data result entries (dn={%s}) found.", dataEntry.getDn()));
                Assert.assertEquals(dataResultEntries.size(), 1, String.format("LDAP service data result entry (dn={%s}) is not unique.", dataEntry.getDn()));
                Assert.assertEquals((dataResultEntry = dataResultEntries.get(0)).size(), dataEntry.size(),
                    String.format("LDAP service data result entry (dn={%s}) size does not match.", dataEntry.getDn()));

                for (Attribute dataEntryAttr : dataEntry) {
                    Assert.assertTrue(dataResultEntry.containsAttribute((dataEntryAttrUpId = dataEntryAttr.getUpId())),
                        String.format("LDAP service data result entry (dn={%s}) does not contain attribute (upId=%s).", dataEntry.getDn(), dataEntryAttrUpId));

                    for (Value<?> dataEntryAttrValue : dataEntryAttr) {
                        if (dataEntryAttrValue.isHumanReadable()) {
                            Assert.assertTrue(
                                dataResultEntry.get(dataEntryAttrUpId).contains((dataEntryAttrValueStr = dataEntryAttrValue.getString())),
                                String.format("LDAP service data result entry (dn={%s}) attribute (upId=%s) does not contain string value: %s",
                                    dataEntry.getDn(), dataEntryAttrUpId, dataEntryAttrValueStr));
                        } else {
                            Assert.assertTrue(
                                dataResultEntry.get(dataEntryAttrUpId).contains((dataEntryAttrValueData = dataEntryAttrValue.getBytes())),
                                String.format("LDAP service data result entry (dn={%s}) attribute (upId=%s) does not contain binary value: %s",
                                    dataEntry.getDn(), dataEntryAttrUpId, Base64.encodeBase64String(dataEntryAttrValueData)));
                        }
                    }
                }
            }
        }
    }

    @BeforeClass(dependsOnMethods = { "registerInstanceConfig" }, groups = { "dcdt.test.func.service.ldap" })
    @Override
    public void startService() {
        super.startService();
    }
}
