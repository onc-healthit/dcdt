package gov.hhs.onc.dcdt.utils.ldap;

import gov.hhs.onc.dcdt.test.data.provider.IterableDataProvider;
import gov.hhs.onc.dcdt.utils.beans.BeanAttrib;
import gov.hhs.onc.dcdt.utils.test.MockTestUtility;
import java.util.Iterator;
import org.testng.annotations.DataProvider;

public class LdapServiceDataProvider extends IterableDataProvider
{
	public LdapServiceDataProvider()
	{
		super(MockTestUtility.getInstance().getConfig().getLdapService(
			new BeanAttrib("name", "default")));
	}

	@DataProvider(name = "ldapServiceDataProvider")
	public static Iterator<Object[]> createData()
	{
		return new LdapServiceDataProvider().createNextData();
	}
}