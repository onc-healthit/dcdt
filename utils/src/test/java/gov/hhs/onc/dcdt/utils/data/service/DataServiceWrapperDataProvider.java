package gov.hhs.onc.dcdt.utils.data.service;

import gov.hhs.onc.dcdt.utils.config.UtilityConfig;
import gov.hhs.onc.dcdt.utils.test.MockTestUtility;
import gov.hhs.onc.dcdt.utils.test.UtilityDataProvider;
import java.net.MalformedURLException;
import java.util.Iterator;
import org.testng.annotations.DataProvider;

public abstract class DataServiceWrapperDataProvider extends UtilityDataProvider
{
	private final static String WEB_SERVICE_HOST_ATTRIB_NAME = "wsHost";
	private final static String WEB_SERVICE_PORT_ATTRIB_NAME = "wsPort";
	private final static String WEB_SERVICE_PATH_ATTRIB_NAME = "wsPath";
	
	private static DataServiceWrapper dataService;
	
	@DataProvider(name = "dataServiceWrapperDataProvider")
	public static Iterator<Object> createData() throws MalformedURLException
	{
		return createData(getDataService());
	}
	
	private static DataServiceWrapper getDataService() throws MalformedURLException
	{
		UtilityConfig<?> testUtilConfig = MockTestUtility.getInstance().getConfig();
		
		return (dataService != null) ? dataService : (dataService = new DataServiceWrapper(
			testUtilConfig.getUtilString(WEB_SERVICE_HOST_ATTRIB_NAME), 
			Integer.parseInt(testUtilConfig.getUtilString(WEB_SERVICE_PORT_ATTRIB_NAME)), 
			testUtilConfig.getUtilString(WEB_SERVICE_PATH_ATTRIB_NAME)));
	}
}