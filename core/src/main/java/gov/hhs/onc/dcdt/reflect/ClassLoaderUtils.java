package gov.hhs.onc.dcdt.reflect;

import gov.hhs.onc.dcdt.lang.builder.ComparatorBuilder;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.discovery.jdk.JDKHooks;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

public abstract class ClassLoaderUtils
{
	private static class ClassLoaderWrapper extends ClassLoader
	{
		public ClassLoaderWrapper(ClassLoader parent)
		{
			super(parent);
		}
		
		@Override
		protected Package getPackage(String name)
		{
			return super.getPackage(name);
		}

		@Override
		protected Package[] getPackages()
		{
			return super.getPackages();
		}
	}
	
	public static Map<ClassLoader, Set<Package>> getAllPackages(ClassLoader ... classLoaders)
	{
		Map<ClassLoader, Set<Package>> packagesMap = new TreeMap<>(new ComparatorBuilder<>(ClassLoader.class));
		
		for (ClassLoader classLoader : getLoaders(classLoaders))
		{
			packagesMap.put(classLoader, getPackages(classLoader));
		}
		
		return packagesMap;
	}
	
	public static Set<Package> getPackages(ClassLoader classLoader)
	{
		Set<Package> packages = new TreeSet<>(new ComparatorBuilder<>(Package.class));
		packages.addAll(Arrays.asList(new ClassLoaderWrapper(classLoader).getPackages()));
		
		return packages;
	}
	
	public static Package getPackage(String name, ClassLoader ... classLoaders)
	{
		Package pkg;
		
		for (ClassLoader classLoadersObjItem : getLoaders(classLoaders))
		{
			if ((pkg = new ClassLoaderWrapper(classLoadersObjItem).getPackage(name)) != null)
			{
				return pkg;
			}
		}
		
		return null;
	}
	
	public static boolean isContext(ClassLoader classLoader)
	{
		return ObjectUtils.equals(classLoader, JDKHooks.getJDKHooks().getThreadContextClassLoader());
	}
	
	public static ClassLoader getContext()
	{
		return Thread.currentThread().getContextClassLoader();
	}
	
	public static boolean isSystem(ClassLoader classLoader)
	{
		return ObjectUtils.equals(classLoader, JDKHooks.getJDKHooks().getSystemClassLoader());
	}
	
	public static ClassLoader getSystem()
	{
		return ClassLoader.getSystemClassLoader();
	}
	
	public static ClassLoader getRoot(ClassLoader classLoader)
	{
		while (hasParent(classLoader))
		{
			classLoader = classLoader.getParent();
		}
		
		return classLoader;
	}
	
	public static boolean hasParent(ClassLoader classLoader)
	{
		ClassLoader classLoaderParent = classLoader.getParent();
		
		return (classLoaderParent != null) && (classLoader != classLoaderParent);
	}
	
	public static boolean isAncestor(ClassLoader classLoader1, ClassLoader classLoader2)
	{
		return getLoaders(classLoader2).isAncestor(classLoader1);
	}
	
	public static ClassLoaderList getLoaders(ClassLoader... classLoaders)
	{
		if (!ArrayUtils.isEmpty(classLoaders))
		{
			ClassLoaderList classLoadersList = new ClassLoaderList();
			classLoadersList.addAll(Arrays.asList(classLoaders), true);
			
			return classLoadersList;
		}
		else
		{
			return ClassLoaderList.getAppLoaders(null, null, true);
		}
	}
}