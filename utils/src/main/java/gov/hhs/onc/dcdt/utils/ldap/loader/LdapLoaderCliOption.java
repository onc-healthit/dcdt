package gov.hhs.onc.dcdt.utils.ldap.loader;

import gov.hhs.onc.dcdt.utils.cli.CliOption;
import gov.hhs.onc.dcdt.utils.cli.validation.NotBlankValidator;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;

public enum LdapLoaderCliOption implements CliOption
{
	DOMAIN
	(
		new DefaultOptionBuilder()
			.withDescription("Domain name to load LDAP data for.")
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
	INPUT_DIR
	(
		new DefaultOptionBuilder()
			.withDescription("Path to the input directory to read certificates/keys from.")
			.withLongName("in")
			.withShortName("i")
			.withArgument(new ArgumentBuilder()
				.withName("path")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.create(), 
		"inputDir"
	);
	
	private Option option;
	private String attribName;
	
	LdapLoaderCliOption(Option option)
	{
		this(option, null);
	}
	
	LdapLoaderCliOption(Option option, String attribName)
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