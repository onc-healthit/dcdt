package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.net.sockets.impl.AbstractSocketRequest;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerRequest;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import javax.annotation.Nonnegative;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Message;

@Component("dnsServerReq")
@Lazy
@Scope("prototype")
public class DnsServerRequestImpl extends AbstractSocketRequest implements DnsServerRequest {
    private int querySize = -1;

    public DnsServerRequestImpl(InetProtocol protocol) {
        super(protocol, Message.MAXLENGTH, Message.MAXLENGTH);
    }

    @Override
    public boolean hasQuerySize() {
        return ToolNumberUtils.isPositive(this.querySize);
    }

    @Override
    public int getQuerySize() {
        return this.querySize;
    }

    @Nonnegative
    @Override
    public int setQuerySize(@Nonnegative int querySize) {
        return (this.querySize = querySize);
    }
}
