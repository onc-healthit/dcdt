package gov.hhs.onc.dcdt.utils.certgen.cli;

import gov.hhs.onc.dcdt.utils.cli.CliOption;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public enum CertGenCliOption implements CliOption
{
	DOMAIN
	(
		OptionBuilder
			.withLongOpt("domain")
			.hasArgs()
			.withArgName("name")
			.withDescription("Domain name to generate certificates/keys for.")
			.create("d")
	), 
	OUTPUT_DIR
	(
		OptionBuilder
			.withLongOpt("out")
			.hasArgs()
			.withArgName("path")
			.withDescription("Path to the output directory to write certificates/keys to.")
			.create("o")
	);
	
	private Option option;
	
	CertGenCliOption(Option option)
	{
		this.option = option;
	}
	
	@Override
	public Option getOption()
	{
		return this.option;
	}
}