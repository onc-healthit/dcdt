package gov.hhs.onc.dcdt.discovery.resource;

import java.util.Comparator;
import org.apache.commons.discovery.Resource;
import org.apache.commons.lang3.ObjectUtils;

public class ResourceComparator implements Comparator<Resource>
{
	public final static ResourceComparator INSTANCE = new ResourceComparator();
	
	@Override
	public int compare(Resource resource1, Resource resource2)
	{
		if ((resource1 == null) || (resource2 == null))
		{
			return 0;
		}
		
		int result;
		
		if ((result = ObjectUtils.compare(ObjectUtils.toString(resource1.getResource(), null), 
			ObjectUtils.toString(resource2.getResource(), null))) == 0)
		{
			if ((result = ObjectUtils.compare(resource1.getName(), resource2.getName())) == 0)
			{
				result = ObjectUtils.compare(ObjectUtils.toString(resource1.getClassLoader(), null), 
					ObjectUtils.toString(resource2.getClassLoader(), null));
			}
		}
		
		return result;
	}
}