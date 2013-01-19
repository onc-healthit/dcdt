package gov.hhs.onc.dcdt.web.mail.encrypt;

import gov.hhs.onc.dcdt.web.mail.MailCryptographyException;
import gov.hhs.onc.dcdt.web.mail.MailCryptographyUtils;
import gov.hhs.onc.dcdt.web.mail.decrypt.MailDecryptionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;
import org.bouncycastle.mail.smime.SMIMEException;

public abstract class MailEncryptor
{
	private final static String TEXT_PLAIN_MIME_TYPE = "text/plain";
	
	public static SMIMEEnveloped encryptMail(Map<RecipientType, List<Address>> recipMap, String subject, Object content, String msgFilePath, 
		String keyFilePath, String certFilePath) throws MailCryptographyException
	{
		return encryptMail(recipMap, subject, content, new File(msgFilePath), new File(keyFilePath), new File(certFilePath));
	}
	
	public static SMIMEEnveloped encryptMail(Map<RecipientType, List<Address>> recipMap, String subject, Object content, File msgFile, 
		File keyFile, File certFile) throws MailCryptographyException
	{
		FileOutputStream msgOutStream;
		FileInputStream keyInStream, certInStream;
		
		try
		{
			msgOutStream = new FileOutputStream(msgFile);
		}
		catch (FileNotFoundException e)
		{
			throw new MailDecryptionException("Mail message file not found: " + msgFile, e);
		}
		
		try
		{
			keyInStream = new FileInputStream(keyFile);
		}
		catch (FileNotFoundException e)
		{
			throw new MailDecryptionException("Mail private key file not found: " + keyFile, e);
		}
		
		try
		{
			certInStream = new FileInputStream(certFile);
		}
		catch (FileNotFoundException e)
		{
			throw new MailDecryptionException("Mail certificate file not found: " + certFile, e);
		}
		
		return encryptMail(recipMap, subject, content, msgOutStream, keyInStream, certInStream);
	}
	
	public static SMIMEEnveloped encryptMail(Map<RecipientType, List<Address>> recipMap, String subject, Object content, 
		OutputStream msgOutStream, InputStream keyInStream, InputStream certInStream) throws MailCryptographyException
	{
		return encryptMail(generateEnvelopedMsg(recipMap, subject, content), MailCryptographyUtils.getKey(keyInStream), 
			MailCryptographyUtils.getCert(certInStream));
	}
	
	public static SMIMEEnveloped encryptMail(SMIMEEnveloped envelopedMsg, PrivateKey key, 
		X509Certificate cert) throws MailDecryptionException
	{
		// TODO: implement
		return null;
	}
	
	private static SMIMEEnveloped generateEnvelopedMsg(Map<RecipientType, List<Address>> recipMap, String subject, Object content)
		throws MailDecryptionException
	{
		try
		{
			MimeMultipart msgMultiPart = new MimeMultipart();
			
			MimeBodyPart msgBodyPart = new MimeBodyPart();
			msgBodyPart.setContent(content, TEXT_PLAIN_MIME_TYPE);
			msgMultiPart.addBodyPart(msgBodyPart);
			
			MimeMessage msg = new MimeMessage(MailCryptographyUtils.getMailSession());
			msg.setContent(msgMultiPart);
			
			SMIMEEnvelopedGenerator envelopedGen = new SMIMEEnvelopedGenerator();
			MimeBodyPart encryptedMsgBodyPart = envelopedGen.generate(msg, new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES128_CBC)
				.setProvider(BouncyCastleProvider.PROVIDER_NAME).build());
			
			MimeMessage encryptedMsg = new MimeMessage(MailCryptographyUtils.getMailSession());
			encryptedMsg.setContent(encryptedMsgBodyPart.getContent(), encryptedMsgBodyPart.getContentType());
			encryptedMsg.setSubject(subject);
			
			List<Address> recipAddrs;
			
			for (RecipientType recipType : recipMap.keySet())
			{
				recipAddrs = recipMap.get(recipType);
				
				encryptedMsg.setRecipients(recipType, recipAddrs.toArray(new Address[recipAddrs.size()]));
			}
			
			return new SMIMEEnveloped(encryptedMsg);
		}
		catch (CMSException | IOException | MessagingException | SMIMEException e)
		{
			throw new MailDecryptionException("Unable to get enveloped message from input stream.", e);
		}
	}
}