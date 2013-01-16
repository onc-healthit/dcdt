package gov.hhs.onc.dcdt.mail.decrypt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "mail.", "mail.decrypt" })
public class MailDecryptorTest
{
	private static InputStream mailInStream;
	private static InputStream badMailInStream;
	private static InputStream keyInStream;
	private static InputStream certInStream;
	
	@Test(dependsOnMethods = { "testDecryptMail" }, expectedExceptions = { MailDecryptionException.class })
	public void testDecryptBadMail() throws MailDecryptionException
	{
		MimeMessage msg = MailDecryptor.decryptMail(MailDecryptorTest.badMailInStream, MailDecryptorTest.keyInStream, 
			MailDecryptorTest.certInStream);
		
		Assert.assertNull(msg, "Decrypted bad mail.");
	}
	
	@Test(dataProvider = "mailDecryptorDataProvider", dataProviderClass = MailDecryptorDataProvider.class)
	public void testDecryptMail(InputStream mailInStream, InputStream badMailInStream, InputStream keyInStream, 
		InputStream certInStream)
		throws IOException, MailDecryptionException, MessagingException
	{
		MailDecryptorTest.mailInStream = mailInStream;
		MailDecryptorTest.badMailInStream = badMailInStream;
		MailDecryptorTest.keyInStream = new ByteArrayInputStream(IOUtils.toByteArray(keyInStream));
		MailDecryptorTest.certInStream = new ByteArrayInputStream(IOUtils.toByteArray(certInStream));
		
		MimeMessage msg = MailDecryptor.decryptMail(MailDecryptorTest.mailInStream, MailDecryptorTest.keyInStream, 
			MailDecryptorTest.certInStream);
		
		Assert.assertNotNull(msg, "Failed to decrypt mail.");
	}
}