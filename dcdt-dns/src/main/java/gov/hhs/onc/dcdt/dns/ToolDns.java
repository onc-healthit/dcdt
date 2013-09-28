package gov.hhs.onc.dcdt.dns;


import gov.hhs.onc.dcdt.dns.conf.DnsServerConfig;
import gov.hhs.onc.dcdt.standalone.ToolWrapper;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class ToolDns extends ToolWrapper<ClassPathXmlApplicationContext> {
    private final static List<String> CONTEXT_CONFIG_LOCS_DNS = ToolResourceUtils.getOverrideableResourceLocation("spring/spring-dns*.xml");
    
    @Autowired
    private ThreadPoolTaskExecutor dnsServerTaskExecutor;

    @Autowired
    private List<DnsServerConfig> dnsServerConfigs;

    private List<DnsServer> dnsServers;

    public ToolDns() {
        super();
    }

    public ToolDns(AbstractApplicationContext parentContext) {
        super(parentContext);
    }

    private ToolDns(String ... args) {
        super(args);
    }

    public static void main(String ... args) {
        new ToolDns(args).start();
    }

    @Override
    protected void startWrapper() {
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
    }

    @Override
    protected void stopWrapper() {
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
