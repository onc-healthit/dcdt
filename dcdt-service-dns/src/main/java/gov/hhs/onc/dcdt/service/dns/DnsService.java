package gov.hhs.onc.dcdt.service.dns;


import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.dns.conf.DnsServerConfig;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class DnsService extends ToolService {
    private final static List<String> CONTEXT_CONFIG_LOCS_DNS = ToolResourceUtils.getOverrideableResourceLocation("spring/spring-service-dns*.xml");

    @Autowired
    private ThreadPoolTaskExecutor dnsServerTaskExecutor;

    @Autowired
    private List<DnsServerConfig> dnsServerConfigs;

    private List<DnsServer> dnsServers;

    public DnsService() {
        super();
    }

    public DnsService(ApplicationContext parentContext) {
        super(parentContext);
    }

    @Override
    protected void startService() {
        if (this.dnsServerConfigs != null) {
            this.dnsServers = new ArrayList<>(this.dnsServerConfigs.size());

            for (DnsServerConfig dnsServerConfig : this.dnsServerConfigs) {
                DnsServer dnsServer = new DnsServer();
                dnsServer.setDnsServerConfig(dnsServerConfig);
                this.beanFactory.autowireBeanProperties(dnsServer, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
                this.dnsServers.add(dnsServer);

                this.dnsServerTaskExecutor.execute(dnsServer);
            }
        }

        super.startService();
    }

    @Override
    protected void stopService() {
        if (this.dnsServers != null) {
            for (DnsServer dnsServer : this.dnsServers) {
                dnsServer.stop();
            }
        }
    }

    @Override
    protected void initializeContext() {
        this.contextConfigLocs.addAll(CONTEXT_CONFIG_LOCS_DNS);

        super.initializeContext();
    }
}
