package gov.hhs.onc.dcdt.utils.data.generator;

import gov.hhs.onc.dcdt.utils.Utility;
import gov.hhs.onc.dcdt.utils.beans.BeanAttrib;
import gov.hhs.onc.dcdt.utils.beans.Entry;
import gov.hhs.onc.dcdt.utils.cli.UtilityCli;
import gov.hhs.onc.dcdt.utils.config.UtilityConfig;
import gov.hhs.onc.dcdt.utils.data.entry.EntryBuilder;
import gov.hhs.onc.dcdt.utils.data.entry.EntryException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

public class DataGen extends Utility<DataGenCliOption>
{
	private final static String UTIL_NAME = "datagen";
	
	private final static String OUTPUT_FILE_ARCHIVE_PATH_ATTRIB_NAME = DataGenCliOption.OUTPUT_FILE.getAttribName() + "ArchivePath";
	
	private final static String ENTRY_CA_ID = "ca";
	
	private final static String PKCS8_PEM_TYPE = "PRIVATE KEY";
	private final static String X509_CERT_PEM_TYPE = "CERTIFICATE";
	
	private final static Logger LOGGER = Logger.getLogger(DataGen.class);
	
	private EntryBuilder entryBuilder;
	
	public DataGen()
	{
		super(UTIL_NAME, new UtilityCli<>(DataGenCliOption.class));
	}
	
	public static void main(String ... args)
	{
		new DataGen().execute(args);
	}
	
	@Override
	protected void execute(String ... args)
	{
		super.execute(args);
		
		List<Entry> entries = new ArrayList<>();
		Entry caEntry = this.config.getBeanById(Entry.class, ENTRY_CA_ID);
		caEntry.setIssuer(caEntry);
		
		try
		{
			this.entryBuilder.generateCa(caEntry);
			
			LOGGER.info("Successfully built Certificate Authority (CA) entry: " + caEntry);
		}
		catch (EntryException e)
		{
			LOGGER.error("Unable to build Certificate Authority (CA) entry: " + caEntry, e);
			
			exitError();
		}
		
		entries.add(caEntry);
		
		for (Entry leafEntry : this.config.getEntries(new BeanAttrib(UtilityConfig.BEAN_ID_ATTRIB_KEY, ENTRY_CA_ID, true)))
		{
			leafEntry.setIssuer(caEntry);
			
			try
			{
				this.entryBuilder.generateLeaf(leafEntry);
				
				LOGGER.info("Successfully built leaf entry: " + leafEntry);
			}
			catch (EntryException e)
			{
				LOGGER.error("Unable to build leaf entry: " + leafEntry, e);
				
				exitError();
			}
			
			entries.add(leafEntry);
		}
		
		writeOutputFile(new File(this.config.getUtilString(DataGenCliOption.OUTPUT_FILE)), 
			this.config.getUtilString(OUTPUT_FILE_ARCHIVE_PATH_ATTRIB_NAME), entries);
	}

	@Override
	protected void processCmdLine()
	{
		super.processCmdLine();
		
		this.config.setUtilString(DataGenCliOption.DOMAIN);
		
		this.config.setUtilString(DataGenCliOption.OUTPUT_FILE);
		
		String outputFilePath = this.config.getUtilString(DataGenCliOption.OUTPUT_FILE);
		
		if (StringUtils.isBlank(outputFilePath))
		{
			LOGGER.error("Output file path must be specified.");
			
			exitError();
		}
		
		File outputFile = new File(outputFilePath);
		
		if (outputFile.exists() && !outputFile.isFile())
		{
			LOGGER.error("Output file path is not a file: " + outputFile);
			
			exitError();
		}
	}

	@Override
	protected void init()
	{
		super.init();
		
		this.entryBuilder = new EntryBuilder();
	}
	
