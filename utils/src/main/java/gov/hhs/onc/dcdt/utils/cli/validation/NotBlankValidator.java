package gov.hhs.onc.dcdt.utils.cli.validation;

import java.util.List;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.apache.commons.cli2.validation.Validator;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class NotBlankValidator implements Validator
{
	public final static NotBlankValidator INSTANCE = new NotBlankValidator();
	
	@Override
	public void validate(List values) throws InvalidArgumentException
	{
		String valueStr;
		
		for (Object value : values)
		{
			valueStr = ObjectUtils.toString(value, null);
			
			if (StringUtils.isBlank(valueStr))
			{
				throw new InvalidArgumentException("Value is blank: " + valueStr);
			}
		}
	}
}