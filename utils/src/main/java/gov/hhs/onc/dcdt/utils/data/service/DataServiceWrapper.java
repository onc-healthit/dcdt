package gov.hhs.onc.dcdt.utils.data.service;

import gov.hhs.onc.dcdt.beans.entry.Entry;
import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.rpc.JAXRPCException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.nhind.config.Address;
import org.nhind.config.Anchor;
import org.nhind.config.Certificate;
import org.nhind.config.CertificateGetOptions;
import org.nhind.config.ConfigurationService;
import org.nhind.config.ConfigurationServiceProxy;
import org.nhind.config.DnsRecord;
import org.nhind.config.Domain;
import org.nhind.config.EntityStatus;
import org.nhind.config.Setting;
import org.xbill.DNS.Type;

public class DataServiceWrapper
{
	private final static String HTTP_PROTOCOL = "http";
	
	private final static CertificateGetOptions GET_ANCHORS_OPTIONS = new CertificateGetOptions(true, true, null);
	private final static CertificateGetOptions LIST_ANCHORS_OPTIONS = new CertificateGetOptions(true, true, null);
	private final static int LIST_ANCHORS_MAX_RESULTS = 1000;
	private final static CertificateGetOptions GET_CERTS_OPTIONS = new CertificateGetOptions(true, true, null);
	private final static CertificateGetOptions LIST_CERTS_OPTIONS = new CertificateGetOptions(true, true, null);
	private final static int LIST_CERTS_MAX_RESULTS = 1000;
	private final static int LIST_DOMAINS_MAX_RESULTS = 1000;
	
	private final static Logger LOGGER = Logger.getLogger(DataServiceWrapper.class);
	
	private String serviceHost;
	private int servicePort;
	private String servicePath;
	private URL serviceUrl;
	private ConfigurationService service;
	
	public DataServiceWrapper(String serviceHost, int servicePort, String servicePath)
	{
		this.serviceHost = serviceHost;
		this.servicePort = servicePort;
		this.servicePath = servicePath;
	}
	
	public static Certificate newCertificate(String owner, byte[] data)
	{
		Certificate cert = new Certificate();
		cert.setOwner(owner);
		cert.setData(data);
		cert.setStatus(EntityStatus.ENABLED);
		
		return cert;
	}
	
	public static Setting newSetting(String name, Object value)
	{
		Setting setting = new Setting();
		setting.setName(name);
		setting.setValue(ObjectUtils.toString(value));
		setting.setStatus(EntityStatus.ENABLED);
		
		return setting;
	}
	
	public static Anchor newAnchor(String owner, byte[] data)
	{
		Anchor anchor = new Anchor();
		anchor.setOwner(owner);
		anchor.setData(data);
		anchor.setIncoming(true);
		anchor.setOutgoing(true);
		anchor.setStatus(EntityStatus.ENABLED);
		
		return anchor;
	}
	
	public static Domain newDomain(String domainName)
	{
		Domain domain = new Domain();
		domain.setDomainName(domainName);
		domain.setStatus(EntityStatus.ENABLED);
		
		return domain;
	}
	
	public boolean setDnsRecord(DnsRecord dnsRecord) throws DataServiceException
	{
		List<DnsRecord> existingDnsRecords = this.getDnsRecords(dnsRecord.getType(), dnsRecord.getName());
		DnsRecord existingMatchingDnsRecord = null;
		
		for (DnsRecord existingDnsRecord : existingDnsRecords)
		{
			if ((dnsRecord.getType() == existingDnsRecord.getType()) && 
				StringUtils.equals(dnsRecord.getName(), existingDnsRecord.getName()) && 
				Arrays.equals(dnsRecord.getData(), existingDnsRecord.getData()))
			{
				existingMatchingDnsRecord = existingDnsRecord;
				
				break;
			}
		}
		
		if (existingMatchingDnsRecord != null)
		{
			mergeDnsRecord(existingMatchingDnsRecord, dnsRecord);
			
			try
			{
				this.service.updateDNS(dnsRecord.getId(), dnsRecord);
			}
			catch (RemoteException e)
			{
				throw new DataServiceException("Unable to update DNS record in data service (" + this.serviceUrl + "): " + 
					dnsRecordsToString(dnsRecord), e);
			}
			
			LOGGER.debug("Updated DNS record in data service (" + this.serviceUrl + "): " + dnsRecordsToString(dnsRecord));
		}
		else
		{
			try
			{
				this.service.addDNS(new DnsRecord[]{ dnsRecord });
			}
			catch (RemoteException e)
			{
				throw new DataServiceException("Unable to add DNS record to data service (" + this.serviceUrl + "): " + 
					dnsRecordsToString(dnsRecord), e);
			}
			
			LOGGER.debug("Added DNS record to data service (" + this.serviceUrl + "): " + dnsRecordsToString(dnsRecord));
		}
		
		return existingMatchingDnsRecord == null;
	}
	
