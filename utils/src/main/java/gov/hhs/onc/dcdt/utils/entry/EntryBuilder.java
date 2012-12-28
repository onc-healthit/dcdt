package gov.hhs.onc.dcdt.utils.entry;

import gov.hhs.onc.dcdt.utils.beans.Entry;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

public class EntryBuilder
{
	private final static String RSA_ALG_NAME = "RSA";
	private final static String PKCS8_PEM_TYPE = "PRIVATE KEY";
	private final static String PKCS12_KEYSTORE_TYPE = "PKCS12";
	private final static String X509_CERT_TYPE = "X.509";
	private final static String X509_CERT_PEM_TYPE = "CERTIFICATE";
	
	private final static int SERIAL_NUM_LENGTH = 64;
	
	private final static AlgorithmIdentifier SIG_ALGORITHM = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA1WithRSAEncryption");
	private final static AlgorithmIdentifier DIGEST_ALGORITM = new DefaultDigestAlgorithmIdentifierFinder().find(SIG_ALGORITHM);
	
	private final static Logger LOGGER = Logger.getLogger(EntryBuilder.class);
	
	public void generateCa(Entry caEntry) throws EntryException
	{
		generateKeys(caEntry);
		generateCert(caEntry);
		generateKeyStore(caEntry);
		writeFiles(caEntry);
	}
	
	public void generateLeaf(Entry leafEntry) throws EntryException
	{
		generateKeys(leafEntry);
		generateCert(leafEntry);
		generateKeyStore(leafEntry);
		writeFiles(leafEntry);
	}
	
	private static void writeFiles(Entry entry) throws EntryException
	{
		try
		{
			FileUtils.writeByteArrayToFile(entry.getKeyDerFile(), entry.getPrivateKey().getEncoded());
		}
		catch (IOException e)
		{
			throw new EntryException("Unable to write DER-encoded private key to file (path=" + entry.getKeyDerFile() + "): " + 
				entry.getPrivateKey(), e);
		}
		
		try
		{
			writePemFile(entry.getKeyPemFile(), new PemObject(PKCS8_PEM_TYPE, entry.getPrivateKey().getEncoded()));
		}
		catch (IOException e)
		{
			throw new EntryException("Unable to write PEM-encoded private key to file (path=" + entry.getKeyPemFile() + "): " + 
				entry.getPrivateKey(), e);
		}
		
		try
		{
			FileUtils.writeByteArrayToFile(entry.getCertDerFile(), entry.getCert().getEncoded());
		}
		catch (CertificateEncodingException | IOException e)
		{
			throw new EntryException("Unable to write DER-encoded certificate to file (path=" + entry.getCertDerFile() + "): " + 
				entry.getCert(), e);
		}
		
		try
		{
			writePemFile(entry.getCertPemFile(), new PemObject(X509_CERT_PEM_TYPE, entry.getCert().getEncoded()));
		}
		catch (CertificateEncodingException | IOException e)
		{
			throw new EntryException("Unable to write PEM-encoded certificate to file (path=" + entry.getKeyPemFile() + "): " + 
				entry.getPrivateKey(), e);
		}

		try
		{
			entry.getKeyStore().store(new FileOutputStream(entry.getKeyStoreFile()), "".toCharArray());
		}
		catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException e)
		{
			throw new EntryException("Unable to write keystore to file (path=" + entry.getKeyStoreFile() + "): " + 
				entry.getKeyStore(), e);
		}
	}
	
	private static void writePemFile(File pemFile, PemObject pemObj) throws IOException
	{
		PemWriter pemWriter = new PemWriter(new FileWriter(pemFile));
		pemWriter.writeObject(pemObj);
		pemWriter.flush();
	}
	
	private static void generateKeyStore(Entry entry) throws EntryException
	{
		try
		{
			KeyStore keyStore = KeyStore.getInstance(PKCS12_KEYSTORE_TYPE, BouncyCastleProvider.PROVIDER_NAME);
			keyStore.load(null, null);
			keyStore.setKeyEntry(entry.getName(), entry.getPrivateKey(), "".toCharArray(), new Certificate[]{ entry.getCert() });
			
			entry.setKeyStore(keyStore);
		}
		catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException e)
		{
			throw new EntryException("Unable to generate keystore.", e);
		}
	}
	
	private static void generateCert(Entry entry) throws EntryException
	{
		Calendar validEndCalendar = Calendar.getInstance();
		validEndCalendar.add(Calendar.DAY_OF_MONTH, entry.getValidDays());
		
		try
		{
			SubjectPublicKeyInfo pubKeyInfo = entry.getPublicKeyInfo();
			
			X509v3CertificateBuilder certGen = new X509v3CertificateBuilder(entry.getIssuer().getDn().toX500Name(), 
				generateSerialNum(), new Date(), validEndCalendar.getTime(), entry.getDn().toX500Name(), pubKeyInfo);
			
			certGen.addExtension(X509Extension.basicConstraints, false, new BasicConstraints(entry.getCanSign()));
			
			GeneralNames subjAltNames = entry.getDn().getSubjAltNames();
			
			if (subjAltNames != null)
			{
				certGen.addExtension(X509Extension.subjectAlternativeName, false, subjAltNames);
			}
			
			if (entry.isLeaf())
			{
				certGen.addExtension(X509Extension.authorityKeyIdentifier, false, entry.getAuthKeyId());
				certGen.addExtension(X509Extension.subjectKeyIdentifier, false, entry.getSubjKeyId());
				
				GeneralNames issuerAltNames = entry.getIssuer().getDn().getSubjAltNames();
				
				if (issuerAltNames != null)
				{
					certGen.addExtension(X509Extension.issuerAlternativeName, false, issuerAltNames);
				}
			}
			
			X509CertificateHolder certHolder = certGen.build(new BcRSAContentSignerBuilder(SIG_ALGORITHM, DIGEST_ALGORITM)
				.build(entry.getIssuer().getPrivateKeyParam()));
			
			entry.setCert((X509Certificate)CertificateFactory.getInstance(X509_CERT_TYPE, BouncyCastleProvider.PROVIDER_NAME)
				.generateCertificate(new ByteArrayInputStream(certHolder.getEncoded())));
		}
		catch (CertificateException | IOException | NoSuchProviderException | OperatorCreationException e)
		{
			throw new EntryException("Unable to generate certificate.", e);
		}
	}
	
	private static void generateKeys(Entry entry) throws EntryException
	{
		KeyPairGenerator keyPairGen;
		
		try
		{
			keyPairGen = KeyPairGenerator.getInstance(RSA_ALG_NAME, BouncyCastleProvider.PROVIDER_NAME);
		}
		catch (NoSuchAlgorithmException | NoSuchProviderException e)
		{
			throw new EntryException("Unable to create key pair generator: algorithm=" + RSA_ALG_NAME + 
				", providerName=" + BouncyCastleProvider.PROVIDER_NAME, e);
		}
		
		keyPairGen.initialize(entry.getKeyBits(), new SecureRandom());
		
		entry.setKeyPair(keyPairGen.generateKeyPair());
	}
	
	private static BigInteger generateSerialNum()
	{
		byte[] serialNumData = new byte[SERIAL_NUM_LENGTH];
		new SecureRandom().nextBytes(serialNumData);
		
		return new BigInteger(serialNumData);
	}
}