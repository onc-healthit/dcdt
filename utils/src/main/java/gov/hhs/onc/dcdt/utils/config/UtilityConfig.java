package gov.hhs.onc.dcdt.utils.config;

import gov.hhs.onc.dcdt.utils.Utility;
import gov.hhs.onc.dcdt.utils.annotations.ConfigBean;
import gov.hhs.onc.dcdt.utils.beans.BeanAttrib;
import gov.hhs.onc.dcdt.utils.beans.Domain;
import gov.hhs.onc.dcdt.utils.beans.Entry;
import gov.hhs.onc.dcdt.utils.beans.LdapService;
import gov.hhs.onc.dcdt.utils.beans.Setting;
import gov.hhs.onc.dcdt.utils.beans.UtilityBean;
import gov.hhs.onc.dcdt.utils.beans.dns.ARecord;
import gov.hhs.onc.dcdt.utils.beans.dns.CnameRecord;
import gov.hhs.onc.dcdt.utils.beans.dns.MxRecord;
import gov.hhs.onc.dcdt.utils.beans.dns.NsRecord;
import gov.hhs.onc.dcdt.utils.beans.dns.SoaRecord;
import gov.hhs.onc.dcdt.utils.beans.dns.SrvRecord;
import gov.hhs.onc.dcdt.utils.cli.CliOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationRuntimeException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.beanutils.BeanFactory;
import org.apache.commons.configuration.beanutils.BeanHelper;
import org.apache.commons.configuration.beanutils.XMLBeanDeclaration;
import org.apache.commons.configuration.interpol.ConfigurationInterpolator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXParseException;

public class UtilityConfig<T extends Enum<T> & CliOption>
{
	public final static String XPATH_DELIM = "/";
	public final static String XPATH_ATTRIB_DELIM = ", ";
	public final static String XPATH_ATTRIB_EQUALS = "=";
	public final static String XPATH_ATTRIB_NOT_EQUALS = "!=";
	public final static String XPATH_ATTRIB_KEY_PREFIX = "@";
	public final static String XPATH_ATTRIB_VALUE_QUOTE = "\"";
	public final static String XPATH_SEARCH_PREFIX = "[";
	public final static String XPATH_SEARCH_SUFFIX = "]";
	
	public final static String BEAN_ID_ATTRIB_KEY = "id";
	
	private final static String CONFIG_FILE_PATH = "config.xml";
	private final static String CONFIG_PARSE_ERROR_SOURCE_DELIM = ":";
	
	private final static String UTIL_LOOKUP_PREFIX = "util";
	
	private final static List<BeanFactory> BEAN_FACTORIES = Arrays.asList(new BeanFactory[]
		{
		});
	
	private Utility<T> util;
	private UtilityConfigEntityResolver configEntityResolver;
	private DefaultConfigurationBuilder configBuilder;
	private CombinedConfiguration config;
	private UtilityConfigListener configListener;
	
