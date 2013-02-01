package gov.hhs.onc.dcdt.config;

import gov.hhs.onc.dcdt.annotations.ConfigBean;
import gov.hhs.onc.dcdt.beans.BeanAttrib;
import gov.hhs.onc.dcdt.beans.Domain;
import gov.hhs.onc.dcdt.beans.LdapService;
import gov.hhs.onc.dcdt.beans.Setting;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.dns.ARecord;
import gov.hhs.onc.dcdt.beans.dns.CnameRecord;
import gov.hhs.onc.dcdt.beans.dns.MxRecord;
import gov.hhs.onc.dcdt.beans.dns.NsRecord;
import gov.hhs.onc.dcdt.beans.dns.SoaRecord;
import gov.hhs.onc.dcdt.beans.dns.SrvRecord;
import gov.hhs.onc.dcdt.beans.entry.Entry;
import gov.hhs.onc.dcdt.beans.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.beans.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.reflect.resources.ResourcePathUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationRuntimeException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.beanutils.BeanFactory;
import org.apache.commons.configuration.beanutils.BeanHelper;
import org.apache.commons.configuration.beanutils.XMLBeanDeclaration;
import org.apache.commons.configuration.interpol.ConfigurationInterpolator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;

public class ToolConfig
{
	public final static String PARENT_MODULE_NAME = "parent";
	public final static String ALL_MODULE_NAME = "all";
	public final static String CORE_MODULE_NAME = "core";
	
	public final static String BEAN_ID_ATTRIB_KEY = "id";
	
	protected final static String MODULE_CONFIG_NAME_PREFIX = "config-";
	
	protected final static String CONFIG_FILE_RESOURCE_NAME = "config.xml";
	protected final static String CONFIG_PARSE_ERROR_SOURCE_DELIM = ":";
	
	protected final static String TOOL_LOOKUP_PREFIX = "dcdt";
	
	protected final static List<BeanFactory> BEAN_FACTORIES = Arrays.asList(new BeanFactory[]
		{
		});
	
	private final static Logger LOGGER = Logger.getLogger(ToolConfig.class);
	
	protected String moduleName;
	protected ToolConfigEntityResolver configEntityResolver;
	protected DefaultConfigurationBuilder configBuilder;
	protected CombinedConfiguration config;
	protected ToolConfigListener configListener;
	protected Map<String, ToolVersion> moduleVersions = new LinkedHashMap<>();
	
	public ToolConfig()
	{
		this(StringUtils.EMPTY);
	}
	
	public ToolConfig(String moduleName)
	{
		this.moduleName = moduleName;
	}
	
	public static PropertiesConfiguration getChildPropsConfig(CombinedConfiguration parentConfig, String childConfigName)
	{
		return getChildConfig(parentConfig, childConfigName, PropertiesConfiguration.class);
	}
	
	public static XMLConfiguration getChildXmlConfig(CombinedConfiguration parentConfig, String childConfigName)
	{
		return getChildConfig(parentConfig, childConfigName, XMLConfiguration.class);
	}
	
	public static <T extends Configuration> T getChildConfig(CombinedConfiguration parentConfig, String childConfigName, 
		Class<T> childConfigClass)
	{
		return getChildConfig(parentConfig, childConfigName, childConfigClass, -1);
	}
	
	public static <T extends Configuration> T getChildConfig(CombinedConfiguration parentConfig, String childConfigName, 
		Class<T> childConfigClass, int maxDepth)
	{
		return getChildConfig(parentConfig, childConfigName, childConfigClass, maxDepth, 0);
	}
	
	public static boolean hasAdditionalConfig(CombinedConfiguration parentConfig)
	{
		return parentConfig.getConfigurationNames().contains(DefaultConfigurationBuilder.ADDITIONAL_NAME);
	}
	
	public static CombinedConfiguration getAdditionalConfig(CombinedConfiguration parentConfig)
	{
		return (CombinedConfiguration)parentConfig.getConfiguration(DefaultConfigurationBuilder.ADDITIONAL_NAME);
	}
	
	//<editor-fold desc="typed bean getters">
	public List<Domain> getDomains(BeanAttrib... attribs)
	{
		return this.getBeans(Domain.class, attribs);
	}
	
	public Domain getDomain(BeanAttrib ... attribs)
	{
		return this.getBean(Domain.class, attribs);
	}
	
	public List<Entry> getEntries(BeanAttrib ... attribs)
	{
		return this.getBeans(Entry.class, attribs);
	}
	
	public Entry getEntry(BeanAttrib ... attribs)
	{
		return this.getBean(Entry.class, attribs);
	}
	
	public List<LdapService> getLdapServices(BeanAttrib ... attribs)
	{
		return this.getBeans(LdapService.class, attribs);
	}
	
	public LdapService getLdapService(BeanAttrib ... attribs)
	{
		return this.getBean(LdapService.class, attribs);
	}
	
	public List<Setting> getSettings(BeanAttrib ... attribs)
	{
		return this.getBeans(Setting.class, attribs);
	}
	
