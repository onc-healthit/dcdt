package gov.hhs.onc.dcdt.test.data.provider;

import java.util.Arrays;
import java.util.Iterator;

public class IterableDataProvider extends ToolDataProvider
{
	private Iterable<?> dataIterable;
	
	public IterableDataProvider(Object ... data)
	{
		this(Arrays.asList(data));	
	}
	
	public IterableDataProvider(Iterable<?> dataIterable)
	{
		this.dataIterable = dataIterable;
	}

	@Override
	protected Iterator<Iterable<?>> createData(int iteration)
	{
		return Arrays.asList(new Iterable<?>[]{ this.dataIterable }).iterator();
	}
}