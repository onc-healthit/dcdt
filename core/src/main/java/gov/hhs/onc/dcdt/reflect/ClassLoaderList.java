package gov.hhs.onc.dcdt.reflect;

import gov.hhs.onc.dcdt.lang.IterableUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.discovery.resource.ClassLoaders;

public class ClassLoaderList extends ClassLoaders implements List<ClassLoader>
{
	private static class ClassLoadersIterator implements Iterator<ClassLoader>
	{
		private ClassLoaders delegate;
		private int index = 0;
		
		public ClassLoadersIterator(ClassLoaders delegate)
		{
			this.delegate = delegate;
		}
		
		@Override
		public boolean hasNext()
		{
			return (this.delegate != null) && (this.index < this.delegate.size());
		}

		@Override
		public ClassLoader next()
		{
			return this.hasNext() ? this.delegate.get(this.index++) : null;
		}

		@Override
		public void remove()
		{
		}
	}
	
	public ClassLoaderList()
	{
		super();
	}
	
	public ClassLoaderList(ClassLoaders delegate)
	{
		this(IterableUtils.asIterable(new ClassLoadersIterator(delegate)));
	}
	
	public ClassLoaderList(ClassLoader ... classLoaders)
	{
		this(IterableUtils.asIterable(classLoaders));
	}
	
	public ClassLoaderList(Iterable<ClassLoader> classLoaders)
	{
		super();
		
		this.addAll(IterableUtils.toList(classLoaders));
	}
	
	public static ClassLoaderList getAppLoaders(Class<?> spi, Class<?> factory)
	{
		return getAppLoaders(spi, factory, false);
	}
	
	public static ClassLoaderList getAppLoaders(Class<?> spi, Class<?> factory, boolean prune)
	{
		return new ClassLoaderList(ClassLoaders.getAppLoaders(spi, factory, prune));
	}
	
	public static ClassLoaderList getLibLoaders(Class<?> spi, Class<?> factory)
	{
		return getLibLoaders(spi, factory, false);
	}
	
	public static ClassLoaderList getLibLoaders(Class<?> spi, Class<?> factory, boolean prune)
	{
		return new ClassLoaderList(ClassLoaders.getLibLoaders(spi, factory, prune));
	}
	
	@Override
	public boolean add(ClassLoader classLoaderAdd)
	{
		return this.addAll(-1, Arrays.asList(classLoaderAdd));
	}

	@Override
	public void add(int index, ClassLoader classLoaderAdd)
	{
		this.add(index, classLoaderAdd, false);
	}
	
	public void add(int index, ClassLoader classLoaderAdd, boolean prune)
	{
		this.addAll(index, Arrays.asList(classLoaderAdd), prune);
	}

	@Override
	public boolean addAll(Collection<? extends ClassLoader> classLoadersAdd)
	{
		return this.addAll(-1, classLoadersAdd);
	}
	
	public boolean addAll(Collection<? extends ClassLoader> classLoadersAdd, boolean prune)
	{
		return this.addAll(-1, classLoadersAdd, prune);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends ClassLoader> classLoadersAdd)
	{
		return this.addAll(index, classLoadersAdd, false);
	}
	
	public boolean addAll(int index, Collection<? extends ClassLoader> classLoadersAdd, boolean prune)
	{
		Iterator<? extends ClassLoader> classLoadersAddIter = classLoadersAdd.iterator();
		ClassLoader classLoaderAdd;
		
		while (classLoadersAddIter.hasNext())
		{
			classLoaderAdd = classLoadersAddIter.next();
			
			if (prune && this.isAncestor(classLoaderAdd))
			{
				classLoadersAddIter.remove();
			}
		}
		
		return ((index >= 0) && (index <= this.classLoaders.size())) ? 
			this.classLoaders.addAll(index, classLoadersAdd) : 
			this.classLoaders.addAll(classLoadersAdd);
	}

	//<editor-fold description="Directly delegated List<ClassLoader> methods">
	@Override
	public void clear()
	{
		this.classLoaders.clear();
	}

	@Override
	public boolean contains(Object o)
	{
		return this.classLoaders.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return this.classLoaders.containsAll(c);
	}

	@Override
	public ClassLoader get(int index)
	{
		return this.classLoaders.get(index);
	}

	@Override
	public int indexOf(Object o)
	{
		return this.classLoaders.indexOf(o);
	}

	@Override
	public boolean isEmpty()
	{
		return this.classLoaders.isEmpty();
	}

	@Override
	public Iterator<ClassLoader> iterator()
	{
		return this.classLoaders.iterator();
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return this.classLoaders.lastIndexOf(o);
	}

	@Override
	public ListIterator<ClassLoader> listIterator()
	{
		return this.classLoaders.listIterator();
	}

	@Override
	public ListIterator<ClassLoader> listIterator(int index)
	{
		return this.classLoaders.listIterator(index);
	}

	@Override
	public ClassLoader remove(int index)
	{
		return this.classLoaders.remove(index);
	}

	@Override
	public boolean remove(Object o)
	{
		return this.classLoaders.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		return this.classLoaders.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return this.classLoaders.retainAll(c);
	}

	@Override
	public ClassLoader set(int index, ClassLoader element)
	{
		return this.classLoaders.set(index, element);
	}

	@Override
	public int size()
	{
		return this.classLoaders.size();
	}

	@Override
	public List<ClassLoader> subList(int fromIndex, int toIndex)
	{
		return this.classLoaders.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray()
	{
		return this.classLoaders.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return this.classLoaders.toArray(a);
	}
	//</editor-fold>
	
	public List<ClassLoader> getClassLoaders()
	{
		return this.classLoaders;
	}
}