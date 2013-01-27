package gov.hhs.onc.dcdt.config;

import gov.hhs.onc.dcdt.annotations.ConfigBean;
import gov.hhs.onc.dcdt.beans.BeanAttrib;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.reflect.resources.ResourcePathUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationRuntimeException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.beanutils.BeanFactory;
import org.apache.commons.configuration.beanutils.BeanHelper;
import org.apache.commons.configuration.beanutils.XMLBeanDeclaration;
import org.apache.commons.configuration.interpol.ConfigurationInterpolator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXParseException;

public class ToolConfig
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
	
	protected final static String CONFIG_FILE_RESOURCE_NAME = "config.xml";
	protected final static String CONFIG_PARSE_ERROR_SOURCE_DELIM = ":";
	
	protected final static String TOOL_LOOKUP_PREFIX = "dcdt";
	
	protected final static List<BeanFactory> BEAN_FACTORIES = Arrays.asList(new BeanFactory[]
		{
		});
	
	protected ToolConfigEntityResolver configEntityResolver;
	protected DefaultConfigurationBuilder configBuilder;
	protected CombinedConfiguration config;
	protected ToolConfigListener configListener;
	
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
	
	protected static Throwable getRootConfigurationCause(Throwable throwable)
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

	public ToolConfigEntityResolver getConfigEntityResolver()
	{
		return this.configEntityResolver;
	}

	public ToolConfigListener getConfigListener()
	{
		return this.configListener;
	}
}