package gov.hhs.onc.dcdt.utils.entry;

import gov.hhs.onc.dcdt.utils.beans.Entry;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EntryLoader
{
	private final static int ARCHIVE_ENTRY_DATA_BUFFER_SIZE = 4096;
	
	public void loadEntry(Entry entry, String inputPath, String inputContainerPath) throws EntryException
	{
		File inputPathFile = new File(inputPath);
		
		try
		{
			InputStream keyStoreInStream = getEntryFileInStream(inputPathFile, inputContainerPath, entry.getKeyStoreFilePath());
			
			if (keyStoreInStream != null)
			{
				KeyStore keyStore = KeyStore.getInstance(Entry.PKCS12_KEYSTORE_TYPE, BouncyCastleProvider.PROVIDER_NAME);
				keyStore.load(keyStoreInStream, ArrayUtils.EMPTY_CHAR_ARRAY);
				entry.setKeyStore(keyStore);
				
				String alias = keyStore.aliases().nextElement();
				entry.setCert((X509Certificate)keyStore.getCertificate(alias));
				entry.setKeyPair(new KeyPair(entry.getCert().getPublicKey(), (PrivateKey)keyStore.getKey(alias, 
					ArrayUtils.EMPTY_CHAR_ARRAY)));
			}
		}
		catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | 
			UnrecoverableKeyException e)
		{
			// TODO: finish exception
			throw new EntryException(e);
		}
	}
	
	private static InputStream getEntryFileInStream(File inputPathFile, String inputContainerPath, String entryFilePath)
		throws IOException
	{
		return inputPathFile.isDirectory() ? getExtractedEntryFileInStream(inputPathFile, inputContainerPath, entryFilePath) : 
			getArchiveEntryFileInStream(inputPathFile, inputContainerPath, entryFilePath);
	}
	
	private static InputStream getExtractedEntryFileInStream(File inputDir, String inputSubPath, String entryFilePath)
		throws IOException
	{
		File entryFile = Paths.get(inputDir.getName(), inputSubPath, entryFilePath).toFile();
		
		return entryFile.exists() ? new FileInputStream(entryFile) : null;
	}
	
	private static InputStream getArchiveEntryFileInStream(File inputArchiveFile, String inputArchivePath, String entryFilePath)
		throws IOException
	{
		if (inputArchiveFile.exists())
		{
			String entryArchiveFilePath = Paths.get(inputArchivePath, entryFilePath).toString();
			
			try (ZipInputStream archiveInStream = new ZipInputStream(new FileInputStream(inputArchiveFile)))
			{
				ZipEntry archiveEntry;
				byte[] archiveEntryData = new byte[ARCHIVE_ENTRY_DATA_BUFFER_SIZE];
				int archiveEntryDataLength;
				
				while ((archiveEntry = archiveInStream.getNextEntry()) != null)
				{
					ByteArrayOutputStream archiveEntryDataOutStream = new ByteArrayOutputStream();
					
					while ((archiveEntryDataLength = archiveInStream.read(archiveEntryData, 0, archiveEntryData.length)) != -1)
					{
						archiveEntryDataOutStream.write(archiveEntryData, 0, archiveEntryDataLength);
					}
					
					archiveInStream.closeEntry();
					
					if (StringUtils.equals(archiveEntry.getName(), entryArchiveFilePath))
					{
						return new ByteArrayInputStream(archiveEntryDataOutStream.toByteArray());
					}
				}
			}
		}
		
		return null;
	}
}