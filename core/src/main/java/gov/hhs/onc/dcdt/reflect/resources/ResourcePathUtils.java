package gov.hhs.onc.dcdt.reflect.resources;

import gov.hhs.onc.dcdt.lang.IterableUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.log4j.Logger;

public abstract class ResourcePathUtils
{
	public final static String RESOURCE_PATH_DELIM = "/";
	public final static String META_INF_RESOURCE_PATH_PART = "META-INF";
	public final static String TOOL_RESOURCE_PATH_PART = "dcdt";
	
	private final static String RESOURCE_PATH_PART_REMOVE_ENDS_PATTERN = "(^[\\s" + RESOURCE_PATH_DELIM + 
		"]+|[\\s" + RESOURCE_PATH_DELIM + "]+$)";
	
	private final static Logger LOGGER = Logger.getLogger(ResourcePathUtils.class);
	
	public static String buildPath(boolean inMetaInf, boolean isToolResource, Object ... resourcePathParts)
	{
		return buildPath(inMetaInf, isToolResource, IterableUtils.asIterable(resourcePathParts));
	}
	
	public static String buildPath(boolean inMetaInf, boolean isToolResource, Iterable<Object> resourcePathParts)
	{
		List<Object> resourcePathPartsList = new ArrayList<>();
		
		if (inMetaInf)
		{
			resourcePathPartsList.add(META_INF_RESOURCE_PATH_PART);
		}
		
		if (isToolResource)
		{
			resourcePathPartsList.add(TOOL_RESOURCE_PATH_PART);
		}
		
		resourcePathPartsList.addAll(IterableUtils.toList(resourcePathParts));
		
		return buildPath(resourcePathPartsList);
	}
	
	public static String buildPath(Object ... resourcePathParts)
	{
		return buildPath(IterableUtils.asIterable(resourcePathParts));
	}
	
	public static String buildPath(Iterable<Object> resourcePathParts)
	{
		StrBuilder resourcePathBuilder = new StrBuilder();
		Class<?> resourcePathPartClass;
		String resourcePathPartStr;
		
		for (Object resourcePathPart : resourcePathParts)
		{
			if (resourcePathPart != null)
			{
				resourcePathPartClass = resourcePathPart.getClass();
				
				if (Class.class.isAssignableFrom(resourcePathPartClass))
				{
					resourcePathPart = ((Class<?>)resourcePathPart).getPackage();
					resourcePathPartClass = resourcePathPart.getClass();
				}
				
				if (Package.class.isAssignableFrom(resourcePathPartClass))
				{
					resourcePathPart = packageToPath((Package)resourcePathPart);
				}
				
				resourcePathPartStr = formatPathPart(ObjectUtils.toString(resourcePathPart, null));
				
				if (!StringUtils.isBlank(resourcePathPartStr))
				{
					resourcePathBuilder.appendSeparator(RESOURCE_PATH_DELIM);
					resourcePathBuilder.append(resourcePathPartStr);
				}
			}
		}
		
		return resourcePathBuilder.toString();
	}
	
	public static String formatPathPart(String resourcePathPart)
	{
		return !StringUtils.isBlank(resourcePathPart) ? resourcePathPart.replaceAll(
			RESOURCE_PATH_PART_REMOVE_ENDS_PATTERN, StringUtils.EMPTY) : resourcePathPart;
	}
	
	public static String[] splitPathParts(String resourcePath)
	{
		return StringUtils.split(resourcePath, RESOURCE_PATH_DELIM);
	}
	
	public static String packageToPath(Package pkg)
	{
		return packageToPath(((pkg != null) ? pkg.getName() : null));
	}
	
	public static String packageToPath(String pkgName)
	{
		return StringUtils.join(StringUtils.split(pkgName, ClassUtils.PACKAGE_SEPARATOR_CHAR), RESOURCE_PATH_DELIM);
	}
}