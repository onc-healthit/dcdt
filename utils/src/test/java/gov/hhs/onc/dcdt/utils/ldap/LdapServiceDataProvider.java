package gov.hhs.onc.dcdt.utils.ldap;

import gov.hhs.onc.dcdt.utils.beans.BeanAttrib;
import gov.hhs.onc.dcdt.utils.beans.LdapService;
import gov.hhs.onc.dcdt.utils.test.MockTestUtility;
import gov.hhs.onc.dcdt.utils.test.UtilityDataProvider;
import java.util.Iterator;
import org.testng.annotations.DataProvider;

public abstract class LdapServiceDataProvider extends UtilityDataProvider
{
	private final static BeanAttrib LDAP_SERVICE_DEFAULT_NAME_ATTRIB = new BeanAttrib("name", "default");
	
	private static LdapService ldapService;
	
	@DataProvider(name = "ldapServiceDataProvider")
	public static Iterator<Object> createData()
	{
		return createData(getLdapService());
	}
	
	private static LdapService getLdapService()
	{
		return (ldapService != null) ? ldapService : (ldapService = MockTestUtility.getInstance().getConfig().getLdapService(
			LDAP_SERVICE_DEFAULT_NAME_ATTRIB));
	}
}