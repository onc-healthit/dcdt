package gov.hhs.onc.dcdt.test.data.provider;

import java.util.Arrays;
import java.util.Iterator;
import org.apache.commons.collections.IteratorUtils;

public class DataProviderIterator implements Iterator<Object[]>
{
	protected Iterator<Iterable<?>> dataIterator;
	
	public DataProviderIterator(Object ... data)
	{
		this(Arrays.asList(data));
	}
	
	public DataProviderIterator(Iterable<?> dataIterable)
	{
		this(Arrays.asList(new Iterable<?>[]{ dataIterable }).iterator());
	}
	
	public DataProviderIterator(Iterator<Iterable<?>> dataIterator)
	{
		this.dataIterator = dataIterator;
	}

	@Override
	public boolean hasNext()
	{
		return dataIterator.hasNext();
	}

	@Override
	public Object[] next()
	{
		return IteratorUtils.toArray(this.dataIterator.next().iterator());
	}

	@Override
	public void remove()
	{
		this.dataIterator.remove();
	}
}