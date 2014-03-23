package gov.hhs.onc.dcdt.service.mail.james.config;

import gov.hhs.onc.dcdt.config.ConfigurationNode;
import java.util.List;

@ConfigurationNode(name = "servers")
public interface DnsServiceServersConfigBean extends JamesConfigBean {
    @ConfigurationNode(name = "server")
    public List<String> getServers();

    public void setServers(List<String> servers);
}
