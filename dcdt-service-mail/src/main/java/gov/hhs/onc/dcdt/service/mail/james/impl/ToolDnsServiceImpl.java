package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.service.mail.james.ToolDnsService;
import gov.hhs.onc.dcdt.service.mail.james.config.DnsServiceConfigBean;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.james.dnsservice.dnsjava.DNSJavaService;

public class ToolDnsServiceImpl extends DNSJavaService implements ToolDnsService {
    private final static String CONFIG_PROP_NAME_SET_AS_DNS_JAVA_DEFAULT = "setAsDNSJavaDefault";

    private DnsServiceConfigBean configBean;

    @Override
    public void init() throws Exception {
        super.init();

        if (this.configBean.hasCache()) {
            this.cache = this.configBean.getCache();
        }

        if (this.configBean.hasResolver()) {
            this.resolver = this.configBean.getResolver();
        }
    }

    @Override
    public void configure(HierarchicalConfiguration config) throws ConfigurationException {
        config.setProperty(CONFIG_PROP_NAME_SET_AS_DNS_JAVA_DEFAULT, false);

        super.configure(config);
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
