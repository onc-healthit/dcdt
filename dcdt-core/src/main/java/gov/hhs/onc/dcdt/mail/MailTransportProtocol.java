package gov.hhs.onc.dcdt.mail;

import javax.annotation.Nonnegative;

public enum MailTransportProtocol implements MailProtocol {
    SMTP("smtp", 25, false), SMTPS("smtp", 465, true), SMTP_SUBMIT("smtp", 587, false), SMTPS_SUBMIT("smtp", 587, true);

    private final String protocol;
    private final int defaultPort;
    private final boolean ssl;

    private MailTransportProtocol(String protocol, @Nonnegative int defaultPort, boolean ssl) {
        this.protocol = protocol;
        this.defaultPort = defaultPort;
        this.ssl = ssl;
    }

    @Nonnegative
    @Override
    public int getDefaultPort() {
        return this.defaultPort;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public boolean isSsl() {
        return this.ssl;
    }
}
