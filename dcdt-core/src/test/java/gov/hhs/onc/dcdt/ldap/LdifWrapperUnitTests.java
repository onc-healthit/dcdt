package gov.hhs.onc.dcdt.ldap;

import java.util.ArrayList;
import java.util.List;

import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.ldap.all", "dcdt.test.unit.ldap.ldif" })
public class LdifWrapperUnitTests extends ToolTestNgUnitTests {
    private final static String CERT_NAME_VALID_LDAP = "d1_valid_ldap";
    private final static String CERT_NAME_INVALID_LDAP = "d1_invalid_ldap";
    private List<LdifEntry> ldifEntries;
    private byte[] ldifData;

    @Test
    public void testWriteLdifEntries() throws LdapException, LdifException {
        ldifEntries = new ArrayList<>();
        ldifEntries.add(createLdifEntry(CERT_NAME_VALID_LDAP));
        ldifEntries.add(createLdifEntry(CERT_NAME_INVALID_LDAP));

        ldifData = LdifWrapper.writeLdifEntries(ldifEntries);
        Assert.assertNotEquals(ldifData.length, 0, "Output stream is empty.");
    }

    @Test(dependsOnMethods = "testWriteLdifEntries")
    public void testReadLdifEntries() throws LdapException, LdifException {
        List<LdifEntry> ldifEntriesRead = LdifWrapper.readLdifEntries(ldifData);
        Assert.assertEquals(ldifEntriesRead.size(), 2, "LDIF entry size is incorrect.");
        Assert.assertEquals(ldifEntriesRead, ldifEntries, "LDIF entries are not the same.");
        assertLdifEntryAttributesMatch(ldifEntriesRead.get(0), CERT_NAME_VALID_LDAP);
        assertLdifEntryAttributesMatch(ldifEntriesRead.get(1), CERT_NAME_INVALID_LDAP);
    }

    private LdifEntry createLdifEntry(String certName) throws LdapException {
        LdifEntry ldifEntry = new LdifEntry();
        ldifEntry.setDn("cn=" + certName);
        ldifEntry.addAttribute("objectClass", "top", "inetOrgPerson", "person", "organizationalPerson");
        ldifEntry.addAttribute("cn", certName);
        ldifEntry.addAttribute("sn", certName);
        ldifEntry.addAttribute("mail", "d1@direct1.direct-test.com");
        ldifEntry.addAttribute("o", "direct-test.com");
        return ldifEntry;
    }

    private void assertLdifEntryAttributesMatch(LdifEntry ldifEntry, String certName) throws LdapInvalidAttributeValueException {
        Assert.assertEquals(ldifEntry.getDn().toString(), "cn=" + certName, "DN attribute is incorrect.");
        Assert.assertEquals(ldifEntry.get("objectClass").size(), 4, "ObjectClass attribute size is incorrect.");
        Assert.assertEquals(ldifEntry.get("cn").getString(), certName, "CN attribute is incorrect.");
        Assert.assertEquals(ldifEntry.get("sn").getString(), certName, "SN attribute is incorrect.");
        Assert.assertEquals(ldifEntry.get("mail").getString(), "d1@direct1.direct-test.com", "Mail attribute is incorrect.");
        Assert.assertEquals(ldifEntry.get("o").getString(), "direct-test.com", "O attribute is incorrect.");
    }
}
