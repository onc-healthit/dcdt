package gov.hhs.onc.dcdt.utils.ldap;

import gov.hhs.onc.dcdt.ldap.LdapServiceWrapper;
import gov.hhs.onc.dcdt.ldap.ToolLdapException;
import gov.hhs.onc.dcdt.test.ToolTestListener;
import gov.hhs.onc.dcdt.test.ldap.AbstractTestNgLdapTest;
import gov.hhs.onc.dcdt.beans.LdapService;
import gov.hhs.onc.dcdt.ldap.filter.StringEqualityNode;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.server.annotations.CreateLdapServer;
import org.apache.directory.server.annotations.CreateTransport;
import org.apache.directory.server.core.annotations.CreateDS;
import org.testng.Assert;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@CreateDS(name = "dcdt.utils.test.apacheds", allowAnonAccess = true)
@CreateLdapServer(name = "dcdt.utils.test.apacheds.ldap.server", transports = { 
	@CreateTransport(protocol = "LDAP", port = 10389), 
	@CreateTransport(protocol = "LDAP", port = 11389)
})
@Listeners({ ToolTestListener.class })
@Test(groups = { "utils.ldap" })
public class LdapServiceWrapperTest extends AbstractTestNgLdapTest
{
	public static LdapServiceWrapper serviceWrapper;
	public static List<Dn> baseDns;
	
	@BeforeGroups({ "utils.ldap" })
	public void setUpGroup() throws Exception
	{
		this.initClassAnnotations();
		
		this.startLdapServer();
	}
	
	@AfterGroups({ "utils.ldap" })
	public void tearDownGroup() throws Exception
	{
		this.stopLdapServer();
	}
	
	@Test(dependsOnMethods = { "testGetBaseDns" })
	public void testSearch() throws LdapInvalidAttributeValueException, ToolLdapException
	{
		Assert.assertFalse(CollectionUtils.isEmpty(serviceWrapper.search(baseDns, new StringEqualityNode(SchemaConstants.UID_AT, "admin"), 
			SearchScope.SUBTREE)), "Search failed to find admin object.");
	}
	
	@Test(dependsOnMethods = { "testBind" })
	public void testGetBaseDns() throws ToolLdapException
	{
		baseDns = serviceWrapper.getBaseDns();
		
		Assert.assertFalse(CollectionUtils.isEmpty(serviceWrapper.search(baseDns, new StringEqualityNode(SchemaConstants.UID_AT, "admin"), 
			SearchScope.SUBTREE)), "Search failed to find any base DN(s).");
	}
	
	@Test(dataProvider = "ldapServiceDataProvider", dataProviderClass = LdapServiceDataProvider.class)
	public void testBind(LdapService service) throws ToolLdapException
	{
		serviceWrapper = new LdapServiceWrapper(service);
		serviceWrapper.bind();
		
		Assert.assertTrue(serviceWrapper.isBound(), "Failed to bind to LDAP service.");
	}
}