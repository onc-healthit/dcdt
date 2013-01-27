package gov.hhs.onc.dcdt.reflect.resources;

import gov.hhs.onc.dcdt.lang.builder.ComparatorBuilder;
import java.net.URL;
import org.apache.commons.discovery.Resource;

public class ResourceComparatorBuilder extends ComparatorBuilder<Resource>
{
	public ResourceComparatorBuilder()
	{
		super(Resource.class);
	}

	@Override
	protected boolean appendObjsCompare(Resource obj1, Resource obj2)
	{
		return this.appendObjCompare(URL.class, obj1.getResource(), obj2.getResource()) || 
			this.appendObjCompare(String.class, obj1.getName(), obj2.getName()) || 
			this.appendObjCompare(ClassLoader.class, obj1.getClassLoader(), obj2.getClassLoader());
	}
}