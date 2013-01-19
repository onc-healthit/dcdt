package gov.hhs.onc.dcdt.web.mail.decrypt;

import org.testng.annotations.DataProvider;

public abstract class MailDecryptorDataProvider
{
	private final static ClassLoader CONTEXT_CLASS_LOADER = Thread.currentThread().getContextClassLoader();
	
	private static Object[] data;
	
	@DataProvider(name = "mailDecryptorDataProvider")
	public static Object[][] createData()
	{
		return new Object[][]{ getData() };
	}
	
	private static Object[] getData()
	{
		if (data == null)
		{
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			
			data = new Object[]
			{
				contextClassLoader.getResourceAsStream("testDecryptMail.eml"), 
				contextClassLoader.getResourceAsStream("testDecryptMail_bad.eml"), 
				contextClassLoader.getResourceAsStream("testDecryptMail_key.der"),
				contextClassLoader.getResourceAsStream("testDecryptMail_cert.der")
			};
		}
		
		return data;
	}
}