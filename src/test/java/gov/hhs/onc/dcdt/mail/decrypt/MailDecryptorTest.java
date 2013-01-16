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
	private static InputStream badMailInStream;
	private static byte[] keyData;
	private static byte[] certData;
	
	@Test(dependsOnMethods = { "testDecryptMail" }, expectedExceptions = { MailDecryptionException.class })
	public void testDecryptBadMail() throws MailDecryptionException
	{
		MimeMessage msg = MailDecryptor.decryptMail(badMailInStream, new ByteArrayInputStream(keyData), 
			new ByteArrayInputStream(certData));
		
		Assert.assertNull(msg, "Decrypted bad mail.");
	}
	
	@Test(dataProvider = "mailDecryptorDataProvider", dataProviderClass = MailDecryptorDataProvider.class)
	public void testDecryptMail(InputStream mailInStream, InputStream badMailInStream, InputStream keyInStream, 
		InputStream certInStream)
		throws IOException, MailDecryptionException, MessagingException
	{
		MailDecryptorTest.badMailInStream = badMailInStream;
		keyData = IOUtils.toByteArray(keyInStream);
		certData = IOUtils.toByteArray(certInStream);
		
		MimeMessage msg = MailDecryptor.decryptMail(mailInStream, new ByteArrayInputStream(keyData), 
			new ByteArrayInputStream(certData));
		
		Assert.assertNotNull(msg, "Failed to decrypt mail.");
	}
}