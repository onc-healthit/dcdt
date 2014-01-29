package gov.hhs.onc.dcdt.ldap.utils;

import gov.hhs.onc.dcdt.ldap.LdifException;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.unit.ldap.all", "dcdt.test.unit.ldap.utils.all", "dcdt.test.unit.ldap.utils.ldif" })
public class ToolLdifUtilsUnitTests extends AbstractToolUnitTests {
    @Value("${dcdt.test.ldap.ldif.entry.1.attr.cn}")
    private Attribute testLdifEntryAttrCn;

    @Value("${dcdt.test.ldap.ldif.entry.1.attr.mail}")
    private Attribute testLdifEntryAttrMail;

    @Value("${dcdt.test.ldap.ldif.entry.1.attr.obj.class}")
    private Attribute testLdifEntryAttrObjClass;

    @Value("${dcdt.test.ldap.ldif.entry.1}")
    private String testLdifEntryStr;

    private LdifEntry testLdifEntry;

    @Test(dependsOnMethods = { "testReadLdifEntry" })
    public void testWriteLdifEntry() throws LdifException {
        this.testLdifEntryStr = ToolLdifUtils.writeLdifEntries(this.testLdifEntry);
        this.assertLdifEntryStringContainsLdapAttribute(this.testLdifEntryAttrCn);
        this.assertLdifEntryStringContainsLdapAttribute(this.testLdifEntryAttrMail);
        this.assertLdifEntryStringContainsLdapAttribute(this.testLdifEntryAttrObjClass);
    }

    @Test
    public void testReadLdifEntry() throws LdifException {
        this.testLdifEntry = ToolLdifUtils.readLdifEntry(this.testLdifEntryStr);
        this.assertLdifEntryLdapAttributeMatches(this.testLdifEntryAttrCn);
        this.assertLdifEntryLdapAttributeMatches(this.testLdifEntryAttrMail);
        this.assertLdifEntryLdapAttributeMatches(this.testLdifEntryAttrObjClass);
    }

    private void assertLdifEntryStringContainsLdapAttribute(Attribute ldapAttr) {
        String ldapAttrIdStr = ldapAttr.getUpId() + ToolLdapAttributeUtils.DELIM;
        Assert.assertTrue(StringUtils.containsIgnoreCase(this.testLdifEntryStr, ldapAttrIdStr),
            String.format("LDIF entry string does not contain LDAP attribute ID string (%s):\n%s", ldapAttrIdStr, this.testLdifEntryStr));
    }

    private void assertLdifEntryLdapAttributeMatches(Attribute ldapAttr) {
        Assert.assertTrue(this.testLdifEntry.getEntry().contains(ldapAttr),
            String.format("LDIF entry (dn=%s) does not contain LDAP attribute (id=%s).", this.testLdifEntry.getDn(), ldapAttr.getUpId()));
        Assert.assertEquals(IteratorUtils.toList(IteratorUtils.getIterator(this.testLdifEntry.getEntry().get(ldapAttr.getAttributeType()))),
            IteratorUtils.toList(IteratorUtils.getIterator(ldapAttr)),
            String.format("LDIF entry (dn=%s) LDAP attribute (id=%s) values do not match.", this.testLdifEntry.getDn(), ldapAttr.getUpId()));
    }
}