	public UtilityConfig(Utility<T> util)
	{
		this.util = util;
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
	//</editor-fold>
	
	public <T extends UtilityBean> T getBeanById(Class<T> beanClass, String id)
	{
		return this.getBean(beanClass, new BeanAttrib(BEAN_ID_ATTRIB_KEY, id));
	}
	
	public <T extends UtilityBean> T getBean(Class<T> beanClass, BeanAttrib ... attribs)
	{
		List<T> beans = this.getBeans(beanClass, attribs);
		
		return !beans.isEmpty() ? beans.get(0) : null;
	}
	
	public <T extends UtilityBean> List<T> getBeans(Class<T> beanClass, BeanAttrib ... attribs)
	{
		List<T> beans = new ArrayList<>();
		
		for (HierarchicalConfiguration beanConfig : this.util.getConfig().getConfig().configurationsAt(getBeanXpath(beanClass, attribs)))
		{
			beans.add(beanClass.cast(BeanHelper.createBean(new XMLBeanDeclaration(beanConfig))));
		}
		
		return beans;
	}
	
	public String getUtilString(T option)
	{
		return this.getUtilString(option, null);
	}
	
	public String getUtilString(T option, Object defaultValue)
	{
		return ObjectUtils.toString(this.util.getCli().getOptionValue(option), this.getUtilString(option.getAttribName(), 
			defaultValue));
	}
	
	public String getUtilString(String key)
	{
		return this.getUtilString(key, null);
	}
	
	public String getUtilString(String key, Object defaultValue)
	{
		return this.getUtilConfig().getString(XPATH_ATTRIB_KEY_PREFIX + key, ObjectUtils.toString(defaultValue, null));
	}
	
	public void setUtilString(T option)
	{
		this.setUtilString(option, null);
	}
	
	public void setUtilString(T option, Object defaultValue)
	{
		this.setUtilString(option.getAttribName(), this.getUtilString(option), defaultValue);
	}
	
	public void setUtilString(String key, Object value)
	{
		this.setUtilString(key, value, null);
	}
	
	public void setUtilString(String key, Object value, Object defaultValue)
	{
		this.getUtilConfig().setProperty(XPATH_ATTRIB_KEY_PREFIX + key, ObjectUtils.defaultIfNull(value, defaultValue));
	}
	
	public SubnodeConfiguration getUtilConfig()
	{
		return this.config.configurationAt(this.util.getName());
	}
	
	public void initConfig() throws UtilityConfigException
	{
		try
		{
			for (BeanFactory beanFactory : BEAN_FACTORIES)
			{
				BeanHelper.registerBeanFactory(beanFactory.getClass().getName(), beanFactory);
			}
			
			this.configListener = new UtilityConfigListener(this.util);
			
			this.configEntityResolver = new UtilityConfigEntityResolver();
			
			this.configBuilder = new DefaultConfigurationBuilder(CONFIG_FILE_PATH);
			this.configBuilder.addConfigurationListener(this.configListener);
			this.configBuilder.addErrorListener(this.configListener);
			this.configBuilder.setEntityResolver(this.configEntityResolver);
			ConfigurationUtils.enableRuntimeExceptions(this.configBuilder);
			
			ConfigurationInterpolator.registerGlobalLookup(UTIL_LOOKUP_PREFIX, new UtilityConfigStrLookup(this.util));
			
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
				
				throw new UtilityConfigException("Invalid configuration: source=" + StringUtils.join(new Object[]{
					parseConfigCause.getSystemId(),
					parseConfigCause.getLineNumber(), parseConfigCause.getColumnNumber()
				}, CONFIG_PARSE_ERROR_SOURCE_DELIM) + ", error=" + 
					parseConfigCause.getMessage());
			}
			else
			{
				throw new UtilityConfigException("Unable to initialize utility (name=" + this.util.getName() + ") configuration.", e);
			}
		}
	}
	
	private static <T extends UtilityBean> String getBeanXpath(Class<T> beanClass, BeanAttrib ... attribs)
	{
		StringBuilder xpathBuilder = new StringBuilder();
		ConfigBean configBeanAnno = (ConfigBean)beanClass.getAnnotation(ConfigBean.class);
		String configBeanValue;
		
		if ((configBeanAnno != null) && !StringUtils.isBlank(configBeanValue = configBeanAnno.value()))
		{
			xpathBuilder.append(configBeanValue);
		}
		
		if (!ArrayUtils.isEmpty(attribs))
		{
			xpathBuilder.append(XPATH_SEARCH_PREFIX);
			
			BeanAttrib attrib;
			
			for (int a = 0; a < attribs.length; a++)
			{
				if (a > 0)
				{
					xpathBuilder.append(XPATH_ATTRIB_DELIM);
				}
				
				attrib = attribs[a];
				
				xpathBuilder.append(XPATH_ATTRIB_KEY_PREFIX);
				xpathBuilder.append(attrib.getKey());
				xpathBuilder.append(attrib.isInverse() ? XPATH_ATTRIB_NOT_EQUALS : XPATH_ATTRIB_EQUALS);
				xpathBuilder.append(XPATH_ATTRIB_VALUE_QUOTE);
				xpathBuilder.append(attrib.getValue());
				xpathBuilder.append(XPATH_ATTRIB_VALUE_QUOTE);
			}
			
			xpathBuilder.append(XPATH_SEARCH_SUFFIX);
		}
		
		return xpathBuilder.toString();
	}
	
	private static Throwable getRootConfigurationCause(Throwable throwable)
	{
		return (!(throwable instanceof ConfigurationException) && !(throwable instanceof ConfigurationRuntimeException)) || 
			(throwable.getCause() == null) || (throwable.getCause() == throwable) ? throwable : 
			getRootConfigurationCause(throwable.getCause());
	}
	
	public CombinedConfiguration getConfig()
	{
		return this.config;
	}

	public DefaultConfigurationBuilder getConfigBuilder()
	{
		return this.configBuilder;
	}

	public UtilityConfigEntityResolver getConfigEntityResolver()
	{
		return this.configEntityResolver;
	}

	public UtilityConfigListener getConfigListener()
	{
		return this.configListener;
	}
}