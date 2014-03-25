package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.utils.ToolPropertyUtils;

public final class JavaMailProperties {
    final static String PREFIX = "mail";

    final static String SUFFIX_ENABLE = "enable";

    final static String SUFFIX_SSL = "ssl";
    final static String SUFFIX_SSL_ENABLE = ToolPropertyUtils.joinName(SUFFIX_SSL, SUFFIX_ENABLE);
    final static String SUFFIX_SSL_TRUST = ToolPropertyUtils.joinName(SUFFIX_SSL, "trust");

    final static String SUFFIX_SSL_STARTTLS = ToolPropertyUtils.joinName(SUFFIX_SSL, "starttls");
    final static String SUFFIX_SSL_STARTTLS_ENABLE = ToolPropertyUtils.joinName(SUFFIX_SSL_STARTTLS, SUFFIX_ENABLE);

    public final static String DEBUG = ToolPropertyUtils.joinName(PREFIX, "debug");

    private JavaMailProperties() {
    }
}
