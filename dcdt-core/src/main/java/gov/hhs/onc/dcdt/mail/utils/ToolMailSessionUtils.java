package gov.hhs.onc.dcdt.mail.utils;

import gov.hhs.onc.dcdt.mail.JavaMailProperties;
import gov.hhs.onc.dcdt.mail.smtp.SmtpAuthMechanism;
import gov.hhs.onc.dcdt.net.SslType;
import java.util.Properties;
import javax.annotation.Nullable;
import javax.mail.Session;

public final class ToolMailSessionUtils {
    private final static String AUTH_MECHANISMS_PROP_VALUE = SmtpAuthMechanism.LOGIN.getId();

    public static Session buildSession() {
        return buildSession(null);
    }

    public static Session buildSession(@Nullable SslType sslType) {
        Properties props = new Properties();
        props.put(JavaMailProperties.DEBUG_AUTH_NAME, Boolean.TRUE);

        boolean startTls = (sslType == SslType.STARTTLS);

        if ((sslType == SslType.NONE) || startTls) {
            props.put(JavaMailProperties.SMTP_AUTH_MECHANISMS_NAME, AUTH_MECHANISMS_PROP_VALUE);
            props.put(JavaMailProperties.SMTP_SOCKET_FACTORY_FALLBACK_NAME, Boolean.FALSE);
            props.put(JavaMailProperties.SMTP_USE_SOCKET_CHANNELS_NAME, Boolean.TRUE);

            if (startTls) {
                props.put(JavaMailProperties.SMTP_STARTTLS_ENABLE_NAME, Boolean.TRUE);
                props.put(JavaMailProperties.SMTP_STARTTLS_REQUIRED_NAME, Boolean.TRUE);
                props.put(JavaMailProperties.SMTP_SSL_TRUST_NAME, JavaMailProperties.SSL_TRUST_ALL_VALUE);
            }
        } else if (sslType == SslType.SSL) {
            props.put(JavaMailProperties.SMTPS_AUTH_MECHANISMS_NAME, AUTH_MECHANISMS_PROP_VALUE);
            props.put(JavaMailProperties.SMTPS_SOCKET_FACTORY_FALLBACK_NAME, Boolean.FALSE);
            props.put(JavaMailProperties.SMTPS_SSL_ENABLE_NAME, Boolean.TRUE);
            props.put(JavaMailProperties.SMTPS_SSL_TRUST_NAME, JavaMailProperties.SSL_TRUST_ALL_VALUE);
            props.put(JavaMailProperties.SMTPS_USE_SOCKET_CHANNELS_NAME, Boolean.TRUE);
        }

        return Session.getInstance(props);
    }

    private ToolMailSessionUtils() {
    }
}
