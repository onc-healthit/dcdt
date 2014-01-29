package gov.hhs.onc.dcdt.net.utils;

import gov.hhs.onc.dcdt.net.ToolUrlException;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public abstract class ToolUrlUtils {
    public final static String DELIM_URL = "/";
    public final static String DELIM_URL_PROTOCOL = ":" + StringUtils.repeat(DELIM_URL, 2);
    public final static String DELIM_URL_PORT = ":";

    public static URL contextRelative(URL url, @Nullable String urlStrRelative) throws ToolUrlException {
        return join(getContextUrlString(url), urlStrRelative);
    }

    public static URL getContextUrl(URL url) throws ToolUrlException {
        return fromString(getContextUrlString(url));
    }

    public static String getContextUrlString(URL url) {
        ToolStrBuilder contextUrlStrBuilder = new ToolStrBuilder();

        if (hasProtocol(url)) {
            contextUrlStrBuilder.append(url.getProtocol());
            contextUrlStrBuilder.appendDelimiter(DELIM_URL_PROTOCOL);
        }

        if (hasHost(url)) {
            contextUrlStrBuilder.append(url.getHost());
        }

        if (hasPort(url)) {
            contextUrlStrBuilder.appendWithDelimiter(url.getPort(), DELIM_URL_PORT);
        }

        if (hasContextPath(url)) {
            contextUrlStrBuilder.appendWithDelimiter(getContextPath(url), DELIM_URL);
        }

        return contextUrlStrBuilder.build();
    }

    public static boolean hasContextPath(URL url) {
        return !StringUtils.isBlank(getContextPath(url));
    }

    @Nullable
    public static String getContextPath(URL url) {
        return ToolArrayUtils.getFirst(getPathParts(url));
    }

    @Nullable
    public static String[] getPathParts(URL url) {
        return StringUtils.split(url.getPath(), DELIM_URL);
    }

    public static URL join(String ... urlStrs) throws ToolUrlException {
        return join(ToolArrayUtils.asList(urlStrs));
    }

    public static URL join(Iterable<String> urlStrs) throws ToolUrlException {
        return fromString(joinString(urlStrs));
    }

    public static String joinString(String ... urlStrs) {
        return joinString(ToolArrayUtils.asList(urlStrs));
    }

    public static String joinString(Iterable<String> urlStrs) {
        return ToolStringUtils.joinDelimit(urlStrs, DELIM_URL);
    }

    public static boolean hasProtocol(URL url) {
        return !StringUtils.isBlank(url.getProtocol());
    }

    public static boolean hasHost(URL url) {
        return !StringUtils.isBlank(url.getHost());
    }

    public static boolean hasPort(URL url) {
        return ToolNumberUtils.isPositive(url.getPort());
    }

    public static boolean hasPath(URL url) {
        return !StringUtils.isBlank(url.getPath());
    }

    public static boolean hasQuery(URL url) {
        return !StringUtils.isBlank(url.getQuery());
    }

    public static URL fromString(String urlStr) throws ToolUrlException {
        try {
            return new URL(urlStr);
        } catch (MalformedURLException e) {
            throw new ToolUrlException(String.format("Unable to create URL from string: %s", urlStr), e);
        }
    }
}