	public DnsRecord getDnsRecord(int type, String name) throws DataServiceException
	{
		List<DnsRecord> dnsRecords = this.getDnsRecords(type, name);
		
		return !CollectionUtils.isEmpty(dnsRecords) ? dnsRecords.get(0) : null;
	}
	
	public List<DnsRecord> getDnsRecords(int type, String name) throws DataServiceException
	{
		List<DnsRecord> dnsRecords = new ArrayList<>();
		DnsRecord[] dnsRecordsArr;
		
		try
		{
			dnsRecordsArr = this.service.getDNSByNameAndType(name, type);
			
			if (!ArrayUtils.isEmpty(dnsRecordsArr))
			{
				dnsRecords.addAll(Arrays.asList(dnsRecordsArr));
			}
		}
		catch (RemoteException e)
		{
			// TODO: finish exception
			throw new DataServiceException(e);
		}
		
		if (LOGGER.isTraceEnabled())
		{
			LOGGER.trace("Found " + dnsRecords.size() + " DNS record(s) in data service (" + this.serviceUrl + "): " + 
				dnsRecordsToString(dnsRecords));
		}
		else
		{
			LOGGER.debug("Found " + dnsRecords.size() + " DNS record(s) in data service (" + this.serviceUrl + ").");
		}
		
		return dnsRecords;
	}
	
	public boolean setCertificate(String certOwner, byte[] certData) throws DataServiceException
	{
		return this.setCertificate(newCertificate(certOwner, certData));
	}
	
	public boolean setCertificate(Certificate cert) throws DataServiceException
	{
		String certOwner = cert.getOwner();
		Certificate existingCert = this.getCertificate(certOwner);
		
		if (existingCert != null)
		{
			try
			{
				this.service.removeCertificates(new long[]{ existingCert.getId() });
			}
			catch (RemoteException e)
			{
				throw new DataServiceException("Unable to remove certificate from data service (" + this.serviceUrl + "): " + 
					certificatesToString(existingCert), e);
			}
			
			LOGGER.debug("Removed certificate from data service (" + this.serviceUrl + "): " + certificatesToString(existingCert));
		}
		
		try
		{
			this.service.addCertificates(new Certificate[]{ cert });
		}
		catch (RemoteException e)
		{
			throw new DataServiceException("Unable to add certificate to data service (" + this.serviceUrl + "): " + 
				certificatesToString(cert), e);
		}
		
		LOGGER.debug("Added certificate to data service (" + this.serviceUrl + "): " + certificatesToString(cert));
		
		return existingCert == null;
	}
	
	public boolean hasCertificate(String certOwner) throws DataServiceException
	{
		return this.getCertificate(certOwner) != null;
	}
	
	public boolean hasAllCertificates(String ... certOwners) throws DataServiceException
	{
		return this.hasAllCertificates(Arrays.asList(certOwners));
	}
	
	public boolean hasAllCertificates(Collection<String> certOwners) throws DataServiceException
	{
		return !this.getAllCertificates(certOwners).isEmpty();
	}
	
	public Certificate getCertificate(String certOwner) throws DataServiceException
	{
		List<Certificate> certs = this.getCertificates(certOwner);
		
		return !CollectionUtils.isEmpty(certs) ? certs.get(0) : null;
	}
	
	public List<Certificate> getCertificates(String certOwner) throws DataServiceException
	{
		return this.getAllCertificates(certOwner).get(certOwner);
	}
	
	public Map<String, List<Certificate>> getAllCertificates(String ... certOwners) throws DataServiceException
	{
		return this.getAllCertificates(Arrays.asList(certOwners));
	}
	
