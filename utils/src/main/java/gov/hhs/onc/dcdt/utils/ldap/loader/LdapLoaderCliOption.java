package gov.hhs.onc.dcdt.utils.ldap.loader;

import gov.hhs.onc.dcdt.utils.cli.CliOption;
import gov.hhs.onc.dcdt.utils.cli.validation.NotBlankValidator;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;

public enum LdapLoaderCliOption implements CliOption
{
	BIND_DN_NAME
	(
		new DefaultOptionBuilder()
			.withDescription("Distinguished name to use when binding to a LDAP service.")
			.withLongName("bdn")
			.withLongName("binddn")
			.withArgument(new ArgumentBuilder()
				.withName("dn")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.create(), 
		"bindDnName"
	), 
	BIND_PASS
	(
		new DefaultOptionBuilder()
			.withDescription("Distinguished name to use when binding to a LDAP service.")
			.withLongName("bps")
			.withLongName("bindpass")
			.withArgument(new ArgumentBuilder()
				.withName("password")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.create(), 
		"bindPass"
	), 
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
	INPUT_PATH
	(
		new DefaultOptionBuilder()
			.withDescription("Path to the input directory or archive file from which to read certificates/keys.")
			.withShortName("i")
			.withLongName("in")
			.withArgument(new ArgumentBuilder()
				.withName("path")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.create(), 
		"inputPath"
	), 
	LOAD_DN_NAME
	(
		new DefaultOptionBuilder()
			.withDescription("Distinguished name to load the LDAP entries into.")
			.withLongName("dn")
			.withLongName("loaddn")
			.withArgument(new ArgumentBuilder()
				.withName("dn")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.create(), 
		"loadDnName"
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