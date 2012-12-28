package gov.hhs.onc.dcdt.utils.configgen.cli;

import gov.hhs.onc.dcdt.utils.cli.CliOption;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public enum ConfigGenCliOption implements CliOption
{
	DOMAIN
	(
		OptionBuilder
			.withLongOpt("domain")
			.hasArgs()
			.withArgName("name")
			.withDescription("Domain name to generate certificates for.")
			.create("d")
	), 
	INPUT_DIR
	(
		OptionBuilder
			.withLongOpt("in")
			.hasArgs()
			.withArgName("path")
			.withDescription("Path to the input directory to read certificates/keys from.")
			.create("i")
	), 
	OUTPUT_FILE
	(
		OptionBuilder
			.withLongOpt("out")
			.hasArgs()
			.withArgName("path")
			.withDescription("Path to the output archive file.")
			.create("o")
	);
	
	private Option option;
	
	ConfigGenCliOption(Option option)
	{
		this.option = option;
	}
	
	@Override
	public Option getOption()
	{
		return this.option;
	}
}