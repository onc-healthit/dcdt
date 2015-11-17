package gov.hhs.onc.dcdt.mail.smtp;

public interface SmtpReply extends SmtpMessage<SmtpReplyCode> {
    public boolean hasParameters();

    public String[] getParameters();

    public void setParameters(String ... params);
}
