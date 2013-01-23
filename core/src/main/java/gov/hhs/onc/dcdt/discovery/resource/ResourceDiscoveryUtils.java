package gov.hhs.onc.dcdt.discovery.resource;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.discovery.Resource;
import org.apache.commons.discovery.resource.ClassLoaders;
import org.apache.commons.discovery.resource.DiscoverResources;
import org.apache.commons.lang3.StringUtils;

public abstract class ResourceDiscoveryUtils
{	
	public static InputStream getStream(String resourceName)
	{
		Resource resource = findResource(resourceName);
		
		return (resource != null) ? resource.getResourceAsStream() : null;
	}
	
	public static boolean hasFile(String resourceName)
	{
		return getFile(resourceName) != null;
	}
	
	public static File getFile(String resourceName)
	{
		String resourceFilePath = getFilePath(resourceName);
		
		return !StringUtils.isBlank(resourceFilePath) ? new File(resourceFilePath) : null;
	}
	
	public static boolean hasFilePath(String resourceName)
	{
		return !StringUtils.isBlank(getFilePath(resourceName));
	}
	
	public static String getFilePath(String resourceName)
	{
		URL resourceUrl = getUrl(resourceName);
		
		return (resourceUrl != null) ? resourceUrl.getFile() : null;
	}
	
	public static boolean hasUrl(String resourceName)
	{
		return getUrl(resourceName) != null;
	}
	
	public static URL getUrl(String resourceName)
	{
		Resource resource = findResource(resourceName);
		
		return (resource != null) ? resource.getResource() : null;
	}
	
	public static boolean hasResource(String resourceName)
	{
		return findResource(resourceName) != null;
	}
	
	public static Resource findResource(String resourceName)
	{
		Set<Resource> resources = findResources(resourceName);
		
		return !CollectionUtils.isEmpty(resources) ? resources.iterator().next() : null;
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
	
	@SuppressWarnings("unchecked")
	public static Set<Resource> findResources(String resourceName)
	{
		Set<Resource> resources = new TreeSet<>(ResourceComparator.INSTANCE);
		resources.addAll(IteratorUtils.toList(new ResourceIteratorAdapter(
			getDiscoverResources().findResources(resourceName))));
		
		return resources;
	}
	
	public static DiscoverResources getDiscoverResources(ClassLoader ... classLoaders)
	{
		return new DiscoverResources(getClassLoaders(classLoaders));
	}
	
	public static ClassLoaders getClassLoaders(ClassLoader ... classLoaders)
	{
		ClassLoaders classLoadersObj;
		
		if (classLoaders != null)
		{
			classLoadersObj = new ClassLoaders();
			
			for (ClassLoader classLoader : classLoaders)
			{
				classLoadersObj.put(classLoader, true);
			}
		}
		else
		{
			classLoadersObj = ClassLoaders.getAppLoaders(null, ResourceDiscoveryUtils.class, true);
		}
		
		return classLoadersObj;
	}
}