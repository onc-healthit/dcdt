package gov.hhs.onc.dcdt.ldap.filter;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.directory.api.ldap.model.entry.StringValue;
import org.apache.directory.api.ldap.model.filter.EqualityNode;
import org.apache.directory.api.ldap.model.schema.AttributeType;

public class StringEqualityNode extends EqualityNode<String>
{
	public StringEqualityNode(String attribute, Object value)
	{
		super(attribute, new StringValue(ObjectUtils.toString(value)));
	}

	public StringEqualityNode(AttributeType attributeType, Object value)
	{
		super(attributeType, new StringValue(ObjectUtils.toString(value)));
	}
}