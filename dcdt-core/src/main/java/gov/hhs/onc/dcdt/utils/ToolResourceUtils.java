package gov.hhs.onc.dcdt.utils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

public abstract class ToolResourceUtils {
    public final static String URL_PREFIX_DELIM = ":";
    public final static String CLASSPATH_ALL_URL_PREFIX = "classpath*" + URL_PREFIX_DELIM;

    private final static String RESOURCE_LOC_DELIMS = ",; \t\n";
    private final static String META_INF_RESOURCE_PATH = "META-INF";

    private final static ResourcePatternResolver RES_PATTERN_RESOLVER_DEFAULT = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());

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

    public static Map<String, List<Resource>> mapResourceLocations(List<String> resourceLocs) {
        return mapResourceLocations(resourceLocs, RES_PATTERN_RESOLVER_DEFAULT);
    }

    public static Map<String, List<Resource>> mapResourceLocations(List<String> resourceLocs, ResourcePatternResolver resourcePatternResolver) {
        Map<String, List<Resource>> resourceLocsMap = new LinkedHashMap<>(resourceLocs.size());
        List<Resource> resources;

        for (String resourceLoc : resourceLocs) {
            resources = resolveResourceLocations(resourceLoc, resourcePatternResolver);

            if (resources != null) {
                resourceLocsMap.put(resourceLoc, resources);
            }
        }

        return resourceLocsMap;
    }

    public static List<Resource> resolveResourceLocations(String resourceLoc) {
        return resolveResourceLocations(resourceLoc, RES_PATTERN_RESOLVER_DEFAULT);
    }

    public static List<Resource> resolveResourceLocations(String resourceLoc, ResourcePatternResolver resourcePatternResolver) {
        try {
            return Arrays.asList(resourcePatternResolver.getResources(resourceLoc));
        } catch (IOException e) {
        }

        return null;
    }

    public static List<String> splitResourceLocations(String resourceLocsStr) {
        String[] resourceLocsArr = StringUtils.split(resourceLocsStr, RESOURCE_LOC_DELIMS);
        List<String> resourceLocs = new ArrayList<>(resourceLocsArr.length);

        for (String resourceLoc : resourceLocsArr) {
            if (!StringUtils.isBlank(resourceLoc = resourceLoc.trim())) {
                resourceLocs.add(resourceLoc);
            }
        }

        return resourceLocs;
    }
}
