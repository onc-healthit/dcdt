package gov.hhs.onc.dcdt.utils.data.loader;

import gov.hhs.onc.dcdt.beans.Domain;
import gov.hhs.onc.dcdt.beans.Setting;
import gov.hhs.onc.dcdt.beans.dns.DnsRecord;
import gov.hhs.onc.dcdt.beans.entry.Entry;
import gov.hhs.onc.dcdt.data.DataException;
import gov.hhs.onc.dcdt.utils.LoaderUtility;
import gov.hhs.onc.dcdt.utils.cli.UtilityCli;
import gov.hhs.onc.dcdt.utils.data.service.DataServiceException;
import gov.hhs.onc.dcdt.utils.data.service.DataServiceWrapper;
import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class DataLoader extends LoaderUtility<DataLoaderCliOption>
{
	private final static String UTIL_NAME = "dataloader";
	
	private final static Logger LOGGER = Logger.getLogger(DataLoader.class);
	
	private DataServiceWrapper dataService;
	
	public DataLoader()
	{
		super(UTIL_NAME, new UtilityCli<>(DataLoaderCliOption.class));
	}
	
	public static void main(String ... args)
	{
		new DataLoader().execute(args);
	}
	
	@Override
	protected void execute(String ... args)
	{
		super.execute(args);

		this.loadEntries(this.config.getUtilString(DataLoaderCliOption.INPUT_PATH));
		
		try
		{
			this.dataService = new DataServiceWrapper(this.config.getUtilString(DataLoaderCliOption.WEB_SERVICE_HOST), 
				Integer.parseInt(this.config.getUtilString(DataLoaderCliOption.WEB_SERVICE_PORT)), 
				this.config.getUtilString(DataLoaderCliOption.WEB_SERVICE_PATH));
			
			this.dataService.connect();
			
			for (Entry entry : this.entryMap.values())
			{
				this.loadServiceEntry(entry);
			}
			
			LOGGER.info("Loaded " + this.entryMap.size() + " entries into data service (" + this.dataService.getServiceUrl() + ").");
			
			List<Setting> settings = this.config.getSettings();
			
			for (Setting setting : settings)
			{
				this.loadServiceSetting(setting);
			}
			
			LOGGER.info("Loaded " + settings.size() + " setting(s) into data service (" + this.dataService.getServiceUrl() + ").");

			List<DnsRecord> dnsRecords = new ArrayList<>();
			dnsRecords.addAll(this.config.getARecords());
			dnsRecords.addAll(this.config.getCnameRecords());
			dnsRecords.addAll(this.config.getMxRecords());
			dnsRecords.addAll(this.config.getNsRecords());
			dnsRecords.addAll(this.config.getSoaRecords());
			dnsRecords.addAll(this.config.getSrvRecords());
			
			for (DnsRecord dnsRecord : dnsRecords)
			{
				this.loadServiceDnsRecord(dnsRecord);
			}
			
			LOGGER.info("Loaded " + dnsRecords.size() + " DNS record(s) into data service (" + this.dataService.getServiceUrl() + ").");
		}
		catch (DataServiceException e)
		{
			LOGGER.error(e);
			
			exitError();
		}
	}
	
	@Override
	protected void processCmdLine()
	{
		super.processCmdLine();
		
		this.config.setUtilString(DataLoaderCliOption.DOMAIN);
		this.config.setUtilString(DataLoaderCliOption.DOMAIN_IP);
		this.config.setUtilString(DataLoaderCliOption.WEB_SERVICE_HOST);
		this.config.setUtilString(DataLoaderCliOption.WEB_SERVICE_PATH);
		this.config.setUtilString(DataLoaderCliOption.WEB_SERVICE_PORT);
		
		this.config.setUtilString(DataLoaderCliOption.INPUT_PATH);
		
		File inputPath = new File(this.config.getUtilString(DataLoaderCliOption.INPUT_PATH));
		
		if (!inputPath.exists())
		{
			LOGGER.error("Input path does not exist: " + inputPath);
			
			exitError();
		}
	}
	
	private void loadServiceDnsRecord(DnsRecord dnsRecord) throws DataServiceException
	{
		try
		{
			this.dataService.setDnsRecord(dnsRecord.toDataRecord());
		}
		catch (DataException e)
		{
			throw new DataServiceException(e);
		}
	}
	
	private void loadServiceSetting(Setting setting) throws DataServiceException
	{
		this.dataService.setSetting(setting.getName(), setting.getValue());
	}
	
	private void loadServiceEntry(Entry entry) throws DataServiceException
	{
		byte[] entryData;
		
		if (entry.getDestination().isAnchor())
		{
			try
			{
				entryData = entry.getCertData();
			}
			catch (CertificateEncodingException e)
			{
				throw new DataServiceException("Unable to get entry (name=" + entry.getName() + ") certificate data: " + 
					entry.getCert(), e);
			}
			
			String domainName;
			
			for (Domain domain : this.config.getDomains())
			{
				domainName = domain.getName();
				
				if (!this.dataService.hasDomain(domainName))
				{
					this.dataService.setDomain(domainName);
				}
				
				this.dataService.setAnchor(this.dataService.getDomain(domainName), entryData);
			}
		}
		else if (entry.getDestination().isDns())
		{
			try
			{
				entryData = entry.getKeyStoreData();
			}
			catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException e)
			{
				throw new DataServiceException("Unable to get entry (name=" + entry.getName() + ") keystore data: " + 
					entry.getKeyStore(), e);
			}
			
			this.dataService.setCertificate(entry.getDn().getMail(), entryData);
		}
	}
}