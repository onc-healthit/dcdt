package gov.hhs.onc.dcdt.service.dns.impl;

import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.dns.DnsTransportProtocol;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.dns.DnsService;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServer;
import gov.hhs.onc.dcdt.service.impl.AbstractToolService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@AutoStartup(false)
@Component("dnsServiceImpl")
@Phase(Phase.PHASE_PRECEDENCE_HIGHEST + 1)
@ServiceContextConfiguration({ "spring/spring-service-dns.xml", "spring/spring-service-dns-*.xml" })
public class DnsServiceImpl extends AbstractToolService<DnsTransportProtocol, DnsServerConfig, DnsServer> implements DnsService {
    public DnsServiceImpl() {
        super(DnsServer.class);
    }

    @Autowired(required = false)
    @Override
    public void setServers(List<DnsServer> servers) {
        super.setServers(servers);
    }

    @Override
    @Resource(name = "taskExecServiceDns")
    public void setTaskExecutor(ThreadPoolTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
