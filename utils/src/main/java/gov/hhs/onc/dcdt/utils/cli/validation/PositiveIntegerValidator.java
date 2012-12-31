package gov.hhs.onc.dcdt.utils.cli.validation;

import java.text.NumberFormat;
import org.apache.commons.cli2.validation.NumberValidator;

public class PositiveIntegerValidator extends NumberValidator
{
	public final static PositiveIntegerValidator INSTANCE = new PositiveIntegerValidator();
	
	public PositiveIntegerValidator()
	{
		super(NumberFormat.getIntegerInstance());
		
		this.setMinimum(1);
	}
}