package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.utils.ToolPropertyUtils;

public final class JavaMailSmtpProperties {
    private final static String PREFIX = ToolPropertyUtils.joinName(JavaMailProperties.PREFIX, MailTransportProtocol.SMTP.getProtocol());

    public final static String SSL_ENABLE = ToolPropertyUtils.joinName(PREFIX, JavaMailProperties.SUFFIX_SSL_ENABLE);
    public final static String SSL_TRUST = ToolPropertyUtils.joinName(PREFIX, JavaMailProperties.SUFFIX_SSL_TRUST);

    public final static String SSL_STARTTLS_ENABLE = ToolPropertyUtils.joinName(PREFIX, JavaMailProperties.SUFFIX_SSL_STARTTLS_ENABLE);

    private JavaMailSmtpProperties() {
    }
}
