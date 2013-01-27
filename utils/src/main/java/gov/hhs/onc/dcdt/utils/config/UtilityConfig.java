package gov.hhs.onc.dcdt.utils.config;

import gov.hhs.onc.dcdt.beans.BeanAttrib;
import gov.hhs.onc.dcdt.config.ToolConfig;
import gov.hhs.onc.dcdt.config.ToolConfigException;
import gov.hhs.onc.dcdt.utils.Utility;
import gov.hhs.onc.dcdt.utils.beans.Domain;
import gov.hhs.onc.dcdt.utils.beans.Entry;
import gov.hhs.onc.dcdt.utils.beans.LdapService;
import gov.hhs.onc.dcdt.utils.beans.Setting;
import gov.hhs.onc.dcdt.utils.beans.dns.ARecord;
import gov.hhs.onc.dcdt.utils.beans.dns.CnameRecord;
import gov.hhs.onc.dcdt.utils.beans.dns.MxRecord;
import gov.hhs.onc.dcdt.utils.beans.dns.NsRecord;
import gov.hhs.onc.dcdt.utils.beans.dns.SoaRecord;
import gov.hhs.onc.dcdt.utils.beans.dns.SrvRecord;
import gov.hhs.onc.dcdt.utils.cli.CliOption;
import java.util.List;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.interpol.ConfigurationInterpolator;
import org.apache.commons.lang3.ObjectUtils;

public class UtilityConfig<T extends Enum<T> & CliOption> extends ToolConfig
{
	private final static String UTIL_LOOKUP_PREFIX = "util";
	
	private Utility<T> util;
	
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
	
	@Override
	public void initConfig() throws ToolConfigException
	{
		ConfigurationInterpolator.registerGlobalLookup(UTIL_LOOKUP_PREFIX, new UtilityConfigStrLookup(this.util));
		
		super.initConfig();
	}
}