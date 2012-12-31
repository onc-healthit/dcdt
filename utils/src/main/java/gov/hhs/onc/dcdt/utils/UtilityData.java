package gov.hhs.onc.dcdt.utils;

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
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.beanutils.BeanHelper;
import org.apache.commons.configuration.beanutils.XMLBeanDeclaration;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class UtilityData
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
	
	private Utility util;
	
	public UtilityData(Utility util)
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
		
		for (String beanId : this.util.getConfig().getStringArray(getBeanIdXpath(beanClass, attribs)))
		{
			beans.add(beanClass.cast(BeanHelper.createBean(new XMLBeanDeclaration(this.util.getConfig(),
				getBeanXpath(beanClass, new BeanAttrib(BEAN_ID_ATTRIB_KEY, beanId))))));
		}
		
		return beans;
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
	
	private static <T extends UtilityBean> String getBeanIdXpath(Class<T> beanClass, BeanAttrib ... attribs)
	{
		return getBeanXpath(beanClass, attribs) + XPATH_DELIM + XPATH_ATTRIB_KEY_PREFIX + BEAN_ID_ATTRIB_KEY;
	}
}