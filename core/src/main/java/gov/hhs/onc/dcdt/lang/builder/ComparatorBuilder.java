package gov.hhs.onc.dcdt.lang.builder;

import java.util.Comparator;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

public class ComparatorBuilder<T> extends CompareToBuilder implements Comparator<T>
{
	protected Class<T> objClass;
	
	public ComparatorBuilder(Class<T> objClass)
	{
		this.objClass = objClass;
	}
	
	public <U> boolean appendObjCompare(Class<U> objCompClass, U objComp1, U objComp2)
	{
		return (Comparable.class.isAssignableFrom(objCompClass) ? this.append(objComp1, objComp2) : 
			this.append(ObjectUtils.toString(objComp1, null), ObjectUtils.toString(objComp2, null)))
			.toComparison() != 0;
	}
	
	@Override
	public int compare(T obj1, T obj2)
	{
		if ((obj1 != null) && (obj2 != null))
		{
			this.appendObjsCompare(obj1, obj2);
		}
		
		return this.toComparison();
	}
	
	protected boolean appendObjsCompare(T obj1, T obj2)
	{
		return this.appendObjCompare(this.objClass, obj1, obj2);
	}
}