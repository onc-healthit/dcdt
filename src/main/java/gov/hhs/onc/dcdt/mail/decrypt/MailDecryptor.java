package gov.hhs.onc.dcdt.mail.decrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.KeyTransRecipientInformation;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.mail.smime.SMIMEUtil;

public abstract class MailDecryptor
{
	private final static String RSA_ALG_NAME = "RSA";
	private final static String X509_CERT_TYPE = "X.509";
	
	private final static Properties MAIL_SESSION_PROPS = new Properties();
	private final static Session MAIL_SESSION = Session.getDefaultInstance(MAIL_SESSION_PROPS);
	
	private final static Logger LOGGER = Logger.getLogger("emailMessageLogger");
	
	private static KeyFactory rsaKeyFactory;
	private static CertificateFactory x509CertFactory;
	
	static
	{
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public static MimeMessage decryptMail(String msgFilePath, String keyFilePath, String certFilePath) throws MailDecryptionException
	{
		return decryptMail(new File(msgFilePath), new File(keyFilePath), new File(certFilePath));
	}
	
	public static MimeMessage decryptMail(File msgFile, File keyFile, File certFile) throws MailDecryptionException
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
		throws MailDecryptionException
	{
		return decryptMail(getEnvelopedMsg(msgInStream), getKey(keyInStream), getCert(certInStream));
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
			return new SMIMEEnveloped(new MimeMessage(MAIL_SESSION, msgInStream));
		}
		catch (CMSException | MessagingException e)
		{
			throw new MailDecryptionException("Unable to get enveloped message from input stream.", e);
		}
	}
	
	private static X509Certificate getCert(InputStream certInStream) throws MailDecryptionException
	{
		try
		{
			return (X509Certificate)getX509CertFactory().generateCertificate(certInStream);
		}
		catch (CertificateException e)
		{
			throw new MailDecryptionException("Unable to get X509 certificate from input stream.", e);
		}
	}
	
	private static PrivateKey getKey(InputStream keyInStream) throws MailDecryptionException
	{
		try
		{
			return getRsaKeyFactory().generatePrivate(new PKCS8EncodedKeySpec(IOUtils.toByteArray(keyInStream)));
		}
		catch (InvalidKeySpecException | IOException e)
		{
			throw new MailDecryptionException("Unable to get private key from input stream.", e);
		}
	}
	
	private synchronized static KeyFactory getRsaKeyFactory() throws MailDecryptionException
	{
		if (rsaKeyFactory == null)
		{
			try
			{
				rsaKeyFactory = KeyFactory.getInstance(RSA_ALG_NAME, BouncyCastleProvider.PROVIDER_NAME);
			}
			catch (NoSuchAlgorithmException | NoSuchProviderException e)
			{
				throw new MailDecryptionException("Unable to get RSA (algorithm=" + RSA_ALG_NAME + 
					") key factory instance from the BouncyCastle (name=" + BouncyCastleProvider.PROVIDER_NAME + 
					") provider.", e);
			}
		}
		
		return rsaKeyFactory;
	}
	
	private synchronized static CertificateFactory getX509CertFactory() throws MailDecryptionException
	{
		if (x509CertFactory == null)
		{
			try
			{
				x509CertFactory = CertificateFactory.getInstance(X509_CERT_TYPE, BouncyCastleProvider.PROVIDER_NAME);
			}
			catch (CertificateException | NoSuchProviderException e)
			{
				throw new MailDecryptionException("Unable to get X509 (type=" + X509_CERT_TYPE + 
					") certificate factory instance from the BouncyCastle (name=" + BouncyCastleProvider.PROVIDER_NAME + 
					") provider.", e);
			}
		}
		
		return x509CertFactory;
	}
}