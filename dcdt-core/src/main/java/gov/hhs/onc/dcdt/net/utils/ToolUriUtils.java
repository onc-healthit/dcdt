package gov.hhs.onc.dcdt.net.utils;

import gov.hhs.onc.dcdt.net.ToolUriException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.lang3.StringUtils;

public final class ToolUriUtils {
    public final static String PATH_DELIM = "/";
    public final static String PROTOCOL_DELIM = ":" + StringUtils.repeat(PATH_DELIM, 2);
    public final static String PORT_DELIM = ":";

    private ToolUriUtils() {
    }

    public static URI fromString(String uriStr) throws ToolUriException {
        try {
            return new URI(uriStr);
        } catch (URISyntaxException e) {
            throw new ToolUriException(String.format("Unable to create URI from string (%s): %s", uriStr, e.getMessage()), e);
        }
    }
}
