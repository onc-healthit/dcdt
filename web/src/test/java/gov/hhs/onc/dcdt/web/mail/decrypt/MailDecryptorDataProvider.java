package gov.hhs.onc.dcdt.web.mail.decrypt;

import gov.hhs.onc.dcdt.reflect.resources.ResourceDiscoveryUtils;
import gov.hhs.onc.dcdt.test.data.provider.IterableDataProvider;
import java.util.Iterator;
import org.testng.annotations.DataProvider;

public class MailDecryptorDataProvider extends IterableDataProvider
{
	public MailDecryptorDataProvider()
	{
		super(ResourceDiscoveryUtils.getStream("testDecryptMail.eml"), 
			ResourceDiscoveryUtils.getStream("testDecryptMail_bad.eml"), 
			ResourceDiscoveryUtils.getStream("testDecryptMail_key.der"), 
			ResourceDiscoveryUtils.getStream("testDecryptMail_cert.der"));
	}

	@DataProvider(name = "mailDecryptorDataProvider")
	public static Iterator<Object[]> createData()
	{
		return new MailDecryptorDataProvider().createNextData();
	}
}