package gov.hhs.onc.dcdt.beans;

import java.util.Map.Entry;

public class BeanAttrib implements Entry<String, Object>
{
	private String key;
	private Object value;
	private boolean inverse;
	
	public BeanAttrib()
	{
		this(null, null);
	}
	
	public BeanAttrib(String key, Object value)
	{
		this(key, value, false);
	}
	
	public BeanAttrib(String key, Object value, boolean inverse)
	{
		this.key = key;
		this.value = value;
		this.inverse = inverse;
	}

	public boolean isInverse()
	{
		return this.inverse;
	}

	public void setInverse(boolean inverse)
	{
		this.inverse = inverse;
	}

	@Override
	public String getKey()
	{
		return this.key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	@Override
	public Object getValue()
	{
		return this.value;
	}

	@Override
	public Object setValue(Object value)
	{
		return (this.value = value);
	}
}