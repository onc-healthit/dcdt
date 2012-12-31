package gov.hhs.onc.dcdt.utils.cli;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;

public enum UtilityCliOption implements CliOption
{
	HELP
	(
		new DefaultOptionBuilder()
			.withDescription("Print help information.")
			.withLongName("help")
			.create()
	);
	
	private Option option;
	private String attribName;
	
	UtilityCliOption(Option option)
	{
		this(option, null);
	}
	
	UtilityCliOption(Option option, String attribName)
	{
		this.option = option;
		this.attribName = attribName;
	}
	
	@Override
	public Option getOption()
	{
		return this.option;
	}
	
	@Override
	public String getAttribName()
	{
		return this.attribName;
	}
}