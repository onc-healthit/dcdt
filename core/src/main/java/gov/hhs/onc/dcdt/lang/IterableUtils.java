package gov.hhs.onc.dcdt.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.IteratorUtils;

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
	
	public static <T> List<T> toList(Iterable<T> iterable)
	{
		List<T> list = new ArrayList<>();
		Iterator<T> iterator = getIterator(iterable);
		
		if (iterator != null)
		{
			while (iterator.hasNext())
			{
				list.add(iterator.next());
			}
		}
		
		return list;
	}
	
	public static <T> T getFirst(Iterable<T> iterable)
	{
		Iterator<T> iterator = getIterator(iterable);
		
		return ((iterator != null) && iterator.hasNext()) ? iterator.next() : null;
	}
	
	public static <T> T getLast(Iterable<T> iterable)
	{
		Iterator<T> iterator = getIterator(iterable);
		
		if ((iterator == null) || !iterator.hasNext())
		{
			return null;
		}
		
		T item = null;
		
		while (iterator.hasNext())
		{
			item = iterator.next();
		}
		
		return item;
	}
	
	public static <T> boolean isEmpty(Iterable<T> iterable)
	{
		Iterator<T> iterator = getIterator(iterable);
		
		return (iterator == null) || !iterator.hasNext();
	}
	
	public static <T> Iterable<T> asIterable(Iterator<T> iterator)
	{
		return (iterator != null) ? new IterableIterator<T>(iterator) : new ArrayList<T>();
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
}