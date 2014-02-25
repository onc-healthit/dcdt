package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.dns.utils.ToolDnsMessageUtils;
import gov.hhs.onc.dcdt.nio.channels.impl.AbstractTcpChannelListener;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerQueryAttachment;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerQueryResolver;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerQueryResolverCallback;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import javax.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.stereotype.Component;

@Component("dnsServerTcpChannelListener")
@Lazy
@Scope("prototype")
@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public class DnsServerTcpChannelListener extends AbstractTcpChannelListener<DnsServerQueryAttachment, DnsServerQueryResolver, DnsServerQueryResolverCallback> {
    private DnsServerConfig dnsServerConfig;

    public DnsServerTcpChannelListener(DnsServerConfig dnsServerConfig) {
        super(DnsServerQueryAttachment.class, DnsServerQueryResolver.class, DnsServerQueryResolverCallback.class, dnsServerConfig.getBindSocketAddress());

        this.dnsServerConfig = dnsServerConfig;
    }

    @Override
    protected boolean processReadOperation(SelectionKey selKey, SocketChannel readChannel, DnsServerQueryAttachment selAttachment) throws IOException {
        if (!selAttachment.hasQuerySize()) {
            ByteBuffer reqBuffer = selAttachment.getRequestBuffer();

            if (reqBuffer.limit() != ToolDnsMessageUtils.DATA_SIZE_DNS_MSG_QUERY_SIZE_PREFIX) {
                reqBuffer.limit(ToolDnsMessageUtils.DATA_SIZE_DNS_MSG_QUERY_SIZE_PREFIX);
            }

            readChannel.read(reqBuffer);

            if (!reqBuffer.hasRemaining()) {
                reqBuffer.limit(reqBuffer.limit() + selAttachment.setQuerySize(ToolDnsMessageUtils.parseQuerySizeData(reqBuffer.get(0), reqBuffer.get(1))));
            } else {
                selKey.selector().wakeup();

                return false;
            }
        }

        return super.processReadOperation(selKey, readChannel, selAttachment);
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