	public Map<String, List<Certificate>> getAllCertificates(Collection<String> certOwners) throws DataServiceException
	{
		Map<String, List<Certificate>> certsMap = new LinkedHashMap<>();
		Certificate[] certsArr;
		
		if (CollectionUtils.isEmpty(certOwners))
		{
			try
			{
				certsArr = this.service.listCertificates(0L, LIST_CERTS_MAX_RESULTS, LIST_CERTS_OPTIONS);
			}
			catch (RemoteException e)
			{
				throw new DataServiceException("Unable to list certificates in data service (" + this.serviceUrl + ").", e);
			}
			
			mapCerts(certsMap, certsArr);
		}
		else
		{
			for (String certOwner : certOwners)
			{
				try
				{
					certsArr = this.service.getCertificatesForOwner(certOwner, GET_CERTS_OPTIONS);
				}
				catch (RemoteException e)
				{
					throw new DataServiceException("Unable to get certificates for owner in data service (" + this.serviceUrl + "): " + 
						certOwner, e);
				}
				
				mapCerts(certsMap, certsArr);
			}
		}
		
		if (LOGGER.isTraceEnabled())
		{
			Set<Certificate> certsSet = new LinkedHashSet<>();
			
			for (List<Certificate> certs : certsMap.values())
			{
				certsSet.addAll(certs);
			}
			
			LOGGER.trace("Found " + certsSet.size() + " certificate(s) in data service (" + this.serviceUrl + "): " + 
				certificatesToString(certsSet));
		}
		else
		{
			LOGGER.debug("Found " + certsMap.size() + " certificate(s) in data service (" + this.serviceUrl + ").");
		}
		
		return certsMap;
	}
	
	public boolean setSetting(String settingName, Object settingValue) throws DataServiceException
	{
		return this.setSetting(newSetting(settingName, settingValue));
	}
	
	public boolean setSetting(Setting setting) throws DataServiceException
	{
		Setting existingSetting = this.getSetting(setting.getName());
		
		if (existingSetting != null)
		{
			try
			{
				this.service.updateSetting(setting.getName(), setting.getValue());
			}
			catch (RemoteException e)
			{
				throw new DataServiceException("Unable to update setting in data service (" + this.serviceUrl + "): " + 
					settingsToString(setting), e);
			}
			
			LOGGER.debug("Updated setting in data service (" + this.serviceUrl + "): " + settingsToString(setting));
			
			return false;
		}
		else
		{
			try
			{
				this.service.addSetting(setting.getName(), setting.getValue());
			}
			catch (RemoteException e)
			{
				throw new DataServiceException("Unable to add setting to data service (" + this.serviceUrl + "): " + 
					settingsToString(setting), e);
			}
			
			LOGGER.debug("Added setting to data service (" + this.serviceUrl + "): " + settingsToString(setting));
			
			return true;
		}
	}
	
	public boolean hasSetting(String settingName) throws DataServiceException
	{
		return this.getSetting(settingName) != null;
	}
	
	public boolean hasSettings(String ... settingNames) throws DataServiceException
	{
		return this.hasSettings(Arrays.asList(settingNames));
	}
	
	public boolean hasSettings(Collection<String> settingNames) throws DataServiceException
	{
		return !this.getSettings(settingNames).isEmpty();
	}
	
	public Setting getSetting(String settingName) throws DataServiceException
	{
		return this.getSettings(settingName).get(settingName);
	}
	
	public Map<String, Setting> getSettings(String ... settingNames) throws DataServiceException
	{
		return this.getSettings(Arrays.asList(settingNames));
	}
	
	public Map<String, Setting> getSettings(Collection<String> settingNames) throws DataServiceException
	{
		Map<String, Setting> settingsMap = new LinkedHashMap<>();
		Setting[] settings;
		
		try
		{
			switch (settingNames.size())
			{
				case 0:
					settings = this.service.getAllSettings();
					break;
				
				case 1:
					settings = new Setting[]{ this.service.getSettingByName((String)CollectionUtils.get(settingNames, 0)) };
					break;
				
				default:
					settings = this.service.getSettingsByNames(settingNames.toArray(new String[settingNames.size()]));
					break;
			}
			
			if (settings != null)
			{
				for (Setting setting : settings)
				{
					if (setting != null)
					{
						settingsMap.put(setting.getName(), setting);
					}
				}
			}
		}
		catch (RemoteException e)
		{
			throw new DataServiceException("Unable to get setting(s) by name(s) from data service (" + this.serviceUrl + 
				"): settingNames=[" + StringUtils.join(settingNames, ", ") + "]", e);
		}
		
		if (LOGGER.isTraceEnabled())
		{
			LOGGER.trace("Found " + settingsMap.size() + " setting(s) in data service (" + this.serviceUrl + "): " + 
				settingsToString(settingsMap.values()));
		}
		else
		{
			LOGGER.debug("Found " + settingsMap.size() + " setting(s) in data service (" + this.serviceUrl + ").");
		}
		
		return settingsMap;
	}
	
