package gov.hhs.onc.dcdt.mail;

import gov.hhs.onc.dcdt.utils.ToolPropertyUtils;

public final class JavaMailMimeProperties {
    private final static String PREFIX = ToolPropertyUtils.joinName(JavaMailProperties.PREFIX, "mime");

    public final static String CACHE_MULTIPART = ToolPropertyUtils.joinName(PREFIX, "cachemultipart");

    private JavaMailMimeProperties() {
    }
}
