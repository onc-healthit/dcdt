package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.net.InetProtocol;
import gov.hhs.onc.dcdt.nio.channels.impl.AbstractSelectionAttachment;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerQueryAttachment;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import javax.annotation.Nonnegative;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Message;

@Component("dnsServerQueryAttachmentImpl")
@Lazy
@Scope("prototype")
@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public class DnsServerQueryAttachmentImpl extends AbstractSelectionAttachment implements DnsServerQueryAttachment {
    private int querySize = -1;

    public DnsServerQueryAttachmentImpl(InetProtocol protocol) {
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
