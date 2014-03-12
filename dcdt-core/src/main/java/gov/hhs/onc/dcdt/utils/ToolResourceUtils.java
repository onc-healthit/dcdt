package gov.hhs.onc.dcdt.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

public abstract class ToolResourceUtils {
    public final static String URL_PREFIX_DELIM = ":";
    public final static String CLASSPATH_ALL_URL_PREFIX = "classpath*" + URL_PREFIX_DELIM;

    public final static ResourceLoader RESOURCE_LOADER_DEFAULT = new DefaultResourceLoader();
    public final static ResourcePatternResolver RESOURCE_PATTERN_RESOLVER_DEFAULT = getResourcePatternResolver();

    public final static String RESOURCE_LOC_DELIMS = ",; \t\n";
    public final static String META_INF_RESOURCE_PATH = "META-INF";

    public static List<String> getOverrideableResourceLocations(Collection<String> resourceLocs) {
        return getOverrideableResourceLocations(resourceLocs, CLASSPATH_ALL_URL_PREFIX);
    }

    public static List<String> getOverrideableResourceLocations(Collection<String> resourceLocs, String urlPrefixDefault) {
        return getOverrideableResourceLocations(resourceLocs, urlPrefixDefault, META_INF_RESOURCE_PATH);
    }

    public static List<String> getOverrideableResourceLocations(Collection<String> resourceLocs, String urlPrefixDefault, String baseResourcePath) {
        return getOverrideableResourceLocations(resourceLocs, urlPrefixDefault, baseResourcePath, false);
    }

    public static List<String> getOverrideableResourceLocations(Collection<String> resourceLocs, String urlPrefixDefault, String baseResourcePath,
        boolean includeResourceRawLocs) {
        List<String> overrideableResourceLocs = new ArrayList<>(resourceLocs.size() * (includeResourceRawLocs ? 4 : 2));

        for (String resourceLoc : resourceLocs) {
            overrideableResourceLocs.addAll(getOverrideableResourceLocation(resourceLoc, urlPrefixDefault, baseResourcePath, includeResourceRawLocs));
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
        return getOverrideableResourceLocation(resourceLoc, urlPrefixDefault, baseResourcePath, false);
    }

    public static List<String> getOverrideableResourceLocation(String resourceLoc, String urlPrefixDefault, String baseResourcePath,
        boolean includeResourceRawLocs) {
        String[] resourceLocParts =
            resourceLoc.contains(URL_PREFIX_DELIM) ? resourceLoc.split(URL_PREFIX_DELIM, 2) : ArrayUtils.toArray(urlPrefixDefault, resourceLoc);
        String resourceLocBaseRaw = baseResourcePath + SystemUtils.FILE_SEPARATOR + resourceLocParts[1], resourceLocRaw = resourceLocParts[1];
        List<String> resourceLocs =
            ToolArrayUtils.asList(ToolStringUtils.joinDelimit(ToolArrayUtils.asList(resourceLocParts[0], resourceLocBaseRaw), URL_PREFIX_DELIM),
                ToolStringUtils.joinDelimit(ToolArrayUtils.asList(resourceLocParts[0], resourceLocRaw), URL_PREFIX_DELIM));

        if (includeResourceRawLocs) {
            resourceLocs.add(1, resourceLocBaseRaw);
            resourceLocs.add(resourceLocRaw);
        }

        return resourceLocs;
    }

    public static Map<String, List<Resource>> mapResourceLocations(List<String> resourceLocs) {
        return mapResourceLocations(resourceLocs, RESOURCE_PATTERN_RESOLVER_DEFAULT);
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
        return resolveResourceLocations(resourceLoc, RESOURCE_PATTERN_RESOLVER_DEFAULT);
    }

    public static List<Resource> resolveResourceLocations(String resourceLoc, ResourcePatternResolver resourcePatternResolver) {
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

    public static InputStream getResourceInputStream(String resourceLoc) {
        Resource resource = getFirstResource(resourceLoc);

        if (resource instanceof ClassPathResource || resource instanceof InputStreamResource) {
            try {
                return resource.getInputStream();
            } catch (IOException ignored) {
            }
        }

        return null;
    }

    public static Resource getFirstResource(String resourceLoc) {
        return ToolListUtils.getFirst(resolveResourceLocations(resourceLoc));
    }
}
