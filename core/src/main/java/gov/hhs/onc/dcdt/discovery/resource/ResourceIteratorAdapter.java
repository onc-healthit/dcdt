package gov.hhs.onc.dcdt.discovery.resource;

import java.util.Iterator;
import org.apache.commons.discovery.Resource;
import org.apache.commons.discovery.ResourceIterator;

public class ResourceIteratorAdapter implements Iterator<Resource>
{
	private ResourceIterator resourceIterator;
	
	public ResourceIteratorAdapter(ResourceIterator resourceIterator)
	{
		this.resourceIterator = resourceIterator;
	}

	@Override
	public boolean hasNext()
	{
		return this.resourceIterator.hasNext();
	}

	@Override
	public Resource next()
	{
		return this.resourceIterator.nextResource();
	}

	@Override
	public void remove()
	{
	}
}