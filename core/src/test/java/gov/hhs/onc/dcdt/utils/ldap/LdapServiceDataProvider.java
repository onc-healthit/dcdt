package gov.hhs.onc.dcdt.utils.ldap;

import gov.hhs.onc.dcdt.beans.BeanAttrib;
import gov.hhs.onc.dcdt.config.ToolConfigException;
import gov.hhs.onc.dcdt.test.config.ToolTestConfig;
import gov.hhs.onc.dcdt.test.data.provider.IterableDataProvider;
import java.util.Iterator;
import org.testng.annotations.DataProvider;

public class LdapServiceDataProvider extends IterableDataProvider
{
	public LdapServiceDataProvider() throws ToolConfigException
	{
		super(ToolTestConfig.getInstance().getLdapService(new BeanAttrib("name", "default")));
	}

	@DataProvider(name = "ldapServiceDataProvider")
	public static Iterator<Object[]> createData() throws ToolConfigException
	{
		return new LdapServiceDataProvider().createNextData();
	}
}