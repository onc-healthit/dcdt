package gov.hhs.onc.dcdt.mail.decrypt;

import javax.mail.MessagingException;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

@Test(groups = { "mail.", "mail.decrypt" })
public class MailDecryptorTest
{
	private final static Logger LOGGER = Logger.getLogger(MailDecryptorTest.class);
	
	@Test
	public void testDecryptMail() throws MailDecryptionException, MessagingException
	{
		// TODO: implement DataProvider to get test files
		//ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		
		//MimeMessage msg = MailDecryptor.decryptMail(contextClassLoader.getResourceAsStream("testDecryptMail.eml"),
		//	contextClassLoader.getResourceAsStream("testDecryptMail_key.der"),
		//	contextClassLoader.getResourceAsStream("testDecryptMail_cert.der"));
		
		// TODO: add assertion
	}
}