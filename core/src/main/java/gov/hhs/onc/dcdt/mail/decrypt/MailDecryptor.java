package gov.hhs.onc.dcdt.mail.decrypt;

import gov.hhs.onc.dcdt.mail.MailCryptographyException;
import gov.hhs.onc.dcdt.mail.MailCryptographyUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.KeyTransRecipientInformation;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.mail.smime.SMIMEUtil;

public abstract class MailDecryptor
{
	private final static Logger LOGGER = Logger.getLogger(MailDecryptor.class);
	
	public static MimeMessage decryptMail(String msgFilePath, String keyFilePath, String certFilePath) throws MailCryptographyException
	{
		return decryptMail(new File(msgFilePath), new File(keyFilePath), new File(certFilePath));
	}
	
	public static MimeMessage decryptMail(File msgFile, File keyFile, File certFile) throws MailCryptographyException
	{
		FileInputStream msgInStream, keyInStream, certInStream;
		
		try
		{
			msgInStream = new FileInputStream(msgFile);
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
		
		return decryptMail(msgInStream, keyInStream, certInStream);
	}
	
	public static MimeMessage decryptMail(InputStream msgInStream, InputStream keyInStream, InputStream certInStream)
		throws MailCryptographyException
	{
		return decryptMail(getEnvelopedMsg(msgInStream), MailCryptographyUtils.getKey(keyInStream), MailCryptographyUtils.getCert(certInStream));
	}
	
	public static MimeMessage decryptMail(SMIMEEnveloped envelopedMsg, PrivateKey key, X509Certificate cert) throws MailDecryptionException
	{
		Map<BigInteger, KeyTransRecipientInformation> recipients = getRecipients(envelopedMsg);
		KeyTransRecipientId recipientId = new JceKeyTransRecipientId(cert);
		KeyTransRecipientInformation recipient = recipients.get(recipientId.getSerialNumber());
		
		if (recipient == null)
		{
			throw new MailDecryptionException("Enveloped mail does not contain a matching recipient for certificate (subject=" + 
				cert.getSubjectX500Principal() + ", serialNum=" + MailDecryptionStringBuilder.serialNumToString(cert.getSerialNumber()) + 
				"): " + MailDecryptionStringBuilder.envelopedMsgToString(envelopedMsg));
		}
		
		try
		{
			byte[] decryptedContent = recipient.getContent(new JceKeyTransEnvelopedRecipient(key));

			if (ArrayUtils.isEmpty(decryptedContent))
			{
				throw new MailDecryptionException("Unable to decrypt enveloped message content for mail recipient (" + 
					MailDecryptionStringBuilder.recipientsToString(recipient) + ") using private key for subject (dn=" + 
					cert.getSubjectX500Principal() + "): " + MailDecryptionStringBuilder.envelopedMsgToString(envelopedMsg));
			}
			
			MimeMultipart msgMultiPart = (MimeMultipart)SMIMEUtil.toMimeBodyPart(decryptedContent).getContent();
			
			LOGGER.debug("Decrypted enveloped message for mail recipient (" + MailDecryptionStringBuilder.recipientsToString(recipient) + 
				") using private key for subject (dn="+ cert.getSubjectX500Principal() +"): numMsgParts=" + msgMultiPart.getCount());
			
			MimeMessage msg = findMessagePart(msgMultiPart);
			
			if (msg != null)
			{
				LOGGER.debug("Found message for mail recipient (" + MailDecryptionStringBuilder.recipientsToString(recipient) + 
					"): " + MailDecryptionStringBuilder.msgToString(msg));
				
				return msg;
			}
		}
		catch (CMSException | IOException | MessagingException | SMIMEException e)
		{
			throw new MailDecryptionException("Unable to decrypt enveloped message for mail recipient (" + 
				MailDecryptionStringBuilder.recipientsToString(recipient) + ") using private key for subject (dn=" + 
				cert.getSubjectX500Principal() + "): " + MailDecryptionStringBuilder.envelopedMsgToString(envelopedMsg), e);
		}
		
		return null;
	}
	
	public static MimeMessage findMessagePart(Multipart msgMultiPart) throws MailDecryptionException
	{
		Object msgPart;
		int numMsgParts;
		
		try
		{
			numMsgParts = msgMultiPart.getCount();
		}
		catch (MessagingException e)
		{
			throw new MailDecryptionException("Unable to get the number of message parts.", e);
		}
		
		for (int a = 0; a < numMsgParts; a++)
		{
			try
			{
				msgPart = msgMultiPart.getBodyPart(a).getContent();
				
				if (msgPart instanceof MimeMessage)
				{
					return (MimeMessage)msgPart;
				}
			}
			catch (IOException | MessagingException e)
			{
				throw new MailDecryptionException("Unable to get message part (index=" + a + ") body content.", e);
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<BigInteger, KeyTransRecipientInformation> getRecipients(SMIMEEnveloped envelopedMsg)
		throws MailDecryptionException
	{
		Collection<RecipientInformation> recipientsStore = 
			(Collection<RecipientInformation>)envelopedMsg.getRecipientInfos().getRecipients();
		Map<BigInteger, KeyTransRecipientInformation> recipientsMap = new LinkedHashMap<>(recipientsStore.size());
		
		for (RecipientInformation recipient : recipientsStore)
		{
			if (!KeyTransRecipientInformation.class.isAssignableFrom(recipient.getClass()))
			{
				throw new MailDecryptionException("Recipient is not an instance of " + 
					KeyTransRecipientInformation.class.getName() + ": " + recipient.getClass().getName());
			}
			
			recipientsMap.put(((KeyTransRecipientId)recipient.getRID()).getSerialNumber(), 
				(KeyTransRecipientInformation)recipient);
		}
		
		return recipientsMap;
	}
	
	private static SMIMEEnveloped getEnvelopedMsg(InputStream msgInStream) throws MailDecryptionException
	{
		try
		{
			return new SMIMEEnveloped(new MimeMessage(MailCryptographyUtils.getMailSession(), msgInStream));
		}
		catch (CMSException | MessagingException e)
		{
			throw new MailDecryptionException("Unable to get enveloped message from input stream.", e);
		}
	}
}