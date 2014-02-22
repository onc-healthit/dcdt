package gov.hhs.onc.dcdt.service.dns.impl;

import gov.hhs.onc.dcdt.beans.AutoStartup;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.dns.DnsService;
import gov.hhs.onc.dcdt.service.dns.server.DnsServer;
import gov.hhs.onc.dcdt.service.impl.AbstractToolService;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.stereotype.Component;

@AutoStartup(false)
@Component("dnsServiceImpl")
@ServiceContextConfiguration({ "spring/spring-service-dns.xml", "spring/spring-service-dns-*.xml" })
public class DnsServiceImpl extends AbstractToolService implements DnsService {
    @Autowired(required = false)
    private List<DnsServer> servers;

    @Override
    @SuppressWarnings({ "ConstantConditions" })
    protected synchronized void startInternal() {
        if (this.hasServers()) {
            for (DnsServer server : this.servers) {
                server.start();
            }
        }
    }

    @Override
    public boolean hasServers() {
        return !CollectionUtils.isEmpty(this.servers);
    }

    @Nullable
    @Override
    public List<DnsServer> getServers() {
        return this.servers;
    }

    @Override
    @Resource(name = "taskExecServiceDns")
    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