	private static void writeOutputFile(File outputFile, String outputFileArchivePath, List<Entry> entries)
	{
		if (!outputFile.exists())
		{
			try
			{
				FileUtils.touch(outputFile);
			}
			catch (IOException e)
			{
				LOGGER.error("Unable to create output file: " + outputFile, e);
			}
		}
		
		try (ZipOutputStream outputFileStream = new ZipOutputStream(new FileOutputStream(outputFile)))
		{
			for (Entry entry : entries)
			{
				writeEntryFiles(outputFileStream, outputFileArchivePath, entry);
			}
			
			outputFileStream.finish();
			outputFileStream.close();
			
			LOGGER.info("Successfully wrote output file: " + outputFile);
		}
		catch (EntryException | IOException e)
		{
			LOGGER.error("Unable to write output file: " + outputFile, e);
			
			exitError();
		}
	}
	
	private static void writeEntryFiles(ZipOutputStream outputFileStream, String outputFileArchivePath, Entry entry) throws EntryException
	{
		byte[] entryKeyData = entry.getKeyData(), entryCertData = null, entryKeyStoreData = null;
		
		try
		{
			entryCertData = entry.getCertData();
		}
		catch (CertificateEncodingException e)
		{
			throw new EntryException("Unable to get entry certificate data: " + entry.getCert(), e);
		}
		
		try
		{
			entryKeyStoreData = entry.getKeyStoreData();
		}
		catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException e)
		{
			throw new EntryException("Unable to get entry keystore data: " + entry.getKeyStore(), e);
		}
		
		try
		{
			writeEntryData(outputFileStream, outputFileArchivePath, entry.getKeyDerFilePath(), entryKeyData);
		}
		catch (IOException e)
		{
			throw new EntryException("Unable to write DER-encoded private key to archived file (path=" + entry.getKeyDerFilePath() + "): " + 
				entry.getPrivateKey(), e);
		}
		
		try
		{
			writeEntryData(outputFileStream, outputFileArchivePath, entry.getKeyPemFilePath(),
				getPemData(new PemObject(PKCS8_PEM_TYPE, entryKeyData)));
		}
		catch (IOException e)
		{
			throw new EntryException("Unable to write PEM-encoded private key to archived file (path=" + entry.getKeyPemFilePath() + "): " + 
				entry.getPrivateKey(), e);
		}
		
		try
		{
			writeEntryData(outputFileStream, outputFileArchivePath, entry.getCertDerFilePath(), entryCertData);
		}
		catch (IOException e)
		{
			throw new EntryException("Unable to write DER-encoded certificate to archived file (path=" + entry.getCertDerFilePath() + "): " + 
				entry.getCert(), e);
		}
		
		try
		{
			writeEntryData(outputFileStream, outputFileArchivePath, entry.getCertPemFilePath(),
				getPemData(new PemObject(X509_CERT_PEM_TYPE, entryCertData)));
		}
		catch (IOException e)
		{
			throw new EntryException("Unable to write PEM-encoded certificate to archived file (path=" + entry.getCertPemFilePath() + "): " + 
				entry.getPrivateKey(), e);
		}

		try
		{
			writeEntryData(outputFileStream, outputFileArchivePath, entry.getKeyStoreFilePath(), entryKeyStoreData);
		}
		catch (IOException e)
		{
			throw new EntryException("Unable to write keystore to archived file (path=" + entry.getKeyStoreFilePath() + "): " + 
				entry.getKeyStore(), e);
		}
	}
	
	private static void writeEntryData(ZipOutputStream outputFileStream, String outputFileArchivePath, String entryFilePath, byte[] data)
		throws IOException
	{
		outputFileStream.putNextEntry(new ZipEntry(outputFileArchivePath + File.separatorChar + entryFilePath));
			outputFileStream.write(data);
			outputFileStream.closeEntry();
	}
	
	private static byte[] getPemData(PemObject pemObj) throws IOException
	{
		ByteArrayOutputStream pemDataOutputStream = new ByteArrayOutputStream();
		PemWriter pemWriter = new PemWriter(new OutputStreamWriter(pemDataOutputStream));
		pemWriter.writeObject(pemObj);
		pemWriter.flush();
		
		return pemDataOutputStream.toByteArray();
	}
}