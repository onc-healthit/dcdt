package gov.hhs.onc.dcdt.utils.cert.generator;

import gov.hhs.onc.dcdt.utils.cli.CliOption;
import gov.hhs.onc.dcdt.utils.cli.validation.NotBlankValidator;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;

public enum CertGenCliOption implements CliOption
{
	DOMAIN
	(
		new DefaultOptionBuilder()
			.withDescription("Domain name to generate certificates for.")
			.withShortName("d")
			.withLongName("domain")
			.withArgument(new ArgumentBuilder()
				.withName("name")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.withRequired(true)
			.create(), 
		"domain"
	), 
	OUTPUT_FILE
	(
		new DefaultOptionBuilder()
			.withDescription("Path to the output archive file.")
			.withShortName("o")
			.withLongName("out")
			.withArgument(new ArgumentBuilder()
				.withName("path")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.create(), 
		"outputFile"
	);
	
	private Option option;
	private String attribName;
	
	CertGenCliOption(Option option)
	{
		this(option, null);
	}
	
	CertGenCliOption(Option option, String attribName)
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