	public boolean setAnchor(Domain domain, byte[] anchorCertData) throws DataServiceException
	{
		return this.setAnchor(domain, newAnchor(domain.getDomainName(), anchorCertData));
	}
	
	public boolean setAnchor(Domain domain, Anchor anchor) throws DataServiceException
	{
		CertificateFactory anchorCertFactory;
		X509Certificate anchorCert, existingAnchorCert;
		
		try
		{
			anchorCertFactory = CertificateFactory.getInstance(Entry.X509_CERT_TYPE, BouncyCastleProvider.PROVIDER_NAME);
		}
		catch (CertificateException | NoSuchProviderException e)
		{
			throw new DataServiceException("Unable to create anchor certificate factory.", e);
		}
		
		try
		{
			anchorCert = (X509Certificate)anchorCertFactory.generateCertificate(new ByteArrayInputStream(anchor.getData()));
		}
		catch (CertificateException e)
		{
			throw new DataServiceException("Unable to create anchor certificate.", e);
		}
		
		Anchor existingDomainAnchor = null;
		
		for (Anchor existingAnchor : this.getAnchors(domain))
		{
			try
			{
				existingAnchorCert = (X509Certificate)anchorCertFactory.generateCertificate(new ByteArrayInputStream(existingAnchor.getData()));
			}
			catch (CertificateException e)
			{
				throw new DataServiceException("Unable to create existing anchor certificate.", e);
			}
			
			if (anchorCert.getSubjectDN().equals(existingAnchorCert.getSubjectDN()))
			{
				existingDomainAnchor = existingAnchor;
				
				break;
			}
		}
		
		if (existingDomainAnchor != null)
		{
			mergeAnchor(existingDomainAnchor, anchor);
			
			try
			{
				this.service.removeAnchors(new long[]{ existingDomainAnchor.getId() });
			}
			catch (RemoteException e)
			{
				throw new DataServiceException("Unable to remove anchor from data service (" + this.serviceUrl + "): " + 
					anchorsToString(existingDomainAnchor), e);
			}
			
			LOGGER.debug("Removed anchor from data service (" + this.serviceUrl + "): " + anchorsToString(existingDomainAnchor));
		}
		
		try
		{
			this.service.addAnchor(new Anchor[]{ anchor });
		}
		catch (RemoteException e)
		{
			throw new DataServiceException("Unable to add anchor to data service (" + this.serviceUrl + "): " + 
				anchorsToString(anchor), e);
		}
		
		LOGGER.debug("Added anchor to data service (" + this.serviceUrl + "): " + anchorsToString(anchor));
		
		return existingDomainAnchor == null;
	}
	
	public boolean hasAnchor(Domain domain) throws DataServiceException
	{
		return this.getAnchor(domain) != null;
	}
	
	public boolean hasAllAnchors(Domain ... domains) throws DataServiceException
	{
		return this.hasAllAnchors(Arrays.asList(domains));
	}
	
	public boolean hasAllAnchors(Collection<Domain> domains) throws DataServiceException
	{
		return !this.getAllAnchors(domains).isEmpty();
	}
	
	public Anchor getAnchor(Domain domain) throws DataServiceException
	{
		List<Anchor> anchors = this.getAnchors(domain);
		
		return !CollectionUtils.isEmpty(anchors) ? anchors.get(0) : null;
	}
	
	public List<Anchor> getAnchors(Domain domain) throws DataServiceException
	{
		return this.getAllAnchors(domain).get(domain);
	}
	
	public Map<Domain, List<Anchor>> getAllAnchors(Domain ... domains) throws DataServiceException
	{
		return this.getAllAnchors(Arrays.asList(domains));
	}
	
