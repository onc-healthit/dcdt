package gov.hhs.onc.dcdt.service.dns.server.impl;

import gov.hhs.onc.dcdt.nio.channels.impl.AbstractSelectionAttachment;
import gov.hhs.onc.dcdt.service.dns.server.DnsServerQueryAttachment;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Message;

@Component("dnsServerQueryAttachmentImpl")
@Lazy
@Scope("prototype")
public class DnsServerQueryAttachmentImpl extends AbstractSelectionAttachment implements DnsServerQueryAttachment {
    public DnsServerQueryAttachmentImpl() {
        super(Message.MAXLENGTH, Message.MAXLENGTH);
    }
}
