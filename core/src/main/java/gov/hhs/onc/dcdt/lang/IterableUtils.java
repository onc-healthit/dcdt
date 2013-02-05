package gov.hhs.onc.dcdt.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.ClassUtils;

public abstract class IterableUtils
{
	private static class IterableIterator<T> implements Iterable<T>
	{
		private Iterator<T> iterator;
		
		public IterableIterator(Iterator<T> iterator)
		{
			this.iterator = iterator;
		}

		@Override
		public Iterator<T> iterator()
		{
			return this.iterator;
		}
	}
	
	public static <T> T[] toArray(Iterable<T> iterable, T[] arr)
	{
		return toList(iterable).toArray(arr);
	}
	
	public static <T> Object[] toArray(Iterable<T> iterable)
	{
		return IteratorUtils.toArray(getIterator(iterable));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getFirst(Iterable<T> iterable)
	{
		int firstIndex = getFirstIndex(iterable);
		
		return (firstIndex >= 0) ? (T)CollectionUtils.get(iterable, firstIndex) : null;
	}
	
	public static <T> int getFirstIndex(Iterable<T> iterable)
	{
		return !isEmpty(iterable) ? 0 : -1;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getLast(Iterable<T> iterable)
	{
		int lastIndex = getLastIndex(iterable);
		
		return (lastIndex >= 0) ? (T)CollectionUtils.get(iterable, lastIndex) : null; 
	}
	
	public static <T> int getLastIndex(Iterable<T> iterable)
	{
		return size(iterable) - 1; 
	}
	
	public static <T> int size(Iterable<T> iterable)
	{
		return (iterable != null) ? CollectionUtils.size(iterable) : 0;
	}
	
	public static <T> boolean isEmpty(Iterable<T> iterable)
	{
		return (iterable == null) || CollectionUtils.sizeIsEmpty(iterable);
	}
	
	public static <T> Collection<T> toCollection(Iterable<T> iterable)
	{
		return isCollection(iterable) ? (Collection<T>)iterable : toList(iterable);
	}
	
	public static <T> List<T> toList(Iterable<T> iterable)
	{
		if (iterable == null)
		{
			return new ArrayList<>();
		}
		if (isList(iterable))
		{
			return (List<T>)iterable;
		}
		else if (isCollection(iterable))
		{
			return new ArrayList<>((Collection<T>)iterable);
		}
		else
		{
			List<T> list = new ArrayList<>();
			Iterator<T> iterator = getIterator(iterable);
			
			if (iterator != null)
			{
				CollectionUtils.addAll(list, iterator);
			}
			
			return list;
		}
	}
	
	public static <T> Iterable<T> asIterable(Iterator<T> iterator)
	{
		return (iterator != null) ? new IterableIterator<>(iterator) : new ArrayList<T>();
	}
	
	@SafeVarargs
	public static <T> Iterable<T> asIterable(T ... objs)
	{
		return (objs != null) ? Arrays.asList(objs) : new ArrayList<T>();
	}
	
	public static <T> Iterator<T> getIterator(Iterable<T> iterable)
	{
		return (iterable != null) ? iterable.iterator() : null;
	}
	
	public static <T> boolean isCollection(Iterable<T> iterable)
	{
		return isAssignable(iterable, Collection.class);
	}
	
	public static <T> boolean isList(Iterable<T> iterable)
	{
		return isAssignable(iterable, List.class);
	}
	
	public static <T> boolean isAssignable(Iterable<T> iterable, Class<?> ... toClass)
	{
		return ClassUtils.isAssignable(ClassUtils.toClass(iterable), toClass);
	}
}