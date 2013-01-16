package gov.hhs.onc.dcdt.utils.data.entry;

import gov.hhs.onc.dcdt.utils.beans.Entry;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.prng.VMPCRandomGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;

public class EntryBuilder
{
	private final static AlgorithmIdentifier SIG_ALGORITHM = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA1WithRSAEncryption");
	private final static AlgorithmIdentifier DIGEST_ALGORITM = new DefaultDigestAlgorithmIdentifierFinder().find(SIG_ALGORITHM);
	
	private final static int SERIAL_NUM_SEED_BYTES = 8;
	private final static Set<BigInteger> USED_SERIAL_NUMS = new HashSet<>();
	
	private final static Logger LOGGER = Logger.getLogger(EntryBuilder.class);
	
	public void generateCa(Entry caEntry) throws EntryException
	{
		generateKeys(caEntry);
		generateCert(caEntry);
		generateKeyStore(caEntry);
	}
	
	public void generateLeaf(Entry leafEntry) throws EntryException
	{
		generateKeys(leafEntry);
		generateCert(leafEntry);
		generateKeyStore(leafEntry);
	}
	
	private static void generateKeyStore(Entry entry) throws EntryException
	{
		try
		{
			KeyStore keyStore = KeyStore.getInstance(Entry.PKCS12_KEYSTORE_TYPE, BouncyCastleProvider.PROVIDER_NAME);
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
			
			if (subjAltNames != null)
			{
				certGen.addExtension(X509Extension.subjectAlternativeName, false, subjAltNames);
			}
			
			X509CertificateHolder certHolder = certGen.build(new BcRSAContentSignerBuilder(SIG_ALGORITHM, DIGEST_ALGORITM)
				.build(entry.getIssuer().getPrivateKeyParam()));
			
			entry.setCert((X509Certificate)CertificateFactory.getInstance(Entry.X509_CERT_TYPE, BouncyCastleProvider.PROVIDER_NAME)
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
			keyPairGen = KeyPairGenerator.getInstance(Entry.RSA_ALG_NAME, BouncyCastleProvider.PROVIDER_NAME);
		}
		catch (NoSuchAlgorithmException | NoSuchProviderException e)
		{
			throw new EntryException("Unable to create key pair generator: algorithm=" + Entry.RSA_ALG_NAME + 
				", providerName=" + BouncyCastleProvider.PROVIDER_NAME, e);
		}
		
		keyPairGen.initialize(entry.getKeyBits(), new SecureRandom());
		
		entry.setKeyPair(keyPairGen.generateKeyPair());
	}
	
	private static BigInteger generateSerialNum()
	{
		BigInteger serialNum;
		SecureRandom serialNumRand = new SecureRandom(generateSerialNumSeed());
		
		do
		{
			serialNum = BigInteger.valueOf(serialNumRand.nextLong()).abs();
		}
		while (USED_SERIAL_NUMS.contains(serialNum));
		
		USED_SERIAL_NUMS.add(serialNum);
		
		return serialNum;
	}
	
	private static byte[] generateSerialNumSeed()
	{
		byte[] serialNumSeed = new byte[SERIAL_NUM_SEED_BYTES];
		VMPCRandomGenerator seedRandGen = new VMPCRandomGenerator();
		seedRandGen.addSeedMaterial(System.currentTimeMillis());
		seedRandGen.nextBytes(serialNumSeed);
		
		return serialNumSeed;
	}
}