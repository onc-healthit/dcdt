package gov.hhs.onc.dcdt.utils.data.service;

import gov.hhs.onc.dcdt.test.ToolTestListener;
import gov.hhs.onc.dcdt.utils.test.MockTestUtility;
import java.util.List;
import java.util.Map;
import org.nhind.config.Anchor;
import org.nhind.config.Domain;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({ ToolTestListener.class })
@Test(groups = { "utils.data.service" })
public class DataServiceWrapperTest
{
	private static DataServiceWrapper serviceWrapper;
	private static Map<String, Domain> domains;
	private static Map<Domain, List<Anchor>> anchors;
	
	@Test(dependsOnMethods = { "testGetDomains", "testSetAnchors" })
	public void testGetAnchors() throws DataServiceException
	{
		//anchors = serviceWrapper.getAllAnchors(domains.values());
		
		//Assert.assertEquals(anchors.size(), 7, "Failed to get expected number of anchors.");
	}
	
	@Test(dependsOnMethods = { "testConnect", "testGetDomains" })
	public void testSetAnchors() throws DataServiceException
	{
		for (Domain domain : domains.values())
		{
			// TODO: implement
		}
	}
	
	@Test(dependsOnMethods = { "testConnect", "testSetDomains" })
	public void testGetDomains() throws DataServiceException
	{
		domains = serviceWrapper.getDomains();
		
		Assert.assertEquals(domains.size(), 7, "Failed to get expected number of domains.");
	}
	
	@Test(dependsOnMethods = { "testConnect" })
	public void testSetDomains() throws DataServiceException
	{
		String domain = MockTestUtility.getInstance().getConfig().getUtilString("domain");
		
		for (int a = 1; a <= 7; a++)
		{
			serviceWrapper.setDomain("direct" + a + "." + domain);
		}
	}
	
	@Test(dataProvider = "dataServiceWrapperDataProvider", dataProviderClass = DataServiceWrapperDataProvider.class)
	public void testConnect(DataServiceWrapper serviceWrapperInstance) throws DataServiceException
	{
		serviceWrapper = serviceWrapperInstance;
		
		serviceWrapper.connect();
	}
}