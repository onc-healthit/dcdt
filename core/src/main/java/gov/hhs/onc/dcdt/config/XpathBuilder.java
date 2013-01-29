package gov.hhs.onc.dcdt.config;

import gov.hhs.onc.dcdt.beans.BeanAttrib;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Builder;
import org.apache.commons.lang3.text.StrBuilder;

public class XpathBuilder implements Builder<String>
{
	private final static String XPATH_DELIM = "/";
	private final static String XPATH_ATTRIB_DELIM = ", ";
	private final static String XPATH_ATTRIB_EQUALS = "=";
	private final static String XPATH_ATTRIB_NOT_EQUALS = "!=";
	private final static String XPATH_ATTRIB_KEY_PREFIX = "@";
	private final static String XPATH_ATTRIB_VALUE_QUOTE = "\"";
	private final static String XPATH_SEARCH_PREFIX = "[";
	private final static String XPATH_SEARCH_SUFFIX = "]";
	
	private StrBuilder builder = new StrBuilder();
	
	public static String buildAttribNames(String ... attribNames)
	{
		BeanAttrib[] attribs = new BeanAttrib[attribNames.length];
		
		for (int a = 0; a < attribNames.length; a++)
		{
			attribs[a] = new BeanAttrib(attribNames[a]);
		}
		
		return new XpathBuilder().appendAttribs(false, attribs).build();
	}
	
	public XpathBuilder appendNodes(String ... nodeNames)
	{
		if (!ArrayUtils.isEmpty(nodeNames))
		{
			for (String nodeName : nodeNames)
			{
				if (!StringUtils.isBlank(nodeName))
				{
					builder.appendSeparator(XPATH_DELIM);
					builder.append(nodeName);
				}
			}
		}
		
		return this;
	}
	
	public XpathBuilder appendAttribs(BeanAttrib ... attribs)
	{
		return this.appendAttribs(true, attribs);
	}
	
	public XpathBuilder appendAttribs(boolean asSearch, BeanAttrib ... attribs)
	{
		if (!ArrayUtils.isEmpty(attribs))
		{
			if (asSearch)
			{
				builder.append(XPATH_SEARCH_PREFIX);
			}
			
			StrBuilder attribsBuilder = new StrBuilder();
			
			for (BeanAttrib attrib : attribs)
			{
				attribsBuilder.appendSeparator(XPATH_ATTRIB_DELIM);
				attribsBuilder.append(XPATH_ATTRIB_KEY_PREFIX);
				attribsBuilder.append(attrib.getKey());
				
				if (asSearch)
				{
					attribsBuilder.append(attrib.isInverse() ? XPATH_ATTRIB_NOT_EQUALS : XPATH_ATTRIB_EQUALS);
					attribsBuilder.append(XPATH_ATTRIB_VALUE_QUOTE);
					attribsBuilder.append(attrib.getValue());
					attribsBuilder.append(XPATH_ATTRIB_VALUE_QUOTE);
				}
			}
			
			builder.append(attribsBuilder);
			
			if (asSearch)
			{
				builder.append(XPATH_SEARCH_SUFFIX);
			}
		}
		
		return this;
	}

	@Override
	public String build()
	{
		return this.builder.toString();
	}
	
	@Override
	public String toString()
	{
		return this.build();
	}
}