package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsRecordUtils;
import gov.hhs.onc.dcdt.service.mail.james.ToolDnsService;
import gov.hhs.onc.dcdt.service.mail.james.config.DnsServiceConfigBean;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import javax.annotation.Nullable;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.james.dnsservice.api.TemporaryResolutionException;
import org.apache.james.dnsservice.dnsjava.DNSJavaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.TextParseException;

public class ToolDnsServiceImpl extends DNSJavaService implements ToolDnsService {
    private final static String CONFIG_PROP_NAME_SET_AS_DNS_JAVA_DEFAULT = "setAsDNSJavaDefault";

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolDnsServiceImpl.class);

    private AbstractApplicationContext appContext;
    private DnsServiceConfigBean configBean;
    private DnsLookupService localLookupService, extLookupService;

    @Override
    public void init() throws Exception {
        super.init();

        if (this.configBean.hasCache()) {
            this.cache = this.configBean.getCache();
        }

        this.localLookupService = this.createLookupService(this.configBean.getLocalResolver());
        this.extLookupService = this.createLookupService(this.configBean.getExternalResolver());
    }

    @Override
    public void configure(HierarchicalConfiguration config) throws ConfigurationException {
        config.setProperty(CONFIG_PROP_NAME_SET_AS_DNS_JAVA_DEFAULT, false);

        super.configure(config);
    }

    @Nullable
    @Override
    protected Record[] lookup(String nameStr, int recordTypeId, String recordTypeDesc) throws TemporaryResolutionException {
        DnsRecordType recordType = ToolDnsRecordUtils.findByType(recordTypeId);

        if (recordType == null) {
            LOGGER.error(String.format("Unable to perform DNS lookup (name=%s) for unknown record type (desc=%s): %d", nameStr, recordTypeDesc, recordTypeId));

            return null;
        }

        Name name;

        try {
            name = Name.fromString(nameStr);
        } catch (TextParseException e) {
            LOGGER.error(String.format("Unable to parse DNS lookup (recordType=%s) name: %s", recordType.getTypeDisplay(), nameStr), e);

            return null;
        }

        DnsLookupResult<? extends Record> lookupResult;

        try {
            lookupResult = this.localLookupService.lookupRecords(recordType, recordType.getRecordClass(), name);

            if (!lookupResult.isSuccess()) {
                lookupResult = this.extLookupService.lookupRecords(recordType, recordType.getRecordClass(), name);
            }
        } catch (DnsException e) {
            LOGGER.error(String.format("Unable to perform DNS lookup (recordType=%s, name=%s) name.", recordType.getTypeDisplay(), nameStr), e);

            return null;
        }

        return ToolCollectionUtils.toArray(lookupResult.getOrderedAnswers(), Record.class);
    }

    private DnsLookupService createLookupService(Resolver resolver) {
        DnsLookupService lookupService = ToolBeanFactoryUtils.createBeanOfType(this.appContext, DnsLookupService.class);
        // noinspection ConstantConditions
        lookupService.setCache(this.configBean.getCache());
        lookupService.setResolver(resolver);

        return lookupService;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }

    @Override
    public DnsServiceConfigBean getConfigBean() {
        return this.configBean;
    }

    @Override
    public void setConfigBean(DnsServiceConfigBean configBean) {
        this.configBean = configBean;
    }
}
