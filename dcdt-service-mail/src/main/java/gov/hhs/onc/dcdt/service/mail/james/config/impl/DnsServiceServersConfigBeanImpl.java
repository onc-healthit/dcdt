package gov.hhs.onc.dcdt.service.mail.james.config.impl;

import gov.hhs.onc.dcdt.service.mail.james.config.DnsServiceServersConfigBean;
import java.util.List;

public class DnsServiceServersConfigBeanImpl extends AbstractJamesConfigBean implements DnsServiceServersConfigBean {
    private List<String> servers;

    @Override
    public List<String> getServers() {
        return this.servers;
    }

    @Override
    public void setServers(List<String> servers) {
        this.servers = servers;
    }
}
