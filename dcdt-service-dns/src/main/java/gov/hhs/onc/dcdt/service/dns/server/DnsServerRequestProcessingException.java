package gov.hhs.onc.dcdt.service.dns.server;

import gov.hhs.onc.dcdt.service.dns.DnsServiceException;
import javax.annotation.Nullable;
import org.xbill.DNS.Message;

public class DnsServerRequestProcessingException extends DnsServiceException {
    private final static long serialVersionUID = 0L;

    private Message reqMsg;
    private Message respMsg;

    public DnsServerRequestProcessingException(@Nullable Message reqMsg, @Nullable Message respMsg) {
        this(reqMsg, respMsg, null, null);
    }

    public DnsServerRequestProcessingException(@Nullable Message reqMsg, @Nullable Message respMsg, String msg) {
        this(reqMsg, respMsg, msg, null);
    }

    public DnsServerRequestProcessingException(@Nullable Message reqMsg, @Nullable Message respMsg, Throwable cause) {
        this(reqMsg, respMsg, null, cause);
    }

    public DnsServerRequestProcessingException(@Nullable Message reqMsg, @Nullable Message respMsg, String msg, Throwable cause) {
        super(msg, cause);

        this.reqMsg = reqMsg;
        this.respMsg = respMsg;
    }

    public boolean hasRequestMessage() {
        return this.reqMsg != null;
    }

    @Nullable
    public Message getRequestMessage() {
        return this.reqMsg;
    }

    public boolean hasResponseMessage() {
        return this.respMsg != null;
    }

    @Nullable
    public Message getResponseMessage() {
        return this.respMsg;
    }
}
