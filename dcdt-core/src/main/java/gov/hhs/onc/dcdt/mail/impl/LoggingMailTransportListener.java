package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.mail.MailTransportEventType;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.nio.charset.Charset;
import javax.mail.event.TransportEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("mailTransportListenerLog")
@Lazy
@Scope("prototype")
@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public class LoggingMailTransportListener extends AbstractMailTransportListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingMailTransportListener.class);

    public LoggingMailTransportListener(Charset mailEnc) {
        super(mailEnc);
    }

    @Override
    protected void processMessageEventInternal(MailTransportEventType eventType, TransportEvent event, ToolMimeMessageHelper mimeMsgHelper) throws Exception {
        switch (eventType) {
            case MESSAGE_DELIVERED:
                LOGGER.debug(String.format("Delivered (sentAddrs=[%s]) mail message:\nheaders=\n%s\ntext=\n%s\n",
                    ToolStringUtils.joinDelimit(event.getValidSentAddresses(), ", "), ToolStringUtils.joinLines(mimeMsgHelper.getHeaderLines()),
                    mimeMsgHelper.getText()));
                break;

            case MESSAGE_NOT_DELIVERED:
                LOGGER.error(String.format("Unable to deliver (invalidAddrs=[%s], unsentAddrs=[%s]) mail message:\nheaders=\n%s\ntext=\n%s\n",
                    ToolStringUtils.joinDelimit(event.getInvalidAddresses(), ", "), ToolStringUtils.joinDelimit(event.getValidUnsentAddresses(), ", "),
                    ToolStringUtils.joinLines(mimeMsgHelper.getHeaderLines()), mimeMsgHelper.getText()));
                break;

            case MESSAGE_PARTIALLY_DELIVERED:
                LOGGER.warn(String.format("Partially delivered (sentAddrs=[%s], invalidAddrs=[%s], unsentAddrs=[%s]) mail message:\nheaders=\n%s\ntext=\n%s\n",
                    ToolStringUtils.joinDelimit(event.getValidSentAddresses(), ", "), ToolStringUtils.joinDelimit(event.getInvalidAddresses(), ", "),
                    ToolStringUtils.joinDelimit(event.getValidUnsentAddresses(), ", "), ToolStringUtils.joinLines(mimeMsgHelper.getHeaderLines()),
                    mimeMsgHelper.getText()));
                break;
        }
    }
}
