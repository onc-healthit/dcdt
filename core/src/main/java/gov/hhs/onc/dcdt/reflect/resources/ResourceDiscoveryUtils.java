package gov.hhs.onc.dcdt.reflect.resources;

import gov.hhs.onc.dcdt.lang.IterableUtils;
import gov.hhs.onc.dcdt.reflect.ClassLoaderList;
import gov.hhs.onc.dcdt.reflect.ClassLoaderUtils;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.discovery.Resource;
import org.apache.commons.discovery.resource.ClassLoaders;
import org.apache.commons.discovery.resource.DiscoverResources;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public abstract class ResourceDiscoveryUtils
{
	private final static Logger LOGGER = Logger.getLogger(ResourceDiscoveryUtils.class);
	
	public static InputStream getStream(String resourceName, ClassLoader ... classLoaders)
	{
		Resource resource = findResource(resourceName, classLoaders);
		
		return (resource != null) ? resource.getResourceAsStream() : null;
	}
	
	public static boolean hasFile(String resourceName, ClassLoader ... classLoaders)
	{
		return getFile(resourceName, classLoaders) != null;
	}
	
	public static File getFile(String resourceName, ClassLoader ... classLoaders)
	{
		String resourceFilePath = getFilePath(resourceName, classLoaders);
		
		return !StringUtils.isBlank(resourceFilePath) ? new File(resourceFilePath) : null;
	}
	
	public static boolean hasFilePath(String resourceName, ClassLoader ... classLoaders)
	{
		return !StringUtils.isBlank(getFilePath(resourceName, classLoaders));
	}
	
	public static String getFilePath(String resourceName, ClassLoader ... classLoaders)
	{
		URL resourceUrl = getUrl(resourceName, classLoaders);
		
		return (resourceUrl != null) ? resourceUrl.getFile() : null;
	}
	
	public static boolean hasUrl(String resourceName, ClassLoader ... classLoaders)
	{
		return getUrl(resourceName, classLoaders) != null;
	}
	
	public static URL getUrl(String resourceName, ClassLoader ... classLoaders)
	{
		Resource resource = findResource(resourceName, classLoaders);
		
		return (resource != null) ? resource.getResource() : null;
	}
	
	public static boolean hasResource(String resourceName, ClassLoader ... classLoaders)
	{
		return findResource(resourceName, classLoaders) != null;
	}
	
	public static Resource findResource(String resourceName, ClassLoader ... classLoaders)
	{
		return findResource(resourceName, null, classLoaders);
	}
	
	public static Resource findResource(String resourceName, String resourcePath, ClassLoader ... classLoaders)
	{
		return findResource(resourceName, resourcePath, (resourcePath != null), classLoaders);
	}
	
	public static Resource findResource(String resourceName, String resourcePath, boolean inMetaInf, ClassLoader ... classLoaders)
	{
		Set<Resource> resources = findResources(resourceName, resourcePath, inMetaInf, classLoaders);
		
		return IterableUtils.getFirst(resources);
	}
	
	public static Map<String, Set<Resource>> findAllResources(String ... resourceNames)
	{
		Map<String, Set<Resource>> resourcesMap = new TreeMap<>();
		Set<Resource> resources;
		
		for (String resourceName : resourceNames)
		{
			resources = findResources(resourceName);
			
			if (!CollectionUtils.isEmpty(resources))
			{
				if (!resourcesMap.containsKey(resourceName))
				{
					resourcesMap.put(resourceName, resources);
				}
				else
				{
					resourcesMap.get(resourceName).addAll(resources);
				}
			}
		}
		
		return resourcesMap;
	}
	
	public static Set<Resource> findResources(String resourceName, String resourcePath, ClassLoader ... classLoaders)
	{
		return findResources(resourceName, resourcePath, (resourcePath != null), classLoaders);
	}
	
	public static Set<Resource> findResources(String resourceName, String resourcePath, boolean inMetaInf, ClassLoader ... classLoaders)
	{
		return findResources(ResourcePathUtils.buildPath(inMetaInf, false, ArrayUtils.toArray(resourcePath, resourceName)), classLoaders);
	}
	
	public static Set<Resource> findResources(String resourcePath, ClassLoader ... classLoaders)
	{
		Set<Resource> resources = new TreeSet<>(new ResourceComparatorBuilder());
		ClassLoaderList classLoadersList = ClassLoaderUtils.getLoaders(classLoaders);
		resources.addAll(Arrays.asList(Resource.toArray(getDiscoverResources(classLoadersList).findResources(resourcePath))));
		
		LOGGER.debug("Found " + resources.size() + " resource(s) for path (" + resourcePath + "): numClassLoader(s)=" + 
			classLoadersList.size());
		
		return resources;
	}
	
	public static DiscoverResources getDiscoverResources(ClassLoaders classLoadersObj)
	{
		return new DiscoverResources(classLoadersObj);
	}
}