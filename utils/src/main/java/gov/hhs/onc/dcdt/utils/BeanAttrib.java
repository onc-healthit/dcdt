package gov.hhs.onc.dcdt.utils;

import java.util.Map.Entry;

public class BeanAttrib implements Entry<String, Object>
{
	private String key;
	private Object value;
	
	public BeanAttrib()
	{
	}
	
	public BeanAttrib(String key, Object value)
	{
		this.key = key;
		this.value = value;
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