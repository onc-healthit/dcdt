package gov.hhs.onc.dcdt.ldap.lookup;

import gov.hhs.onc.dcdt.ldap.ToolLdapException;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
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

    @Test(dependsOnMethods = { "testGetBaseDns" })
    public void testSearch() throws ToolLdapException, LdapInvalidAttributeValueException {
        List<Entry> searchResults = this.ldapLookupService.search(this.testLdapLookupConnConfig, this.testLdapLookupSearchFilter);
        Assert.assertFalse(searchResults.isEmpty(), String.format("No LDAP search (filter=%s) results found.", this.testLdapLookupSearchFilter));

        for (Entry searchResult : searchResults) {
            assertSearchResultLdapAttributeMatches(searchResult, this.testLdapLookupSearchAttrMail);
            assertSearchResultLdapAttributeMatches(searchResult, this.testLdapLookupSearchAttrUserCert);
        }
    }

    @Test
    public void testGetBaseDns() throws ToolLdapException {
        List<Dn> baseDns = this.ldapLookupService.getBaseDns(this.testLdapLookupConnConfig);
        Assert.assertFalse(baseDns.isEmpty(), "No LDAP base DN(s) found.");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        this.testLdapLookupConnConfig = new LdapConnectionConfig();
        this.testLdapLookupConnConfig.setLdapHost(this.testLdapLookupHost);
        this.testLdapLookupConnConfig.setLdapPort(this.testLdapLookupPort);
    }

    private static void assertSearchResultLdapAttributeMatches(Entry searchResult, Attribute searchAttr) {
        Assert.assertTrue(searchResult.contains(searchAttr),
            String.format("LDAP search result (dn=%s) does not contain LDAP attribute (id=%s).", searchResult.getDn(), searchAttr.getId()));

        if (searchAttr.isHumanReadable()) {
            Assert.assertEquals(searchResult.get(searchAttr.getId()), searchAttr,
                String.format("LDAP search result (dn=%s) attribute does not match.", searchResult.getDn()));
        }
    }
}
