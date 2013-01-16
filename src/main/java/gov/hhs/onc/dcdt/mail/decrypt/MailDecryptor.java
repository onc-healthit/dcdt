package gov.hhs.onc.dcdt.mail.decrypt;

import java.io.File;
import java.io.FileInputStream;
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
		try
		{
			return decryptMail(new FileInputStream(msgFile), new FileInputStream(keyFile), new FileInputStream(certFile));
		}
		catch (IOException e)
		{
			throw new MailDecryptionException(e);
		}
	}
	
	public static MimeMessage decryptMail(InputStream msgInStream, InputStream keyInStream, InputStream certInStream)
		throws MailDecryptionException
	{
		PrivateKey key = getKey(keyInStream);
		X509Certificate cert = getCert(certInStream);
		
		return decryptMail(getEnvelopedMsg(msgInStream), key, cert);
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
				throw new MailDecryptionException("Unable to decrypt enveloped mail content for recipient (" + 
					MailDecryptionStringBuilder.recipientsToString(recipient) + "): " + 
					MailDecryptionStringBuilder.envelopedMsgToString(envelopedMsg));
			}
			
			MimeMultipart msgMultiPart = (MimeMultipart)SMIMEUtil.toMimeBodyPart(decryptedContent).getContent();
			
			LOGGER.debug("Decrypted enveloped message for recipient (" + MailDecryptionStringBuilder.recipientsToString(recipient) + 
				"): numMsgParts=" + msgMultiPart.getCount());
			
			Object msgPart;
			MimeMessage msg;
			
			for (int a = 0; a < msgMultiPart.getCount(); a++)
			{
				msgPart = msgMultiPart.getBodyPart(a).getContent();
				
				if (msgPart instanceof MimeMessage)
				{
					msg = (MimeMessage)msgPart;
					
					LOGGER.debug("Found message for enveloped mail (" + MailDecryptionStringBuilder.recipientsToString(recipient) + 
						"): " + MailDecryptionStringBuilder.msgToString(msg));
					
					return msg;
				}
			}
		}
		catch (CMSException | IOException | MessagingException | SMIMEException e)
		{
			throw new MailDecryptionException(e);
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
			MimeMessage mimeMsg = new MimeMessage(MAIL_SESSION, msgInStream);
			
			return new SMIMEEnveloped(mimeMsg);
		}
		catch (CMSException | MessagingException e)
		{
			throw new MailDecryptionException(e);
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
			throw new MailDecryptionException(e);
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
			throw new MailDecryptionException(e);
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
				throw new MailDecryptionException(e);
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
				throw new MailDecryptionException(e);
			}
		}
		
		return x509CertFactory;
	}
}