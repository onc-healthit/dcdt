package gov.hhs.onc.dcdt.mail;

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
import java.util.Properties;
import javax.mail.Session;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public abstract class MailCryptographyUtils
{
	private final static String RSA_ALG_NAME = "RSA";
	private final static String X509_CERT_TYPE = "X.509";
	
	private final static Properties MAIL_SESSION_PROPS = new Properties();
	private final static Session MAIL_SESSION = Session.getDefaultInstance(MAIL_SESSION_PROPS);
	
	private final static Logger LOGGER = Logger.getLogger(MailCryptographyUtils.class);
	
	private static KeyFactory rsaKeyFactory;
	private static CertificateFactory x509CertFactory;
	
	static
	{
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public static X509Certificate getCert(InputStream certInStream) throws MailCryptographyException
	{
		try
		{
			return (X509Certificate)getX509CertFactory().generateCertificate(certInStream);
		}
		catch (CertificateException e)
		{
			throw new MailCryptographyException("Unable to get X509 certificate from input stream.", e);
		}
	}
	
	public static PrivateKey getKey(InputStream keyInStream) throws MailCryptographyException
	{
		try
		{
			return getRsaKeyFactory().generatePrivate(new PKCS8EncodedKeySpec(IOUtils.toByteArray(keyInStream)));
		}
		catch (InvalidKeySpecException | IOException e)
		{
			throw new MailCryptographyException("Unable to get private key from input stream.", e);
		}
	}
	
	public synchronized static KeyFactory getRsaKeyFactory() throws MailCryptographyException
	{
		if (rsaKeyFactory == null)
		{
			try
			{
				rsaKeyFactory = KeyFactory.getInstance(RSA_ALG_NAME, BouncyCastleProvider.PROVIDER_NAME);
			}
			catch (NoSuchAlgorithmException | NoSuchProviderException e)
			{
				throw new MailCryptographyException("Unable to get RSA (algorithm=" + RSA_ALG_NAME + 
					") key factory instance from the BouncyCastle (name=" + BouncyCastleProvider.PROVIDER_NAME + 
					") provider.", e);
			}
		}
		
		return rsaKeyFactory;
	}
	
	public synchronized static CertificateFactory getX509CertFactory() throws MailCryptographyException
	{
		if (x509CertFactory == null)
		{
			try
			{
				x509CertFactory = CertificateFactory.getInstance(X509_CERT_TYPE, BouncyCastleProvider.PROVIDER_NAME);
			}
			catch (CertificateException | NoSuchProviderException e)
			{
				throw new MailCryptographyException("Unable to get X509 (type=" + X509_CERT_TYPE + 
					") certificate factory instance from the BouncyCastle (name=" + BouncyCastleProvider.PROVIDER_NAME + 
					") provider.", e);
			}
		}
		
		return x509CertFactory;
	}
	
	public static Session getMailSession()
	{
		return MAIL_SESSION;
	}
	
	public static Properties getMailSessionProps()
	{
		return MAIL_SESSION_PROPS;
	}
}