	public Map<Domain, List<Anchor>> getAllAnchors(Collection<Domain> domains) throws DataServiceException
	{
		Map<Domain, List<Anchor>> anchorsMap = new LinkedHashMap<>();
		List<Anchor> domainAnchors;
		
		try
		{
			if (CollectionUtils.isEmpty(domains))
			{
				String anchorOwner;
				Domain domain;
				
				for (Anchor anchor : this.service.listAnchors(0L, LIST_ANCHORS_MAX_RESULTS, LIST_ANCHORS_OPTIONS))
				{
					anchorOwner = anchor.getOwner();
					
					domain = new Domain();
					domain.setDomainName(anchorOwner);
					
					if (!anchorsMap.containsKey(domain))
					{
						domain = this.getDomain(anchorOwner);
						
						if (domain == null)
						{
							throw new DataServiceException("Domain for anchor not found: " + anchor);
						}
						
						anchorsMap.put(domain, new ArrayList<Anchor>());
					}
					
					domainAnchors = anchorsMap.get(domain);
					domainAnchors.add(anchor);
				}
			}
			else
			{
				Anchor[] domainAnchorsArr;
				
				for (Domain domain : domains)
				{
					if (!anchorsMap.containsKey(domain))
					{
						anchorsMap.put(domain, new ArrayList<Anchor>());
					}
					
					domainAnchors = anchorsMap.get(domain);
					domainAnchorsArr = this.service.getAnchorsForOwner(domain.getDomainName(), GET_ANCHORS_OPTIONS);
					
					if (domainAnchorsArr != null)
					{
						domainAnchors.addAll(Arrays.asList(domainAnchorsArr));
					}
				}
			}
		}
		catch (RemoteException e)
		{
			throw new DataServiceException("Unable to get anchor(s) by domain(s) from data service (" + this.serviceUrl + 
				"): domains=[" + StringUtils.join(domains, ", ") + "]", e);
		}
		
		if (LOGGER.isTraceEnabled())
		{
			Set<Anchor> anchorsSet = new LinkedHashSet<>();
			
			for (List<Anchor> anchors : anchorsMap.values())
			{
				anchorsSet.addAll(anchors);
			}
			
			LOGGER.trace("Found " + anchorsSet.size() + " anchors(s) in data service (" + this.serviceUrl + "): " + 
				anchorsToString(anchorsSet));
		}
		else
		{
			LOGGER.debug("Found " + anchorsMap.size() + " anchors(s) in data service (" + this.serviceUrl + ").");
		}
		
		return anchorsMap;
	}
	
	public boolean setDomain(String domainName) throws DataServiceException
	{
		return this.setDomain(newDomain(domainName));
	}
	
	public boolean setDomain(Domain domain) throws DataServiceException
	{
		String domainName = domain.getDomainName();
		Map<String, Domain> domainsMap = this.getDomains(domainName);
		
		if (domainsMap.containsKey(domainName))
		{
			mergeDomain(domainsMap.get(domainName), domain);
			
			try
			{
				this.service.updateDomain(domain);
			}
			catch (RemoteException e)
			{
				throw new DataServiceException("Unable to set domain in data service (" + this.serviceUrl + "): " + 
					domainsToString(domain), e);
			}
			
			LOGGER.debug("Updated domain in data service (" + this.serviceUrl + "): " + domainsToString(domain));
			
			return false;
		}
		else
		{
			try
			{
				this.service.addDomain(domain);
			}
			catch (RemoteException e)
			{
				throw new DataServiceException("Unable to add domain to data service (" + this.serviceUrl + "): " + 
					domainsToString(domain), e);
			}
			
			LOGGER.debug("Added domain to data service (" + this.serviceUrl + "): " + domainsToString(domain));
			
			return true;
		}
	}
	
	public boolean hasDomain(String domainName) throws DataServiceException
	{
		return this.getDomain(domainName) != null;
	}
	
	public boolean hasDomains(String ... domainNames) throws DataServiceException
	{
		return this.hasDomains(Arrays.asList(domainNames));
	}
	
	public boolean hasDomains(Collection<String> domainNames) throws DataServiceException
	{
		return !this.getDomains(domainNames).isEmpty();
	}
	
	public Domain getDomain(String domainName) throws DataServiceException
	{
		return this.getDomains(domainName).get(domainName);
	}
	
	public Map<String, Domain> getDomains(String ... domainNames) throws DataServiceException
	{
		return this.getDomains(Arrays.asList(domainNames));
	}
	
