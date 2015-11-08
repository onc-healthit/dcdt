package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.utils.ToolPropertyUtils;

public final class JavaMailProperties {
    public final static String PREFIX = "mail";

    public final static String SUFFIX_ENABLE = "enable";
    public final static String SUFFIX_FROM = "from";

    public final static String SUFFIX_AUTH = "auth";
    public final static String SUFFIX_AUTH_MECHANISMS = ToolPropertyUtils.joinName(SUFFIX_AUTH, "mechanisms");

    public final static String SUFFIX_LOCALHOST = "localhost";

    public final static String SUFFIX_SSL = "ssl";
    public final static String SUFFIX_SSL_ENABLE = ToolPropertyUtils.joinName(SUFFIX_SSL, SUFFIX_ENABLE);
    public final static String SUFFIX_SSL_TRUST = ToolPropertyUtils.joinName(SUFFIX_SSL, "trust");

    public final static String SUFFIX_STARTTLS = "starttls";
    public final static String SUFFIX_STARTTLS_ENABLE = ToolPropertyUtils.joinName(SUFFIX_STARTTLS, SUFFIX_ENABLE);

    public final static String DEBUG = ToolPropertyUtils.joinName(PREFIX, "debug");
    public final static String FROM = ToolPropertyUtils.joinName(PREFIX, SUFFIX_FROM);

    private JavaMailProperties() {
    }
}
