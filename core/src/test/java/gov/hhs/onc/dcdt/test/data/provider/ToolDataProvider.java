package gov.hhs.onc.dcdt.test.data.provider;

import java.util.Iterator;

public abstract class ToolDataProvider
{
	protected int iteration = -1;
	
	protected Iterator<Object[]> createNextData()
	{
		return this.createDataIterator(this.createData(++this.iteration));
	}
	
	protected Iterator<Object[]> createDataIterator(Iterator<Iterable<?>> data)
	{
		return new DataProviderIterator(data);
	}
	
	protected abstract Iterator<Iterable<?>> createData(int iteration);
}