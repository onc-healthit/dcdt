package gov.hhs.onc.dcdt.service.mail.impl;

import gov.hhs.onc.dcdt.service.mail.MailProtocolMessage;
import gov.hhs.onc.dcdt.service.mail.MailProtocolMessageDirection;

public abstract class AbstractMailProtocolMessage implements MailProtocolMessage {
    protected MailProtocolMessageDirection direction;

    protected AbstractMailProtocolMessage(MailProtocolMessageDirection direction) {
        this.direction = direction;
    }

    @Override
    public MailProtocolMessageDirection getDirection() {
        return this.direction;
    }
}
