package gov.hhs.onc.dcdt.mail;

import javax.mail.event.TransportEvent;

public enum MailTransportEventType {
    MESSAGE_DELIVERED(TransportEvent.MESSAGE_DELIVERED), MESSAGE_NOT_DELIVERED(TransportEvent.MESSAGE_NOT_DELIVERED), MESSAGE_PARTIALLY_DELIVERED(
        TransportEvent.MESSAGE_PARTIALLY_DELIVERED);

    private final int type;

    private MailTransportEventType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
