package gov.hhs.onc.dcdt.ldap.lookup;

import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.ldap.utils.all" }, groups = { "dcdt.test.unit.ldap.all", "dcdt.test.unit.ldap.lookup.all",
    "dcdt.test.unit.ldap.lookup.service" })
public class LdapLookupServiceUnitTests extends AbstractToolUnitTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private LdapLookupService ldapLookupService;

    @Value("${dcdt.test.ldap.lookup.host}")
    private String testLdapLookupHost;

    @Value("${dcdt.test.ldap.lookup.port}")
    private int testLdapLookupPort;

    @Value("${dcdt.test.ldap.lookup.search.1.attr.mail}")
    private Attribute testLdapLookupSearchAttrMail;

    @Value("${dcdt.test.ldap.lookup.search.1.attr.user.cert}")
    private Attribute testLdapLookupSearchAttrUserCert;

    @Value("${dcdt.test.ldap.lookup.search.1.filter.expr}")
    private ExprNode testLdapLookupSearchFilter;

    private LdapConnectionConfig testLdapLookupConnConfig;
    private List<Dn> testLdapLookupBaseDns;

    @Test(dependsOnMethods = { "testLookupBaseDns" })
    public void testLookupEntries() throws LdapInvalidAttributeValueException {
        List<Entry> entries = new ArrayList<>();

        for (Dn testLdapLookupBaseDn : this.testLdapLookupBaseDns) {
            ToolCollectionUtils.addAll(entries,
                this.ldapLookupService.lookupEntries(this.testLdapLookupConnConfig, testLdapLookupBaseDn, this.testLdapLookupSearchFilter).getItems());
        }

        Assert.assertFalse(entries.isEmpty(), String.format("No LDAP search (filter=%s) results found.", this.testLdapLookupSearchFilter));

        for (Entry entry : entries) {
            assertEntryLdapAttributeMatches(entry, this.testLdapLookupSearchAttrMail);
            assertEntryLdapAttributeMatches(entry, this.testLdapLookupSearchAttrUserCert);
        }
    }

    @Test
    public void testLookupBaseDns() {
        LdapBaseDnLookupResult baseDnLookupResult = this.ldapLookupService.lookupBaseDns(this.testLdapLookupConnConfig);
        Assert.assertTrue(baseDnLookupResult.isSuccess(), String.format("LDAP base DN lookup was not successful: %s", baseDnLookupResult.getMessage()));
        Assert.assertTrue(baseDnLookupResult.hasItems(), "No LDAP base DN(s) found.");

        this.testLdapLookupBaseDns = baseDnLookupResult.getItems();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        this.testLdapLookupConnConfig = new LdapConnectionConfig();
        this.testLdapLookupConnConfig.setLdapHost(this.testLdapLookupHost);
        this.testLdapLookupConnConfig.setLdapPort(this.testLdapLookupPort);
    }

    private static void assertEntryLdapAttributeMatches(Entry entry, Attribute entryAttr) {
        Assert.assertTrue(entry.contains(entryAttr),
            String.format("LDAP search result (dn=%s) does not contain LDAP attribute (id=%s).", entry.getDn(), entryAttr.getId()));

        if (entryAttr.isHumanReadable()) {
            Assert.assertEquals(entry.get(entryAttr.getId()), entryAttr, String.format("LDAP search result (dn=%s) attribute does not match.", entry.getDn()));
        }
    }
}
