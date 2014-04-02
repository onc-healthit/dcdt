package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.nio.channels.impl.AbstractUdpChannelListener;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerQueryAttachment;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerQueryResolver;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerQueryResolverCallback;
import javax.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.stereotype.Component;

@Component("dnsServerUdpChannelListener")
@Lazy
@Scope("prototype")
@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public class DnsServerUdpChannelListener extends AbstractUdpChannelListener<DnsServerQueryAttachment, DnsServerQueryResolver, DnsServerQueryResolverCallback> {
    private DnsServerConfig dnsServerConfig;

    public DnsServerUdpChannelListener(DnsServerConfig dnsServerConfig) {
        super(DnsServerQueryAttachment.class, DnsServerQueryResolver.class, DnsServerQueryResolverCallback.class, dnsServerConfig.toSocketAddress());

        this.dnsServerConfig = dnsServerConfig;
    }

    @Override
    protected DnsServerQueryResolver createDataProcessor(Object ... beanCreationArgs) {
        return super.createDataProcessor(ArrayUtils.add(beanCreationArgs, 0, this.dnsServerConfig));
    }

    @Override
    @Resource(name = "taskExecServiceDnsServerChannelQuery")
    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
