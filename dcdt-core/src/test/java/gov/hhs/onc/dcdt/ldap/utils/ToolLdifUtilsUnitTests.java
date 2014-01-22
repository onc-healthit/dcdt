package gov.hhs.onc.dcdt.ldap.utils;

import java.util.ArrayList;
import java.util.List;
import gov.hhs.onc.dcdt.ldap.LdifException;
import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.ldap.all", "dcdt.test.unit.ldap.utils.all", "dcdt.test.unit.ldap.utils.ldif" })
public class ToolLdifUtilsUnitTests extends ToolTestNgUnitTests {
    private List<LdifEntry> ldifEntries;
    private byte[] ldifData;

    @Value("${dcdt.test.ldap.ldif.cert.name.valid}")
    private String testCertNameValid;

    @Value("${dcdt.test.ldap.ldif.cert.name.invalid}")
    private String testCertNameInvalid;

    @Value("${dcdt.test.instance.domain.name}")
    private String testDomainName;

    @Value("${dcdt.test.ldap.ldif.mail}")
    private String testMailAttribute;

    @Test
    public void testWriteLdifEntries() throws LdapException, LdifException {
        ldifEntries = new ArrayList<>();
        ldifEntries.add(createLdifEntry(testCertNameValid));
        ldifEntries.add(createLdifEntry(testCertNameInvalid));

        ldifData = ToolLdifUtils.writeLdifEntries(ldifEntries);
        Assert.assertNotEquals(ldifData.length, 0, "Output stream is empty.");
    }

    @Test(dependsOnMethods = "testWriteLdifEntries")
    public void testReadLdifEntries() throws LdapException, LdifException {
        List<LdifEntry> ldifEntriesRead = ToolLdifUtils.readLdifEntries(ldifData);
        Assert.assertEquals(ldifEntriesRead.size(), 2, "LDIF entry size is incorrect.");
        Assert.assertEquals(ldifEntriesRead, ldifEntries, "LDIF entries are not the same.");
        assertLdifEntryAttributesMatch(ldifEntriesRead.get(0), testCertNameValid);
        assertLdifEntryAttributesMatch(ldifEntriesRead.get(1), testCertNameInvalid);
    }

    private LdifEntry createLdifEntry(String certName) throws LdapException {
        LdifEntry ldifEntry = new LdifEntry();
        ldifEntry.setDn("cn=" + certName);
        ldifEntry.addAttribute("objectClass", "top", "inetOrgPerson", "person", "organizationalPerson");
        ldifEntry.addAttribute("cn", certName);
        ldifEntry.addAttribute("sn", certName);
        ldifEntry.addAttribute("mail", this.testMailAttribute);
        ldifEntry.addAttribute("o", this.testDomainName);
        return ldifEntry;
    }

    private void assertLdifEntryAttributesMatch(LdifEntry ldifEntry, String certName) throws LdapInvalidAttributeValueException {
        Assert.assertEquals(ldifEntry.getDn().toString(), "cn=" + certName, "DN attribute is incorrect.");
        Assert.assertEquals(ldifEntry.get("objectClass").size(), 4, "ObjectClass attribute size is incorrect.");
        Assert.assertEquals(ldifEntry.get("cn").getString(), certName, "CN attribute is incorrect.");
        Assert.assertEquals(ldifEntry.get("sn").getString(), certName, "SN attribute is incorrect.");
        Assert.assertEquals(ldifEntry.get("mail").getString(), this.testMailAttribute, "Mail attribute is incorrect.");
        Assert.assertEquals(ldifEntry.get("o").getString(), this.testDomainName, "O attribute is incorrect.");
    }
}
