package gov.hhs.onc.dcdt.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SystemUtils;

public abstract class ToolResourceUtils {
    public final static String URL_PREFIX_DELIM = ":";
    public final static String CLASSPATH_ALL_URL_PREFIX = "classpath*" + URL_PREFIX_DELIM;

    private final static String META_INF_RESOURCE_PATH = "META-INF";

    public static List<String> getOverrideableResourceLocations(List<String> resourceLocs) {
        return getOverrideableResourceLocations(resourceLocs, CLASSPATH_ALL_URL_PREFIX);
    }

    public static List<String> getOverrideableResourceLocations(List<String> resourceLocs, String urlPrefixDefault) {
        return getOverrideableResourceLocations(resourceLocs, urlPrefixDefault, META_INF_RESOURCE_PATH);
    }

    public static List<String> getOverrideableResourceLocations(List<String> resourceLocs, String urlPrefixDefault, String baseResourcePath) {
        List<String> overrideableResourceLocs = new ArrayList<>(resourceLocs.size() * 2);

        for (String resourceLoc : resourceLocs) {
            overrideableResourceLocs.addAll(getOverrideableResourceLocation(resourceLoc, urlPrefixDefault, baseResourcePath));
        }

        return overrideableResourceLocs;
    }

    public static List<String> getOverrideableResourceLocation(String resourceLoc) {
        return getOverrideableResourceLocation(resourceLoc, CLASSPATH_ALL_URL_PREFIX);
    }

    public static List<String> getOverrideableResourceLocation(String resourceLoc, String urlPrefixDefault) {
        return getOverrideableResourceLocation(resourceLoc, urlPrefixDefault, META_INF_RESOURCE_PATH);
    }

    public static List<String> getOverrideableResourceLocation(String resourceLoc, String urlPrefixDefault, String baseResourcePath) {
        String[] resourceLocParts = resourceLoc.contains(URL_PREFIX_DELIM) ? resourceLoc.split(URL_PREFIX_DELIM, 2) : ArrayUtils.toArray(urlPrefixDefault,
                resourceLoc);
        resourceLoc = ToolStringUtils.joinDelimit(resourceLocParts, URL_PREFIX_DELIM);

        return Arrays.asList(ArrayUtils.toArray(resourceLocParts[0] + baseResourcePath + SystemUtils.FILE_SEPARATOR + resourceLocParts[1], resourceLoc));
    }
}
