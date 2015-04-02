package gov.hhs.onc.dcdt.service.mail.james;

import org.apache.mailet.Mail;

public enum MailProcessorState {
    GHOST(Mail.GHOST), ROOT(Mail.DEFAULT), TRANSPORT(Mail.TRANSPORT), ERROR(Mail.ERROR);

    private final String state;

    private MailProcessorState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }
}
