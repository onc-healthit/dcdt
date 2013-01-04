package gov.hhs.onc.dcdt.utils.ldap;

import gov.hhs.onc.dcdt.utils.beans.LdapService;
import gov.hhs.onc.dcdt.utils.ldap.filter.StringEqualityNode;
import gov.hhs.onc.dcdt.utils.test.UtilityTest;
import gov.hhs.onc.dcdt.utils.test.UtilityTestListener;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.directory.shared.ldap.model.constants.SchemaConstants;
import org.apache.directory.shared.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.shared.ldap.model.message.SearchScope;
import org.apache.directory.shared.ldap.model.name.Dn;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({ UtilityTestListener.class })
@Test(groups = { "utils.ldap" })
public class LdapServiceWrapperTest extends UtilityTest
{
	public static LdapServiceWrapper serviceWrapper;
	public static List<Dn> baseDns;
	
	@Test(dependsOnMethods = { "testGetBaseDns" })
	public void testSearch() throws LdapInvalidAttributeValueException, UtilityLdapException
	{
		Assert.assertFalse(CollectionUtils.isEmpty(serviceWrapper.search(baseDns, new StringEqualityNode(SchemaConstants.UID_AT, "admin"), 
			SearchScope.SUBTREE)), "Search failed to find admin object.");
	}
	
	@Test(dependsOnMethods = { "testBind" })
	public void testGetBaseDns() throws UtilityLdapException
	{
		baseDns = serviceWrapper.getBaseDns();
		
		Assert.assertFalse(CollectionUtils.isEmpty(serviceWrapper.search(baseDns, new StringEqualityNode(SchemaConstants.UID_AT, "admin"), 
			SearchScope.SUBTREE)), "Search failed to find any base DN(s).");
	}
	
	@Test(dataProvider = "ldapServiceDataProvider", dataProviderClass = LdapServiceDataProvider.class)
	public void testBind(LdapService service) throws UtilityLdapException
	{
		serviceWrapper = new LdapServiceWrapper(service);
		serviceWrapper.bind();
		
		Assert.assertTrue(serviceWrapper.isBound(), "Failed to bind to LDAP service.");
	}
}