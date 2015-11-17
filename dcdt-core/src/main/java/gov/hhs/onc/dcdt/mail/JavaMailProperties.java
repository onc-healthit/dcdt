package gov.hhs.onc.dcdt.mail;

public final class JavaMailProperties {
    public final static String PREFIX = "mail";
    public final static String SMTP_PREFIX = PREFIX + ".smtp";
    public final static String SMTPS_PREFIX = SMTP_PREFIX + "s";

    public final static String AUTH_SUFFIX = ".auth";
    public final static String ENABLE_SUFFIX = ".enable";
    public final static String SSL_SUFFIX = ".ssl";
    public final static String STARTTLS_SUFFIX = ".starttls";
    public final static String TIMEOUT_SUFFIX = "timeout";

    public final static String AUTH_MECHANISMS_SUFFIX = AUTH_SUFFIX + ".mechanisms";
    public final static String CONNECTION_TIMEOUT_SUFFIX = ".connection" + TIMEOUT_SUFFIX;
    public final static String FROM_SUFFIX = ".from";
    public final static String LOCALHOST_SUFFIX = ".localhost";
    public final static String SOCKET_FACTORY_FALLBACK_SUFFIX = ".socketFactory.fallback";
    public final static String SSL_TRUST_SUFFIX = SSL_SUFFIX + ".trust";
    public final static String USE_SOCKET_CHANNELS_SUFFIX = ".usesocketchannels";
    public final static String WRITE_TIMEOUT_SUFFIX = ".write" + TIMEOUT_SUFFIX;

    public final static String DEBUG_NAME = PREFIX + ".debug";
    public final static String DEBUG_AUTH_NAME = DEBUG_NAME + AUTH_SUFFIX;

    public final static String SMTP_AUTH_NAME = SMTP_PREFIX + AUTH_SUFFIX;
    public final static String SMTP_AUTH_MECHANISMS_NAME = SMTP_PREFIX + AUTH_MECHANISMS_SUFFIX;
    public final static String SMTP_CONNECTION_TIMEOUT_NAME = SMTP_PREFIX + CONNECTION_TIMEOUT_SUFFIX;
    public final static String SMTP_FROM_NAME = SMTP_PREFIX + FROM_SUFFIX;
    public final static String SMTP_LOCALHOST_NAME = SMTP_PREFIX + LOCALHOST_SUFFIX;
    public final static String SMTP_SOCKET_FACTORY_FALLBACK_NAME = SMTP_PREFIX + SOCKET_FACTORY_FALLBACK_SUFFIX;
    public final static String SMTP_SSL_TRUST_NAME = SMTP_PREFIX + SSL_TRUST_SUFFIX;
    public final static String SMTP_STARTTLS_ENABLE_NAME = SMTP_PREFIX + STARTTLS_SUFFIX + ENABLE_SUFFIX;
    public final static String SMTP_STARTTLS_REQUIRED_NAME = SMTP_PREFIX + STARTTLS_SUFFIX + ".required";
    public final static String SMTP_TIMEOUT_NAME = SMTP_PREFIX + "." + TIMEOUT_SUFFIX;
    public final static String SMTP_USE_SOCKET_CHANNELS_NAME = SMTP_PREFIX + USE_SOCKET_CHANNELS_SUFFIX;
    public final static String SMTP_WRITE_TIMEOUT_NAME = SMTP_PREFIX + WRITE_TIMEOUT_SUFFIX;

    public final static String SMTPS_AUTH_NAME = SMTPS_PREFIX + AUTH_SUFFIX;
    public final static String SMTPS_AUTH_MECHANISMS_NAME = SMTPS_PREFIX + AUTH_MECHANISMS_SUFFIX;
    public final static String SMTPS_CONNECTION_TIMEOUT_NAME = SMTPS_PREFIX + CONNECTION_TIMEOUT_SUFFIX;
    public final static String SMTPS_FROM_NAME = SMTPS_PREFIX + FROM_SUFFIX;
    public final static String SMTPS_LOCALHOST_NAME = SMTPS_PREFIX + LOCALHOST_SUFFIX;
    public final static String SMTPS_SOCKET_FACTORY_FALLBACK_NAME = SMTPS_PREFIX + SOCKET_FACTORY_FALLBACK_SUFFIX;
    public final static String SMTPS_SSL_ENABLE_NAME = SMTPS_PREFIX + SSL_SUFFIX + ENABLE_SUFFIX;
    public final static String SMTPS_SSL_TRUST_NAME = SMTPS_PREFIX + SSL_TRUST_SUFFIX;
    public final static String SMTPS_TIMEOUT_NAME = SMTPS_PREFIX + "." + TIMEOUT_SUFFIX;
    public final static String SMTPS_USE_SOCKET_CHANNELS_NAME = SMTPS_PREFIX + USE_SOCKET_CHANNELS_SUFFIX;
    public final static String SMTPS_WRITE_TIMEOUT_NAME = SMTPS_PREFIX + WRITE_TIMEOUT_SUFFIX;

    public final static String SSL_TRUST_ALL_VALUE = "*";

    private JavaMailProperties() {
    }
}
