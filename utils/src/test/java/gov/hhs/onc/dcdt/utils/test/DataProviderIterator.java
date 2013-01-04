package gov.hhs.onc.dcdt.utils.test;

import java.util.Iterator;

public class DataProviderIterator<T> implements Iterator<Object>
{
	private Iterator<T> iterator;
	
	public DataProviderIterator(Iterator<T> iterator)
	{
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext()
	{
		return iterator.hasNext();
	}

	@Override
	public Object next()
	{
		return this.iterator.next();
	}

	@Override
	public void remove()
	{
		this.iterator.remove();
	}
}