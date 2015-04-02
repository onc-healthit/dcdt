package gov.hhs.onc.dcdt.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

public abstract class ToolResourceUtils {
    public final static String DELIM_URL_PREFIX = ":";
    public final static String DELIMS_RESOURCE_LOC = ",; \t\n";

    public final static String PREFIX_URL_CLASSPATH_ALL = "classpath*" + DELIM_URL_PREFIX;

    public final static String RESOURCE_LOC_PATH_META_INF = "META-INF";

    public final static ResourceLoader RESOURCE_LOADER_DEFAULT = new DefaultResourceLoader();
    public final static ResourcePatternResolver RESOURCE_PATTERN_RESOLVER_DEFAULT = getResourcePatternResolver();

    public static List<String> getOverrideableLocations(Collection<String> resourceLocs) {
        return getOverrideableLocations(resourceLocs, PREFIX_URL_CLASSPATH_ALL);
    }

    public static List<String> getOverrideableLocations(Collection<String> resourceLocs, String urlPrefixDefault) {
        return getOverrideableLocations(resourceLocs, urlPrefixDefault, RESOURCE_LOC_PATH_META_INF);
    }

    public static List<String> getOverrideableLocations(Collection<String> resourceLocs, String urlPrefixDefault, String baseResourcePath) {
        return getOverrideableLocations(resourceLocs, urlPrefixDefault, baseResourcePath, false);
    }

    public static List<String> getOverrideableLocations(Collection<String> resourceLocs, String urlPrefixDefault, String baseResourcePath,
        boolean includeResourceRawLocs) {
        List<String> overrideableResourceLocs = new ArrayList<>(resourceLocs.size() * (includeResourceRawLocs ? 4 : 2));

        for (String resourceLoc : resourceLocs) {
            overrideableResourceLocs.addAll(getOverrideableLocations(resourceLoc, urlPrefixDefault, baseResourcePath, includeResourceRawLocs));
        }

        return overrideableResourceLocs;
    }

    public static List<String> getOverrideableLocations(String resourceLoc) {
        return getOverrideableLocations(resourceLoc, PREFIX_URL_CLASSPATH_ALL);
    }

    public static List<String> getOverrideableLocations(String resourceLoc, String urlPrefixDefault) {
        return getOverrideableLocations(resourceLoc, urlPrefixDefault, RESOURCE_LOC_PATH_META_INF);
    }

    public static List<String> getOverrideableLocations(String resourceLoc, String urlPrefixDefault, String baseResourcePath) {
        return getOverrideableLocations(resourceLoc, urlPrefixDefault, baseResourcePath, false);
    }

    public static List<String> getOverrideableLocations(String resourceLoc, String urlPrefixDefault, String baseResourcePath, boolean includeResourceRawLocs) {
        String[] resourceLocParts =
            resourceLoc.contains(DELIM_URL_PREFIX) ? resourceLoc.split(DELIM_URL_PREFIX, 2) : ArrayUtils.toArray(urlPrefixDefault, resourceLoc);
        String resourceLocBaseRaw = baseResourcePath + SystemUtils.FILE_SEPARATOR + resourceLocParts[1], resourceLocRaw = resourceLocParts[1];
        List<String> resourceLocs =
            ToolArrayUtils.asList(ToolStringUtils.joinDelimit(ToolArrayUtils.asList(resourceLocParts[0], resourceLocBaseRaw), DELIM_URL_PREFIX),
                ToolStringUtils.joinDelimit(ToolArrayUtils.asList(resourceLocParts[0], resourceLocRaw), DELIM_URL_PREFIX));

        if (includeResourceRawLocs) {
            resourceLocs.add(1, resourceLocBaseRaw);
            resourceLocs.add(resourceLocRaw);
        }

        return resourceLocs;
    }

    public static Map<String, List<Resource>> mapLocations(List<String> resourceLocs) {
        return mapLocations(RESOURCE_PATTERN_RESOLVER_DEFAULT, resourceLocs);
    }

    public static Map<String, List<Resource>> mapLocations(ResourcePatternResolver resourcePatternResolver, List<String> resourceLocs) {
        Map<String, List<Resource>> resourceLocsMap = new LinkedHashMap<>(resourceLocs.size());
        List<Resource> resources;

        for (String resourceLoc : resourceLocs) {
            resources = resolveLocations(resourcePatternResolver, resourceLoc);

            if (resources != null) {
                resourceLocsMap.put(resourceLoc, resources);
            }
        }

        return resourceLocsMap;
    }

    @Nullable
    public static Resource resolveLocation(String resourceLoc) {
        return resolveLocation(RESOURCE_PATTERN_RESOLVER_DEFAULT, resourceLoc);
    }

    @Nullable
    public static Resource resolveLocation(ResourcePatternResolver resourcePatternResolver, String resourceLoc) {
        return ToolListUtils.getFirst(resolveLocations(resourcePatternResolver, resourceLoc));
    }

    public static List<Resource> resolveLocations(String resourceLoc) {
        return resolveLocations(RESOURCE_PATTERN_RESOLVER_DEFAULT, resourceLoc);
    }

    public static List<Resource> resolveLocations(ResourcePatternResolver resourcePatternResolver, String resourceLoc) {
        try {
            return ToolArrayUtils.asList(resourcePatternResolver.getResources(resourceLoc));
        } catch (IOException ignored) {
        }

        return null;
    }

    public static ResourcePatternResolver getResourcePatternResolver() {
        return getResourcePatternResolver(null);
    }

    public static ResourcePatternResolver getResourcePatternResolver(@Nullable ResourceLoader resourceLoader) {
        return ResourcePatternUtils.getResourcePatternResolver(ObjectUtils.defaultIfNull(resourceLoader, RESOURCE_LOADER_DEFAULT));
    }

    public static List<String> splitLocations(String resourceLocsStr) {
        return Arrays.stream(StringUtils.split(resourceLocsStr, DELIMS_RESOURCE_LOC)).filter(resourceLoc -> !StringUtils.isBlank(resourceLoc.trim())).map(
            String::trim).collect(Collectors.toList());
    }

    @Nullable
    public static File getFile(String resourceLoc) {
        return getFile(RESOURCE_PATTERN_RESOLVER_DEFAULT, resourceLoc);
    }

    @Nullable
    public static File getFile(ResourcePatternResolver resourcePatternResolver, String resourceLoc) {
        for (Resource resource : resolveLocations(resourcePatternResolver, resourceLoc)) {
            try {
                return resource.getFile();
            } catch (IOException ignored) {
            }
        }

        return null;
    }

    @Nullable
    public static InputStream getInputStream(String resourceLoc) {
        return getInputStream(RESOURCE_PATTERN_RESOLVER_DEFAULT, resourceLoc);
    }

    @Nullable
    public static InputStream getInputStream(ResourcePatternResolver resourcePatternResolver, String resourceLoc) {
        for (Resource resource : resolveLocations(resourcePatternResolver, resourceLoc)) {
            try {
                return resource.getInputStream();
            } catch (IOException ignored) {
            }
        }

        return null;
    }
}
