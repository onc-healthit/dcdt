package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.crypto.constants.CertificateType;
import gov.hhs.onc.dcdt.crypto.constants.DataEncoding;
import gov.hhs.onc.dcdt.crypto.constants.PemType;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class CertificateUtils
{
	private final static int SERIAL_NUM_SEED_SIZE = 8;

	//<editor-fold desc="Generation methods">
	public static X509Certificate generateCertificate(X509Certificate issuer, BigInteger serialNum, CertificateValidInterval validInterval, 
		CertificateName subject, PublicKey publicKey) throws CryptographyException
	{
		// TODO: implement
		return null;
	}
	
	/**
	 * Generates a random certificate serial number.
	 * 
	 * @return the serial number
	 * @throws CryptographyException thrown when the serial number cannot be generated
	 */
	public static BigInteger generateSerialNum() throws CryptographyException
	{
		return generateSerialNum(null);
	}
	
	/**
	 * Generates a random certificate serial number.
	 * 
	 * @param existingSerialNums set of existing serial numbers
	 * @return the serial number
	 * @throws CryptographyException thrown when the serial number cannot be generated
	 */
	public static BigInteger generateSerialNum(Set<BigInteger> existingSerialNums) throws CryptographyException
	{
		SecureRandom rand = CryptographyUtils.getRandom(SERIAL_NUM_SEED_SIZE);
		BigInteger serialNum;
		
		do
		{
			serialNum = BigInteger.valueOf(rand.nextLong()).abs();
		}
		while ((existingSerialNums != null) && existingSerialNums.contains(serialNum));
		
		if (existingSerialNums != null)
		{
			existingSerialNums.add(serialNum);
		}
		
		return serialNum;
	}
	//</editor-fold>

	//<editor-fold desc="I/O methods">
	public static void writeCertificate(String path, X509Certificate cert, String encoding) throws CryptographyException
	{
		writeCertificate(new File(path), cert, encoding);
	}
	
	public static void writeCertificate(File file, X509Certificate cert, String encoding) throws CryptographyException
	{
		try
		{
			writeCertificate(new FileOutputStream(file), cert, encoding);
		}
		catch (IOException e)
		{
			throw new CryptographyException("Unable to write X509 certificate to file (" + file + "): encoding=" + 
				encoding, e);
		}
	}
	
	public static void writeCertificate(OutputStream stream, X509Certificate cert, String encoding) throws CryptographyException
	{
		try
		{
			byte[] data = cert.getEncoded();
			
			switch (StringUtils.lowerCase(encoding))
			{
				case DataEncoding.PEM:
					CryptographyUtils.writePemContent(stream, PemType.X509_CERTIFICATE, data);
					break;
					
				case DataEncoding.DER:
					IOUtils.write(data, stream);
					break;
				
				default:
					throw new CryptographyException("Unknown X509 certificate data encoding: " + encoding);
			}
		}
		catch (CertificateEncodingException | IOException e)
		{
			throw new CryptographyException("Unable to write X509 certificate to stream: encoding=" + encoding, e);
		}
	}
	
	public static X509Certificate readCertificate(String path, String encoding) throws CryptographyException
	{
		return readCertificate(new File(path), encoding);
	}
	
	public static X509Certificate readCertificate(File file, String encoding) throws CryptographyException
	{
		try
		{
			return readCertificate(new FileInputStream(file), encoding);
		}
		catch (IOException e)
		{
			throw new CryptographyException("Unable to read X509 certificate from file (" + file + "): encoding=" + 
				encoding, e);
		}
	}
	
	public static X509Certificate readCertificate(InputStream stream, String encoding) throws CryptographyException
	{
		try
		{
			return readCertificate(IOUtils.toByteArray(stream), encoding);
		}
		catch (IOException e)
		{
			throw new CryptographyException("Unable to read X509 certificate from stream: encoding=" + encoding, e);
		}
	}
	
	public static X509Certificate readCertificate(byte[] data, String encoding) throws CryptographyException
	{
		try
		{
			switch (StringUtils.lowerCase(encoding))
			{
				case DataEncoding.PEM:
					data = CryptographyUtils.readPemContent(new ByteArrayInputStream(data));
					
				case DataEncoding.DER:
					Certificate cert = getX509CertFactory().generateCertificate(new ByteArrayInputStream(data));
					
					if (!(cert instanceof X509Certificate))
					{
						throw new CryptographyException("Certificate (type=" + cert.getType() + ") is not a X509 certificate: " + 
							cert);
					}
					
					return (X509Certificate)cert;
				
				default:
					throw new CryptographyException("Unknown X509 certificate data encoding: " + encoding);
			}
		}
		catch (CertificateException e)
		{
			throw new CryptographyException("Unable to read X509 certificate from data (length=" + ArrayUtils.getLength(data) + 
				"): encoding=" + encoding, e);
		}
	}
	//</editor-fold>

	//<editor-fold desc="Factory methods">
	public static CertificateFactory getX509CertFactory() throws CryptographyException
	{
		try
		{
			return CertificateFactory.getInstance(CertificateType.X509, CryptographyUtils.BOUNCY_CASTLE_PROVIDER);
		}
		catch (CertificateException e)
		{
			throw new CryptographyException("Unable to get X509 certificate factory (type=" + CertificateType.X509 + 
				") instance from BouncyCastle provider (name= " + CryptographyUtils.BOUNCY_CASTLE_PROVIDER.getName() + 
				" ).", e);
		}
	}
	//</editor-fold>
}