	public Setting getSetting(BeanAttrib ... attribs)
	{
		return this.getBean(Setting.class, attribs);
	}
	
	public List<ARecord> getARecords(BeanAttrib ... attribs)
	{
		return this.getBeans(ARecord.class, attribs);
	}
	
	public ARecord getARecord(BeanAttrib ... attribs)
	{
		return this.getBean(ARecord.class, attribs);
	}
	
	public List<CnameRecord> getCnameRecords(BeanAttrib ... attribs)
	{
		return this.getBeans(CnameRecord.class, attribs);
	}
	
	public CnameRecord getCnameRecord(BeanAttrib ... attribs)
	{
		return this.getBean(CnameRecord.class, attribs);
	}
	
	public List<MxRecord> getMxRecords(BeanAttrib ... attribs)
	{
		return this.getBeans(MxRecord.class, attribs);
	}
	
	public MxRecord getMxRecord(BeanAttrib ... attribs)
	{
		return this.getBean(MxRecord.class, attribs);
	}
	
	public List<NsRecord> getNsRecords(BeanAttrib ... attribs)
	{
		return this.getBeans(NsRecord.class, attribs);
	}
	
	public NsRecord getNsRecord(BeanAttrib ... attribs)
	{
		return this.getBean(NsRecord.class, attribs);
	}
	
	public List<SoaRecord> getSoaRecords(BeanAttrib ... attribs)
	{
		return this.getBeans(SoaRecord.class, attribs);
	}
	
	public SoaRecord getSoaRecord(BeanAttrib ... attribs)
	{
		return this.getBean(SoaRecord.class, attribs);
	}
	
	public List<SrvRecord> getSrvRecords(BeanAttrib ... attribs)
	{
		return this.getBeans(SrvRecord.class, attribs);
	}
	
	public SrvRecord getSrvRecord(BeanAttrib ... attribs)
	{
		return this.getBean(SrvRecord.class, attribs);
	}
	
	public List<HostingTestcase> getHostingTestcases(BeanAttrib ... attribs)
	{
		return this.getBeans(HostingTestcase.class, attribs);
	}
	
	public HostingTestcase getHostingTestcase(BeanAttrib ... attribs)
	{
		return this.getBean(HostingTestcase.class, attribs);
	}
	
	public List<DiscoveryTestcase> getDiscoveryTestcases(BeanAttrib ... attribs)
	{
		return this.getBeans(DiscoveryTestcase.class, attribs);
	}
	
	public DiscoveryTestcase getDiscoveryTestcase(BeanAttrib ... attribs)
	{
		return this.getBean(DiscoveryTestcase.class, attribs);
	}
	//</editor-fold>
	
	public <T extends ToolBean> T getBeanById(Class<T> beanClass, String id)
	{
		return this.getBean(beanClass, new BeanAttrib(BEAN_ID_ATTRIB_KEY, id));
	}
	
	public <T extends ToolBean> T getBean(Class<T> beanClass, BeanAttrib ... attribs)
	{
		List<T> beans = this.getBeans(beanClass, attribs);
		
		return !beans.isEmpty() ? beans.get(0) : null;
	}
	
	public <T extends ToolBean> List<T> getBeans(Class<T> beanClass, BeanAttrib ... attribs)
	{
		List<T> beans = new ArrayList<>();
		
		for (HierarchicalConfiguration beanConfig : this.config.configurationsAt(getBeanXpath(beanClass, attribs)))
		{
			beans.add(beanClass.cast(BeanHelper.createBean(new XMLBeanDeclaration(beanConfig))));
		}
		
		return beans;
	}

	public CombinedConfiguration getModuleParentConfig()
	{
		return this.getChildConfig(MODULE_CONFIG_NAME_PREFIX + this.moduleName, CombinedConfiguration.class);
	}
	
	public PropertiesConfiguration getChildPropsConfig(String childConfigName)
	{
		return this.getChildConfig(childConfigName, PropertiesConfiguration.class);
	}
	
	public XMLConfiguration getChildXmlConfig(String childConfigName)
	{
		return this.getChildConfig(childConfigName, XMLConfiguration.class);
	}
	
	public <T extends Configuration> T getChildConfig(String childConfigName, Class<T> childConfigClass)
	{
		return this.getChildConfig(childConfigName, childConfigClass, -1);
	}
	
	public <T extends Configuration> T getChildConfig(String childConfigName, Class<T> childConfigClass, int maxDepth)
	{
		return getChildConfig(this.config, childConfigName, childConfigClass, maxDepth);
	}
	
	public boolean hasAdditionalConfig()
	{
		return hasAdditionalConfig(this.config);
	}
	
	public CombinedConfiguration getAdditionalConfig()
	{
		return getAdditionalConfig(this.config);
	}
	
	public ToolVersion getModuleVersion()
	{
		return this.moduleVersions.get(this.moduleName);
	}
	
