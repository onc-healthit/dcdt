package gov.hhs.onc.dcdt.mail;

import javax.annotation.Nonnegative;

public enum MailStoreProtocol implements MailProtocol {
    POP3("pop3", 110, false), POP3S("pop3", 995, true), IMAP("imap", 143, false), IMAPS("imap", 993, true);

    private final String protocol;
    private final int defaultPort;
    private final boolean ssl;

    private MailStoreProtocol(String protocol, @Nonnegative int defaultPort, boolean ssl) {
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