	public Map<String, Domain> getDomains(Collection<String> domainNames) throws DataServiceException
	{
		Map<String, Domain> domainsMap = new LinkedHashMap<>();
		
		try
		{
			Domain[] domains = !CollectionUtils.isEmpty(domainNames) ? 
				this.service.getDomains(domainNames.toArray(new String[domainNames.size()]), null) : 
				this.service.listDomains(null, LIST_DOMAINS_MAX_RESULTS);
			
			if (domains != null)
			{
				for (Domain domain : domains)
				{
					domainsMap.put(domain.getDomainName(), domain);
				}
			}
		}
		catch (RemoteException e)
		{
			throw new DataServiceException("Unable to get domain(s) by name(s) from data service (" + this.serviceUrl + 
				"): domainNames=[" + StringUtils.join(domainNames, ", ") + "]", e);
		}
		
		if (LOGGER.isTraceEnabled())
		{
			LOGGER.trace("Found " + domainsMap.size() + " domain(s) in data service (" + this.serviceUrl + "): " + 
				domainsToString(domainsMap.values()));
		}
		else
		{
			LOGGER.debug("Found " + domainsMap.size() + " domain(s) in data service (" + this.serviceUrl + ").");
		}
		
		return domainsMap;
	}
	
	public void connect() throws DataServiceException
	{
		try
		{
			this.serviceUrl = new URL(HTTP_PROTOCOL, this.serviceHost, this.servicePort, this.servicePath);
		}
		catch (MalformedURLException e)
		{
			throw new DataServiceException("Malformed data service url: protocol=" + HTTP_PROTOCOL + 
				", host=" + this.serviceHost + ", port=" + this.servicePort + ", path=" + this.servicePath, e);
		}
		
		try
		{
			this.service = new ConfigurationServiceProxy(this.serviceUrl.toString());
		}
		catch (JAXRPCException e)
		{
			throw new DataServiceException("Unable to connect to data service (" + this.serviceUrl + ").", e);
		}
		
		LOGGER.info("Connected to data service (" + this.serviceUrl + ").");
	}
	
	private static void mapCerts(Map<String, List<Certificate>> certsMap, Certificate[] certsArr)
	{
		if (certsArr != null)
		{
			List<Certificate> certs;
			String certOwner;
			
			for (Certificate cert : certsArr)
			{
				certOwner = cert.getOwner();
				
				if (!certsMap.containsKey(certOwner))
				{
					certsMap.put(certOwner, new ArrayList<Certificate>());
				}
				
				certs = certsMap.get(certOwner);
				certs.add(cert);
				
				certsMap.put(certOwner, certs);
			}
		}
	}
	
	private static DnsRecord mergeDnsRecord(DnsRecord existingDnsRecord, DnsRecord dnsRecord)
	{
		dnsRecord.setId(existingDnsRecord.getId());
		
		return dnsRecord;
	}
	
	private static Anchor mergeAnchor(Anchor existingAnchor, Anchor anchor)
	{
		anchor.setOwner(ObjectUtils.defaultIfNull(anchor.getOwner(), existingAnchor.getOwner()));
		
		return anchor;
	}
	
	private static Domain mergeDomain(Domain existingDomain, Domain domain)
	{
		domain.setId(existingDomain.getId());
		
		domain.setAddress(ObjectUtils.defaultIfNull(domain.getAddress(), existingDomain.getAddress()));
		domain.setDomainName(ObjectUtils.defaultIfNull(domain.getDomainName(), existingDomain.getDomainName()));
		domain.setPostmasterAddressId(ObjectUtils.defaultIfNull(domain.getPostmasterAddressId(), existingDomain.getPostmasterAddressId()));
		domain.setPostMasterEmail(ObjectUtils.defaultIfNull(domain.getPostMasterEmail(), existingDomain.getPostMasterEmail()));
		domain.setStatus(ObjectUtils.defaultIfNull(domain.getStatus(), existingDomain.getStatus()));
		
		return domain;
	}
	
	private static String dnsRecordsToString(DnsRecord ... dnsRecords)
	{
		return dnsRecordsToString(Arrays.asList(dnsRecords));
	}
	
	private static String dnsRecordsToString(Collection<DnsRecord> dnsRecords)
	{
		StringBuilder builder = new StringBuilder();
		
		for (DnsRecord dnsRecord : dnsRecords)
		{
			if (builder.length() > 0)
			{
				builder.append(", ");
			}
			
			builder.append("{type=");
			builder.append(Type.string(dnsRecord.getType()));
			builder.append(", name=");
			builder.append(dnsRecord.getName());
			builder.append(", ttl=");
			builder.append(dnsRecord.getTtl());
			
			// TODO: add DNS record type-specific data
			
			builder.append("}");
		}
		
		return builder.toString();
	}
	
