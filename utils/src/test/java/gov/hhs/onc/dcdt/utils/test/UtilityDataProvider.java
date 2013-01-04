package gov.hhs.onc.dcdt.utils.test;

import java.util.Arrays;
import java.util.Iterator;

public abstract class UtilityDataProvider
{
	protected static Iterator<Object> createData(Object ... data)
	{
		// TODO: implement
		return new DataProviderIterator<>(Arrays.asList(new Object[][]{ data }).iterator());
	}
}