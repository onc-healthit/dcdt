package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.utils.annotations.ConfigBean;
import org.apache.commons.configuration.beanutils.BeanHelper;
import org.apache.commons.configuration.beanutils.XMLBeanDeclaration;
import org.apache.commons.lang3.StringUtils;

public class UtilityBeans
{
	private final static String XPATH_ATTRIB_DELIM = ", ";
	private final static String XPATH_ATTRIB_EQUALS = "=";
	private final static String XPATH_ATTRIB_KEY_PREFIX = "@";
	private final static String XPATH_SEARCH_PREFIX = "[";
	private final static String XPATH_SEARCH_SUFFIX = "]";
	
	private Utility utility;
	
	public UtilityBeans(Utility utility)
	{
		this.utility = utility;
	}
	
	public <T> T createBean(Class<T> beanClass, BeanAttrib ... attribs)
	{
		StringBuilder attribsBuilder = new StringBuilder();
		
		for (BeanAttrib attrib : attribs)
		{
			if (attribsBuilder.length() > 0)
			{
				attribsBuilder.append(XPATH_ATTRIB_DELIM);
			}
			
			attribsBuilder.append(XPATH_ATTRIB_KEY_PREFIX);
			attribsBuilder.append(attrib.getKey());
			attribsBuilder.append(XPATH_ATTRIB_EQUALS);
			attribsBuilder.append('"');
			attribsBuilder.append(attrib.getValue());
			attribsBuilder.append('"');
		}
		
		return this.createBean(beanClass, XPATH_SEARCH_PREFIX + attribsBuilder.toString() + XPATH_SEARCH_SUFFIX);
	}
	
	public <T> T createBean(Class<T> beanClass, int index)
	{
		return this.createBean(beanClass, XPATH_SEARCH_PREFIX + index + XPATH_SEARCH_SUFFIX);
	}
	
	public <T> T createBean(Class<T> beanClass, String xpath)
	{
		ConfigBean configBeanAnno = (ConfigBean)beanClass.getAnnotation(ConfigBean.class);
		String configBeanValue;
		
		if ((configBeanAnno != null) && !StringUtils.isBlank(configBeanValue = configBeanAnno.value()))
		{
			xpath = configBeanValue + xpath;
		}

		return beanClass.cast(BeanHelper.createBean(new XMLBeanDeclaration(this.utility.getConfig(), xpath)));
	}
}