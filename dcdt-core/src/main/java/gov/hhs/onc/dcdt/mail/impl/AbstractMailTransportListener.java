package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.mail.MailTransportEventType;
import gov.hhs.onc.dcdt.mail.MailTransportListener;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.nio.charset.Charset;
import javax.mail.event.TransportEvent;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractMailTransportListener extends AbstractToolBean implements MailTransportListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractMailTransportListener.class);

    protected Charset mailEnc;

    protected AbstractMailTransportListener(Charset mailEnc) {
        this.mailEnc = mailEnc;
    }

    @Override
    public void messageDelivered(TransportEvent event) {
        this.processMessageEvent(MailTransportEventType.MESSAGE_DELIVERED, event);
    }

    @Override
    public void messageNotDelivered(TransportEvent event) {
        this.processMessageEvent(MailTransportEventType.MESSAGE_NOT_DELIVERED, event);
    }

    @Override
    public void messagePartiallyDelivered(TransportEvent event) {
        this.processMessageEvent(MailTransportEventType.MESSAGE_PARTIALLY_DELIVERED, event);
    }

    protected void processMessageEvent(MailTransportEventType eventType, TransportEvent event) {
        try {
            this.processMessageEventInternal(eventType, event, new ToolMimeMessageHelper(((MimeMessage) event.getMessage()), this.mailEnc));
        } catch (Exception e) {
            LOGGER.error(String.format("Unable to process (class=%s) mail message transport event (type=%s).", ToolClassUtils.getName(this), eventType.name()),
                e);
        }
    }

    protected abstract void processMessageEventInternal(MailTransportEventType eventType, TransportEvent event, ToolMimeMessageHelper mimeMsgHelper)
        throws Exception;
}
