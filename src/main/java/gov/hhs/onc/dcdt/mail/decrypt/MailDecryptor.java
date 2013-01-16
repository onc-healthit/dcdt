package gov.hhs.onc.dcdt.mail.decrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.mail.smime.SMIMEUtil;

public abstract class MailDecryptor
{
	private final static Logger LOGGER = Logger.getLogger("emailMessageLogger");
	
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
		try
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA", BouncyCastleProvider.PROVIDER_NAME);
			PrivateKey key = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(IOUtils.toByteArray(keyInStream)));

			CertificateFactory certFactory = CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME);
			X509Certificate cert = (X509Certificate)certFactory.generateCertificate(certInStream);
			
			return decryptMail(getEnvelopedMsg(msgInStream), key, cert);
		}
		catch (CertificateException | InvalidKeySpecException | IOException | NoSuchAlgorithmException | NoSuchProviderException e)
		{
			throw new MailDecryptionException(e);
		}
	}
	
	public static MimeMessage decryptMail(SMIMEEnveloped envelopedMsg, PrivateKey key, X509Certificate cert) throws MailDecryptionException
	{
		RecipientId recipId = new JceKeyTransRecipientId(cert);
		RecipientInformation recip = envelopedMsg.getRecipientInfos().get(recipId);
		
		try
		{
			byte[] decryptedContent = recip.getContent(new JceKeyTransEnvelopedRecipient(key));

			MimeMultipart msgMultiPart = (MimeMultipart)SMIMEUtil.toMimeBodyPart(decryptedContent).getContent();
			Object msgPart;
			
			for (int a = 0; a < msgMultiPart.getCount(); a++)
			{
				msgPart = msgMultiPart.getBodyPart(a).getContent();
				
				if (msgPart instanceof MimeMessage)
				{
					return (MimeMessage)msgPart;
				}
			}
		}
		catch (CMSException | IOException | MessagingException | SMIMEException e)
		{
			throw new MailDecryptionException(e);
		}
		
		return null;
	}
	
	private static SMIMEEnveloped getEnvelopedMsg(InputStream msgStream) throws MailDecryptionException
	{
		try
		{
			MimeMessage mimeMsg = new MimeMessage(Session.getDefaultInstance(System.getProperties(), null), msgStream);
			
			return new SMIMEEnveloped(mimeMsg);
		}
		catch (CMSException | MessagingException e)
		{
			throw new MailDecryptionException(e);
		}
	}
}