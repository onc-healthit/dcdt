package gov.hhs.onc.dcdt.utils.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public enum UtilityCliOption implements CliOption
{
	HELP
	(
		OptionBuilder
			.withLongOpt("help")
			.withDescription("Print help information.")
			.create("h")
	);
	
	private Option option;
	
	UtilityCliOption(Option option)
	{
		this.option = option;
	}
	
	@Override
	public Option getOption()
	{
		return this.option;
	}
}