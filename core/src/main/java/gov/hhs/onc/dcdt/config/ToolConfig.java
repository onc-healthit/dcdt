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
import gov.hhs.onc.dcdt.reflect.resources.ResourcePathUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import org.xml.sax.SAXParseException;

public class ToolConfig
{
	public final static String BEAN_ID_ATTRIB_KEY = "id";
	
	protected final static String CONFIG_FILE_RESOURCE_NAME = "config.xml";
	protected final static String CONFIG_PARSE_ERROR_SOURCE_DELIM = ":";
	
	protected final static String TOOL_LOOKUP_PREFIX = "dcdt";
	
	protected final static List<BeanFactory> BEAN_FACTORIES = Arrays.asList(new BeanFactory[]
		{
		});
	
	protected String moduleName;
	protected ToolConfigEntityResolver configEntityResolver;
	protected DefaultConfigurationBuilder configBuilder;
	protected CombinedConfiguration config;
	protected ToolConfigListener configListener;
	
	public ToolConfig()
	{
		this(StringUtils.EMPTY);
	}
	
	public ToolConfig(String moduleName)
	{
		this.moduleName = moduleName;
	}
	
	public static CombinedConfiguration getChildConfig(CombinedConfiguration configSection, String childConfigName)
	{
		return getChildConfig(CombinedConfiguration.class, configSection, childConfigName);
	}
	
	public static PropertiesConfiguration getChildPropsConfig(CombinedConfiguration configSection, String childConfigName)
	{
		return getChildConfig(PropertiesConfiguration.class, configSection, childConfigName);
	}
	
	public static XMLConfiguration getChildXmlConfig(CombinedConfiguration configSection, String childConfigName)
	{
		return getChildConfig(XMLConfiguration.class, configSection, childConfigName);
	}
	
	public static CombinedConfiguration getAdditionalConfigSection(CombinedConfiguration config)
	{
		return getConfigSection(config, DefaultConfigurationBuilder.ADDITIONAL_NAME);
	}
	
	public static CombinedConfiguration getOverrideConfigSection(CombinedConfiguration config)
	{
		return config;
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
	
	public PropertiesConfiguration getChildPropsConfig(String childConfigName)
	{
		return getChildPropsConfig(getOverrideConfigSection(this.config), childConfigName);
	}
	
	public XMLConfiguration getChildXmlConfig(String childConfigName)
	{
		return getChildXmlConfig(getOverrideConfigSection(this.config), childConfigName);
	}
	
	public CombinedConfiguration getAdditionalConfigSection()
	{
		return getAdditionalConfigSection(this.config);
	}
	
	public CombinedConfiguration getOverrideConfigSection()
	{
		return getOverrideConfigSection(this.config);
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
		}
		catch (ConfigurationException | ConfigurationRuntimeException e)
		{
			Throwable rootConfigCause = getRootConfigurationCause(e);
			
			if (rootConfigCause instanceof SAXParseException)
			{
				SAXParseException parseConfigCause = (SAXParseException)rootConfigCause;
				
				throw new ToolConfigException("Invalid configuration: source=" + StringUtils.join(new Object[]{
					parseConfigCause.getSystemId(),
					parseConfigCause.getLineNumber(), parseConfigCause.getColumnNumber()
				}, CONFIG_PARSE_ERROR_SOURCE_DELIM) + ", error=" + 
					parseConfigCause.getMessage());
			}
			else
			{
				throw new ToolConfigException("Unable to initialize configuration.", e);
			}
		}
	}
	
	protected static <T extends ToolBean> String getBeanXpath(Class<T> beanClass, BeanAttrib ... attribs)
	{
		XpathBuilder xpathBuilder = new XpathBuilder();
		ConfigBean configBeanAnno = (ConfigBean)beanClass.getAnnotation(ConfigBean.class);
		
		if (configBeanAnno != null)
		{
			xpathBuilder.appendNodes(configBeanAnno.value());
		}
		
		xpathBuilder.appendAttribs(attribs);
		
		return xpathBuilder.toString();
	}
	
	protected static Throwable getRootConfigurationCause(Throwable throwable)
	{
		return (!(throwable instanceof ConfigurationException) && !(throwable instanceof ConfigurationRuntimeException)) || 
			(throwable.getCause() == null) || (throwable.getCause() == throwable) ? throwable : 
			getRootConfigurationCause(throwable.getCause());
	}
	
	protected static CombinedConfiguration getConfigSection(CombinedConfiguration config, String sectionName)
	{
		return (CombinedConfiguration)config.getConfiguration(sectionName);
	}
	
	protected static <T extends Configuration> T getChildConfig(Class<T> childConfigClass, CombinedConfiguration configSection, String childConfigName)
	{
		return childConfigClass.cast(configSection.getConfiguration(childConfigName));
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

	public String getModuleName()
	{
		return this.moduleName;
	}
}