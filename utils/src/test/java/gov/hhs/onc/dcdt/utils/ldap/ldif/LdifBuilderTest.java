package gov.hhs.onc.dcdt.utils.ldap.ldif;

import gov.hhs.onc.dcdt.test.ToolTestListener;
import gov.hhs.onc.dcdt.utils.ldap.LdapServiceWrapperTest;
import gov.hhs.onc.dcdt.utils.ldap.UtilityLdapException;
import gov.hhs.onc.dcdt.utils.test.MockTestUtility;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.directory.server.core.integ.AbstractLdapTestUnit;
import org.apache.directory.server.core.integ.FrameworkRunner;
import org.apache.directory.shared.ldap.model.ldif.LdifEntry;
import org.junit.runner.RunWith;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({ ToolTestListener.class })
@RunWith(FrameworkRunner.class)
@Test(dependsOnGroups = { "utils.ldap" }, groups = { "utils.ldap.ldif" })
public class LdifBuilderTest extends AbstractLdapTestUnit
{
	private static LdifBuilder ldifBuilder;
	private static List<LdifEntry> ldifEntries;
	
	@Test(dependsOnMethods = { "testParseEntries" })
	public void testModify() throws UtilityLdapException
	{
		LdapServiceWrapperTest.serviceWrapper.modify(ldifEntries.toArray(new LdifEntry[ldifEntries.size()]));
	}
	
	@Test(dependsOnMethods = { "testReadEntries" })
	public void testParseEntries() throws UtilityLdapException
	{
		ldifBuilder.parseEntries(ldifEntries);
	}
	
	@Test
	public void testReadEntries() throws UtilityLdapException
	{
		ldifBuilder = new LdifBuilder(MockTestUtility.getInstance(), LdapServiceWrapperTest.serviceWrapper);
		
		ldifEntries = ldifBuilder.readEntries("utils/ldaploader/ldif/default.ldif");
		
		Assert.assertFalse(CollectionUtils.isEmpty(ldifEntries), "Failed to read LDIF entries.");
	}
}