	public void initConfig() throws ToolConfigException
	{
		try
		{
			for (BeanFactory beanFactory : BEAN_FACTORIES)
			{
				BeanHelper.registerBeanFactory(beanFactory.getClass().getName(), beanFactory);
			}
			
			this.configListener = new ToolConfigListener();
			this.configEntityResolver = new ToolConfigEntityResolver();
			
			this.configBuilder = new DefaultConfigurationBuilder(ResourcePathUtils.buildPath(
				true, true, ArrayUtils.toArray(CONFIG_FILE_RESOURCE_NAME)));
			this.configBuilder.addConfigurationListener(this.configListener);
			this.configBuilder.addErrorListener(this.configListener);
			ConfigurationUtils.enableRuntimeExceptions(this.configBuilder);
			
			ConfigurationInterpolator.registerGlobalLookup(TOOL_LOOKUP_PREFIX, new ToolConfigStrLookup(this));
			
			this.config = this.configBuilder.getConfiguration(true);
			this.config.addConfigurationListener(this.configListener);
			this.config.addErrorListener(this.configListener);
			ConfigurationUtils.enableRuntimeExceptions(this.config);
			
			this.initModuleVersions();
		}
		catch (ConfigurationException | ConfigurationRuntimeException e)
		{
			int parseCauseIndex = ExceptionUtils.indexOfType(e, SAXParseException.class);
			
			if (parseCauseIndex == -1)
			{
				throw new ToolConfigException("Unable to initialize configuration.", e);
			}
			else
			{
				SAXParseException parseCause = (SAXParseException)ExceptionUtils.getThrowables(e)[parseCauseIndex];
				
				throw new ToolConfigException("Invalid configuration: source=" + StringUtils.join(new Object[]{ 
					parseCause.getSystemId(), parseCause.getLineNumber(), parseCause.getColumnNumber() }, 
					CONFIG_PARSE_ERROR_SOURCE_DELIM)  + ", error=" + parseCause.getMessage());
			}
		}
	}
	
	protected static <T extends Configuration> T getChildConfig(CombinedConfiguration parentConfig, String childConfigName, 
		Class<T> childConfigClass, int maxDepth, int depth)
	{
		if ((parentConfig == null) || ((maxDepth >= 0) && (++depth >= maxDepth)))
		{
			return null;
		}
		
		Configuration config;
		Class<? extends Configuration> configClass;
		T childConfig;
		
		for (String configName : parentConfig.getConfigurationNames())
		{
			config = parentConfig.getConfiguration(configName);
			configClass = config.getClass();
			
			if (configName.equals(childConfigName) && childConfigClass.isAssignableFrom(configClass))
			{
				return childConfigClass.cast(config);
			}
			else if (CombinedConfiguration.class.isAssignableFrom(configClass) && 
				!configName.equals(DefaultConfigurationBuilder.ADDITIONAL_NAME))
			{
				if ((childConfig = getChildConfig((CombinedConfiguration)config, childConfigName, childConfigClass, maxDepth, depth)) != null)
				{
					return childConfig;
				}
			}
		}
		
		return getChildConfig(getAdditionalConfig(parentConfig), childConfigName, childConfigClass, maxDepth, depth);
	}
	
	protected void initModuleVersions()
	{
		this.moduleVersions.put(PARENT_MODULE_NAME, new ToolVersion(this, PARENT_MODULE_NAME));
		this.moduleVersions.put(ALL_MODULE_NAME, new ToolVersion(this, ALL_MODULE_NAME));
		this.moduleVersions.put(CORE_MODULE_NAME, new ToolVersion(this, CORE_MODULE_NAME));
		
		if (!StringUtils.isBlank(this.moduleName))
		{
			this.moduleVersions.put(this.moduleName, new ToolVersion(this, this.moduleName));
		}
	}
	
	protected static <T extends ToolBean> String getBeanXpath(Class<T> beanClass, BeanAttrib ... attribs)
	{
		XpathBuilder xpathBuilder = new XpathBuilder();
		ConfigBean configBeanAnno = beanClass.getAnnotation(ConfigBean.class);
		
		if (configBeanAnno != null)
		{
			xpathBuilder.appendNodes(configBeanAnno.value());
		}
		
		xpathBuilder.appendAttribs(attribs);
		
		String xpath = xpathBuilder.toString();
		
		LOGGER.trace("Bean (class=" + beanClass.getName() + ") XPath: " + xpath);
		
		return xpath;
	}
	
	public CombinedConfiguration getConfig()
	{
		return this.config;
	}

	public DefaultConfigurationBuilder getConfigBuilder()
	{
		return this.configBuilder;
	}

	public ToolConfigEntityResolver getConfigEntityResolver()
	{
		return this.configEntityResolver;
	}

	public ToolConfigListener getConfigListener()
	{
		return this.configListener;
	}

	public boolean hasModuleName()
	{
		return !StringUtils.isBlank(this.moduleName);
	}
	
	public String getModuleName()
	{
		return this.moduleName;
	}

	public Map<String, ToolVersion> getModuleVersions()
	{
		return this.moduleVersions;
	}
}