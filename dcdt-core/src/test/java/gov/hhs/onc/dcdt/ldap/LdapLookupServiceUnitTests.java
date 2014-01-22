package gov.hhs.onc.dcdt.ldap;

import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import java.util.List;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.ldap.utils.all" }, groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.ldap.all",
    "dcdt.test.unit.ldap.lookup" })
public class LdapLookupServiceUnitTests extends ToolTestNgUnitTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private LdapLookupService ldapLookupService;

    @Value("${dcdt.test.ldap.lookup.host}")
    private String testLdapLookupHost;

    @Value("${dcdt.test.ldap.lookup.port}")
    private int testLdapLookupPort;

    @Value("${dcdt.test.ldap.lookup.search.1.filter.mail}")
    private String testLdapLookupSearchFilterMail;

    @Value("${dcdt.test.ldap.lookup.search.1.filter.expr}")
    private ExprNode testLdapLookupSearchFilterExprNode;

    private LdapConnectionConfig testLdapLookupConnConfig;

    @Test(dependsOnMethods = { "testGetBaseDns" })
    public void testSearch() throws LdapException, LdapInvalidAttributeValueException {
        List<Entry> searchResults = this.ldapLookupService.search(this.testLdapLookupConnConfig, this.testLdapLookupSearchFilterExprNode);
        Assert.assertFalse(searchResults.isEmpty(), "No LDAP search results found.");

        String mailAttrName = LdapAttribute.MAIL.getName();
        Dn searchResultDn;

        for (Entry searchResult : searchResults) {
            Assert.assertTrue(searchResult.containsAttribute(mailAttrName),
                String.format("LDAP search result (dn=%s) does not contain mail attribute (name=%s).", (searchResultDn = searchResult.getDn()), mailAttrName));
            Assert.assertEquals(searchResult.get(mailAttrName).getString(), this.testLdapLookupSearchFilterMail,
                String.format("LDAP search result (dn=%s) mail attribute (name=%s) does not match.", searchResultDn, mailAttrName));
        }
    }

    @Test
    public void testGetBaseDns() throws LdapException {
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
}
