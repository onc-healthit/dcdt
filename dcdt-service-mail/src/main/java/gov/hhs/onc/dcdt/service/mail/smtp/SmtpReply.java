package gov.hhs.onc.dcdt.service.mail.smtp;

public interface SmtpReply extends SmtpMessage {
    public SmtpReplyCode getCode();

    public boolean hasParameters();

    public String[] getParameters();
}
