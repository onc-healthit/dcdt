package gov.hhs.onc.dcdt.mail.decrypt;

import gov.hhs.onc.dcdt.reflect.resources.ResourceDiscoveryUtils;
import gov.hhs.onc.dcdt.test.data.provider.IterableDataProvider;
import java.util.Iterator;
import org.testng.annotations.DataProvider;

public class MailDecryptorDataProvider extends IterableDataProvider
{
	public MailDecryptorDataProvider()
	{
		super(ResourceDiscoveryUtils.findResource(false, false, "testDecryptMail.eml").getResourceAsStream(), 
			ResourceDiscoveryUtils.findResource(false, false, "testDecryptMail_bad.eml").getResourceAsStream(), 
			ResourceDiscoveryUtils.findResource(false, false, "testDecryptMail_key.der").getResourceAsStream(), 
			ResourceDiscoveryUtils.findResource(false, false, "testDecryptMail_cert.der").getResourceAsStream());
	}

	@DataProvider(name = "mailDecryptorDataProvider")
	public static Iterator<Object[]> createData()
	{
		return new MailDecryptorDataProvider().createNextData();
	}
}