	private static String certificatesToString(Certificate ... certs)
	{
		return certificatesToString(Arrays.asList(certs));
	}
	
	private static String certificatesToString(Collection<Certificate> certs)
	{
		StringBuilder builder = new StringBuilder();
		Calendar certValidStartDate, certValidEndDate;
		
		for (Certificate cert : certs)
		{
			if (builder.length() > 0)
			{
				builder.append(", ");
			}
			
			builder.append("{owner=");
			builder.append(cert.getOwner());
			builder.append(", privateKey=");
			builder.append(cert.isPrivateKey());
			builder.append(", status=");
			builder.append(cert.getStatus().getValue());
			
			if ((certValidStartDate = cert.getValidStartDate()) != null)
			{
				builder.append(", validStart=");
				builder.append(certValidStartDate);
			}
			
			if ((certValidEndDate = cert.getValidEndDate()) != null)
			{
				builder.append(", validEnd=");
				builder.append(certValidEndDate);
			}
			
			builder.append("}");
		}
		
		return builder.toString();
	}
	
	private static String settingsToString(Setting ... settings)
	{
		return settingsToString(Arrays.asList(settings));
	}
	
	private static String settingsToString(Collection<Setting> settings)
	{
		StringBuilder builder = new StringBuilder();
		
		for (Setting setting : settings)
		{
			if (builder.length() > 0)
			{
				builder.append(", ");
			}
			
			builder.append("{name=");
			builder.append(setting.getName());
			builder.append(", value=");
			builder.append(setting.getValue());
			builder.append(", status=");
			builder.append(setting.getStatus().getValue());
			builder.append("}");
		}
		
		return builder.toString();
	}
	
	private static String anchorsToString(Anchor ... anchors)
	{
		return anchorsToString(Arrays.asList(anchors));
	}
	
	private static String anchorsToString(Collection<Anchor> anchors)
	{
		StringBuilder builder = new StringBuilder();
		String anchorThumbprint;
		
		for (Anchor anchor : anchors)
		{
			if (builder.length() > 0)
			{
				builder.append(", ");
			}
			
			builder.append("{owner=");
			builder.append(anchor.getOwner());
			
			if (!StringUtils.isBlank((anchorThumbprint = anchor.getThumbprint())))
			{
				builder.append(", thumbprint=");
				builder.append(anchorThumbprint);
			}
			
			builder.append(", incoming=");
			builder.append(anchor.isIncoming());
			builder.append(", outgoing=");
			builder.append(anchor.isOutgoing());
			builder.append(", status=");
			builder.append(anchor.getStatus().getValue());
			builder.append("}");
		}
		
		return builder.toString();
	}
	
	private static String domainsToString(Domain ... domains)
	{
		return domainsToString(Arrays.asList(domains));
	}
	
	private static String domainsToString(Collection<Domain> domains)
	{
		StringBuilder builder = new StringBuilder();
		String domainPostmasterEmail;
		Address[] domainAddresses;
		
		for (Domain domain : domains)
		{
			if (builder.length() > 0)
			{
				builder.append(", ");
			}
			
			builder.append("{name=");
			builder.append(domain.getDomainName());
			
			if (!StringUtils.isBlank((domainPostmasterEmail = domain.getPostMasterEmail())))
			{
				builder.append(", postmasterEmail=");
				builder.append(domainPostmasterEmail);
			}
			if (!ArrayUtils.isEmpty((domainAddresses = domain.getAddress())))
			{
				builder.append("addresses=[");
				builder.append(addressesToString(domainAddresses));
				builder.append("]");
			}
			
			builder.append(", status=");
			builder.append(domain.getStatus().getValue());
			builder.append("}");
		}
		
		return builder.toString();
	}
	
	private static String addressesToString(Address[] addresses)
	{
		StringBuilder builder = new StringBuilder();
		
		for (Address address : addresses)
		{
			if (builder.length() > 0)
			{
				builder.append(", ");
			}
			
			builder.append(address.getEmailAddress());
		}
		
		return builder.toString();
	}

	public URL getServiceUrl()
	{
		return this.serviceUrl;
	}
}