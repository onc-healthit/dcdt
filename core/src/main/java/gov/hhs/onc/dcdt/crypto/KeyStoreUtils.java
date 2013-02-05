package gov.hhs.onc.dcdt.crypto;

import gov.hhs.onc.dcdt.crypto.constants.KeyStoreType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.EnumerationUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class KeyStoreUtils
{
	//<editor-fold desc="Aliases methods">
	@SafeVarargs
	public static String getAlias(KeyStore keyStore, Class<? extends Entry> ... entryClasses)
		throws CryptographyException
	{
		List<String> aliases = getAliases(keyStore, entryClasses);
		
		return !CollectionUtils.isEmpty(aliases) ? aliases.get(0) : null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getAliases(KeyStore keyStore, Class<? extends Entry> ... entryClasses)
		throws CryptographyException
	{
		try
		{
			List<String> aliases = EnumerationUtils.toList(keyStore.aliases());
			
			if (!ArrayUtils.isEmpty(entryClasses))
			{
				Iterator<String> aliasesIterator = aliases.iterator();
				String alias;
				
				while (aliasesIterator.hasNext())
				{
					alias = aliasesIterator.next();
					
					for (Class<? extends Entry> entryClass : entryClasses)
					{
						if (!keyStore.entryInstanceOf(alias, entryClass))
						{
							aliasesIterator.remove();
						}
					}
				}
			}
			
			return aliases;
		}
		catch (KeyStoreException e)
		{
			throw new CryptographyException("Unable to get PKCS#12 key store aliases for entry type(s): [" +
				StringUtils.join(ClassUtils.convertClassesToClassNames(Arrays.asList((Class<?>[])entryClasses)), ", ") + 
				"]", e);
		}
	}
	//</editor-fold>

	//<editor-fold desc="Generation methods">
	public static KeyStore generateKeyStore(String alias, PrivateKey privateKey, String pass, Certificate... certs)
		throws CryptographyException
	{
		KeyStore keyStore = getPkcs12KeyStore(true);
		
		try
		{
			keyStore.setKeyEntry(alias, privateKey, getPassword(pass), certs);
			
			return keyStore;
		}
		catch (KeyStoreException e)
		{
			throw new CryptographyException("Unable to generate PKCS#12 key store for entry (alias=" + alias + ").", e);
		}
	}
	//</editor-fold>

	//<editor-fold desc="I/O methods">
	public static void writeKeyStore(String path, KeyStore keyStore, String pass) throws CryptographyException
	{
		writeKeyStore(new File(path), keyStore, pass);
	}
	
	public static void writeKeyStore(File file, KeyStore keyStore, String pass) throws CryptographyException
	{
		try
		{
			writeKeyStore(new FileOutputStream(file), keyStore, pass);
		}
		catch (IOException e)
		{
			throw new CryptographyException("Unable to write PKCS#12 key store to file (" + file + ").", e);
		}
	}
	
	public static void writeKeyStore(OutputStream stream, KeyStore keyStore, String pass) throws CryptographyException
	{
		try
		{
			keyStore.store(stream, getPassword(pass));
		}
		catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException e)
		{
			throw new CryptographyException("Unable to write PKCS#12 key store to stream.", e);
		}
	}
	
	public static KeyStore readKeyStore(String path, String pass) throws CryptographyException
	{
		return readKeyStore(new File(path), pass);
	}
	
	public static KeyStore readKeyStore(File file, String pass) throws CryptographyException
	{
		try
		{
			return readKeyStore(new FileInputStream(file), pass);
		}
		catch (IOException e)
		{
			throw new CryptographyException("Unable to read PKCS#12 key store from file (" + file + ").", e);
		}
	}
	
	public static KeyStore readKeyStore(InputStream stream, String pass) throws CryptographyException
	{
		KeyStore keyStore = getPkcs12KeyStore();
		
		try
		{
			keyStore.load(stream, getPassword(pass));
			
			return keyStore;
		}
		catch (CertificateException | IOException | NoSuchAlgorithmException e)
		{
			throw new CryptographyException("Unable to read PKCS#12 key store from stream.", e);
		}
	}
	//</editor-fold>

	//<editor-fold desc="Instantiation methods">
	public static KeyStore getPkcs12KeyStore() throws CryptographyException
	{
		return getPkcs12KeyStore(false);
	}
	
	public static KeyStore getPkcs12KeyStore(boolean empty) throws CryptographyException
	{
		try
		{
			KeyStore keyStore = KeyStore.getInstance(KeyStoreType.PKCS12, CryptographyUtils.BOUNCY_CASTLE_PROVIDER);
			
			if (empty)
			{
				keyStore.load(null, null);
			}
			
			return keyStore;
		}
		catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException e)
		{
			throw new CryptographyException("Unable to get PKCS#12 key store (type=" + KeyStoreType.PKCS12 + 
				") instance from BouncyCastle provider (name= " + CryptographyUtils.BOUNCY_CASTLE_PROVIDER.getName() + 
				" ): empty=" + empty, e);
		}
	}
	//</editor-fold>

	//<editor-fold desc="Helper methods">
	public static char[] getPassword(String pass)
	{
		return StringUtils.isEmpty(pass) ? ArrayUtils.EMPTY_CHAR_ARRAY : pass.toCharArray();
	}
	//</editor-fold>
}