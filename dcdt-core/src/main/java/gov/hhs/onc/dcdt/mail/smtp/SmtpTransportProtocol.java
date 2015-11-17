package gov.hhs.onc.dcdt.mail.smtp;

import gov.hhs.onc.dcdt.net.SslType;
import gov.hhs.onc.dcdt.net.TransportProtocol;
import javax.annotation.Nonnegative;
import javax.mail.URLName;

public enum SmtpTransportProtocol implements TransportProtocol {
    SMTP("smtp", 25, SslType.NONE), SMTPS("smtp", 465, SslType.SSL), SMTP_SUBMIT("smtp", 587, SslType.STARTTLS);

    private final String scheme;
    private final int defaultPort;
    private final SslType sslType;

    private SmtpTransportProtocol(String scheme, @Nonnegative int defaultPort, SslType sslType) {
        this.scheme = scheme;
        this.defaultPort = defaultPort;
        this.sslType = sslType;
    }

    public URLName toUrlName() {
        return new URLName(this.scheme, null, -1, null, null, null);
    }

    @Nonnegative
    @Override
    public int getDefaultPort() {
        return this.defaultPort;
    }

    @Override
    public String getId() {
        return this.name();
    }

    @Override
    public String getScheme() {
        return this.scheme;
    }

    @Override
    public SslType getSslType() {
        return this.sslType;
    }
}
