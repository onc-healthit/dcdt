package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.service.mail.james.ToolDnsService;
import gov.hhs.onc.dcdt.service.mail.james.config.DnsServiceConfigBean;
import org.apache.james.dnsservice.dnsjava.DNSJavaService;

public class ToolDnsServiceImpl extends DNSJavaService implements ToolDnsService {
    private DnsServiceConfigBean configBean;

    @Override
    public DnsServiceConfigBean getConfigBean() {
        return this.configBean;
    }

    @Override
    public void setConfigBean(DnsServiceConfigBean configBean) {
        this.configBean = configBean;
    }
}
