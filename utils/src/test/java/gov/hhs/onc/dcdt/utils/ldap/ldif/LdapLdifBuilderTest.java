package gov.hhs.onc.dcdt.utils.ldap.ldif;

import gov.hhs.onc.dcdt.utils.ldap.LdapServiceWrapperTest;
import gov.hhs.onc.dcdt.utils.ldap.UtilityLdapException;
import gov.hhs.onc.dcdt.utils.test.UtilityTest;
import gov.hhs.onc.dcdt.utils.test.UtilityTestListener;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.directory.shared.ldap.model.ldif.LdifEntry;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({ UtilityTestListener.class })
@Test(dependsOnGroups = { "utils.ldap" }, groups = { "utils.ldap.ldif" })
public class LdapLdifBuilderTest extends UtilityTest
{
	private static LdapLdifBuilder ldifBuilder;
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
		ldifBuilder = new LdapLdifBuilder(util, LdapServiceWrapperTest.serviceWrapper);
		
		ldifEntries = ldifBuilder.readEntries("utils/ldaploader/ldif/default.ldif");
		
		Assert.assertFalse(CollectionUtils.isEmpty(ldifEntries), "Failed to read LDIF entries.");
	}
}