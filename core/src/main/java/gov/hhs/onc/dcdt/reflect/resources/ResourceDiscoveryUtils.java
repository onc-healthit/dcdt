package gov.hhs.onc.dcdt.reflect.resources;

import gov.hhs.onc.dcdt.lang.IterableUtils;
import gov.hhs.onc.dcdt.reflect.ClassLoaderList;
import gov.hhs.onc.dcdt.reflect.ClassLoaderUtils;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.discovery.Resource;
import org.apache.commons.discovery.resource.ClassLoaders;
import org.apache.commons.discovery.resource.DiscoverResources;
import org.apache.log4j.Logger;

public abstract class ResourceDiscoveryUtils
{
	private final static Logger LOGGER = Logger.getLogger(ResourceDiscoveryUtils.class);
	
	public static boolean hasResource(boolean inMetaInf, boolean isToolResource, Object ... resourcePathParts)
	{
		return hasResource(inMetaInf, isToolResource, IterableUtils.asIterable(resourcePathParts));
	}
	
	public static boolean hasResource(boolean inMetaInf, boolean isToolResource, Iterable<Object> resourcePathParts)
	{
		return findResource(inMetaInf, isToolResource, resourcePathParts) != null;
	}
	
	public static Resource findResource(boolean inMetaInf, boolean isToolResource, Object ... resourcePathParts)
	{
		return findResource(inMetaInf, isToolResource, IterableUtils.asIterable(resourcePathParts));
	}
	
	public static Resource findResource(boolean inMetaInf, boolean isToolResource, Iterable<Object> resourcePathParts)
	{
		Set<Resource> resources = findResources(inMetaInf, isToolResource, resourcePathParts);
		
		return IterableUtils.getFirst(resources);
	}
	
	// TODO: refactor to match new method arguments
	/*
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
	*/
	
	public static Set<Resource> findResources(boolean inMetaInf, boolean isToolResource, Object ... resourcePathParts)
	{
		return findResources(inMetaInf, isToolResource, IterableUtils.asIterable(resourcePathParts));
	}
	
	public static Set<Resource> findResources(boolean inMetaInf, boolean isToolResource, Iterable<Object> resourcePathParts)
	{
		return findResources(ResourcePathUtils.buildPath(inMetaInf, isToolResource, resourcePathParts));
	}
	
	public static Set<Resource> findResources(String resourcePath)
	{
		Set<Resource> resources = new TreeSet<>(new ResourceComparatorBuilder());
		ClassLoaderList classLoadersList = ClassLoaderUtils.getLoaders();
		resources.addAll(Arrays.asList(Resource.toArray(getDiscoverResources(classLoadersList).findResources(resourcePath))));
		
		LOGGER.trace("Found " + resources.size() + " resource(s) for path (" + resourcePath + "): numClassLoader(s)=" + 
			classLoadersList.size());
		
		return resources;
	}
	
	public static DiscoverResources getDiscoverResources(ClassLoaders classLoadersObj)
	{
		return new DiscoverResources(classLoadersObj);
	}
}