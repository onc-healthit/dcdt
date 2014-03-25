package gov.hhs.onc.dcdt.service.dns.server;

import gov.hhs.onc.dcdt.beans.ToolLifecycleBean;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import org.springframework.context.ApplicationContextAware;

public interface DnsServer extends ApplicationContextAware, ToolLifecycleBean {
    public DnsServerConfig getConfig();
}
