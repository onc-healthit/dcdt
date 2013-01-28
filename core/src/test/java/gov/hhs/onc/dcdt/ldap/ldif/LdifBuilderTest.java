package gov.hhs.onc.dcdt.ldap.ldif;

import gov.hhs.onc.dcdt.config.ToolConfig;
import gov.hhs.onc.dcdt.ldap.ToolLdapException;
import gov.hhs.onc.dcdt.test.ToolTestListener;
import gov.hhs.onc.dcdt.test.ldap.AbstractTestNgLdapTest;
import gov.hhs.onc.dcdt.utils.ldap.LdapServiceWrapperTest;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({ ToolTestListener.class })
@Test(dependsOnGroups = { "utils.ldap" }, groups = { "utils.ldap.ldif" })
public class LdifBuilderTest extends AbstractTestNgLdapTest
{
	private static LdifBuilder ldifBuilder;
	private static List<LdifEntry> ldifEntries;
	
	@Test(dependsOnMethods = { "testParseEntries" })
	public void testModify() throws ToolLdapException
	{
		LdapServiceWrapperTest.serviceWrapper.modify(ldifEntries.toArray(new LdifEntry[ldifEntries.size()]));
	}
	
	@Test(dependsOnMethods = { "testReadEntries" })
	public void testParseEntries() throws ToolLdapException
	{
		ldifBuilder.parseEntries(ldifEntries);
	}
	
	@Test
	public void testReadEntries() throws ToolLdapException
	{
		ldifBuilder = new LdifBuilder(new ToolConfig(), LdapServiceWrapperTest.serviceWrapper);
		
		ldifEntries = ldifBuilder.readEntries("utils/ldaploader/ldif/default.ldif");
		
		Assert.assertFalse(CollectionUtils.isEmpty(ldifEntries), "Failed to read LDIF entries.");
	}
}