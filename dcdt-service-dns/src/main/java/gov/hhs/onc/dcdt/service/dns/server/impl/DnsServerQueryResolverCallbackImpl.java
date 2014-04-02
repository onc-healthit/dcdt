package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.dns.DnsMessageRcode;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsMessageUtils;
import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.nio.channels.SelectionOperationType;
import gov.hhs.onc.dcdt.nio.channels.impl.AbstractChannelListenerDataProcessorCallback;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerQueryAttachment;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerQueryResolutionException;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerQueryResolver;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerQueryResolverCallback;
import java.nio.channels.SelectionKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("dnsServerQueryResolverCallbackImpl")
@Lazy
@Scope("prototype")
@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public class DnsServerQueryResolverCallbackImpl extends AbstractChannelListenerDataProcessorCallback<DnsServerQueryResolver, DnsServerQueryAttachment>
    implements DnsServerQueryResolverCallback {
    private final static Logger LOGGER = LoggerFactory.getLogger(DnsServerQueryResolverCallbackImpl.class);

    public DnsServerQueryResolverCallbackImpl(SelectionOperationType readOpType, SelectionOperationType writeOpType, InetProtocol protocol,
        SelectionKey selKey, DnsServerQueryAttachment selAttachment) {
        super(DnsServerQueryResolver.class, readOpType, writeOpType, protocol, selKey, selAttachment);
    }

    @Override
    public void onFailure(Throwable th) {
        LOGGER.error(th.getMessage(), th.getCause());

        this.setResponseData(ToolDnsMessageUtils.createErrorResponse(((DnsServerQueryResolutionException) th).getRequestMessage(), DnsMessageRcode.SERVFAIL)
            .toWire());
    }
}
