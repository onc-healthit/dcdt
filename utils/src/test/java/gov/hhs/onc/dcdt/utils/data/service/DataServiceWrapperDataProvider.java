package gov.hhs.onc.dcdt.utils.data.service;

import gov.hhs.onc.dcdt.test.data.provider.IterableDataProvider;
import gov.hhs.onc.dcdt.utils.config.UtilityConfig;
import gov.hhs.onc.dcdt.utils.test.MockTestUtility;
import java.net.MalformedURLException;
import java.util.Iterator;
import org.testng.annotations.DataProvider;

public class DataServiceWrapperDataProvider extends IterableDataProvider
{
	private final static String WEB_SERVICE_HOST_ATTRIB_NAME = "wsHost";
	private final static String WEB_SERVICE_PORT_ATTRIB_NAME = "wsPort";
	private final static String WEB_SERVICE_PATH_ATTRIB_NAME = "wsPath";
	
	public DataServiceWrapperDataProvider() throws MalformedURLException
	{
		super(getDataService());
	}

	@DataProvider(name = "dataServiceWrapperDataProvider")
	public static Iterator<Object[]> createData() throws MalformedURLException
	{
		return new DataServiceWrapperDataProvider().createNextData();
	}
	
	private static DataServiceWrapper getDataService() throws MalformedURLException
	{
		UtilityConfig<?> testUtilConfig = MockTestUtility.getInstance().getConfig();
		
		return new DataServiceWrapper(testUtilConfig.getUtilString(WEB_SERVICE_HOST_ATTRIB_NAME), 
			Integer.parseInt(testUtilConfig.getUtilString(WEB_SERVICE_PORT_ATTRIB_NAME)), 
			testUtilConfig.getUtilString(WEB_SERVICE_PATH_ATTRIB_NAME));
	}
}