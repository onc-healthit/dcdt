package gov.hhs.onc.dcdt.ldap.ldif;

import gov.hhs.onc.dcdt.config.ToolConfig;
import gov.hhs.onc.dcdt.ldap.LdapServiceWrapperTest;
import gov.hhs.onc.dcdt.ldap.ToolLdapException;
import gov.hhs.onc.dcdt.test.ldap.AbstractTestNgLdapTest;
import java.util.List;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.testng.annotations.Test;

@Test(enabled = false, dependsOnGroups = { "dcdt.ldap" }, groups = { "dcdt.all", "dcdt.ldap.all", "dcdt.ldap.ldif" })
public class LdifBuilderTest extends AbstractTestNgLdapTest
{
	private static LdifBuilder ldifBuilder;
	private static List<LdifEntry> ldifEntries;
	
	@Test(enabled = false, dependsOnMethods = { "testParseEntries" })
	public void testModify() throws ToolLdapException
	{
		LdapServiceWrapperTest.serviceWrapper.modify(ldifEntries.toArray(new LdifEntry[ldifEntries.size()]));
	}
	
	@Test(enabled = false, dependsOnMethods = { "testReadEntries" })
	public void testParseEntries() throws ToolLdapException
	{
		ldifBuilder.parseEntries(ldifEntries);
	}
	
	@Test(enabled = false)
	public void testReadEntries() throws ToolLdapException
	{
		ldifBuilder = new LdifBuilder(new ToolConfig(), LdapServiceWrapperTest.serviceWrapper);
		
		// TODO: add test data to core module
		//ldifEntries = ldifBuilder.readEntries("utils/ldaploader/ldif/default.ldif");
		
		//Assert.assertFalse(CollectionUtils.isEmpty(ldifEntries), "Failed to read LDIF entries.");
	}
}