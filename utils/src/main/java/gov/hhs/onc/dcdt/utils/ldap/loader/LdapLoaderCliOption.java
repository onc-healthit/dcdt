package gov.hhs.onc.dcdt.utils.ldap.loader;

import gov.hhs.onc.dcdt.utils.cli.CliOption;
import gov.hhs.onc.dcdt.utils.cli.validation.NotBlankValidator;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.SwitchBuilder;
import org.apache.commons.cli2.option.Switch;

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
	LOAD
	(
		new SwitchBuilder(Switch.DEFAULT_DISABLED_PREFIX, Switch.DEFAULT_ENABLED_PREFIX)
			.withDescription("Whether to load LDAP entries.")
			.withName("l")
			.withName("load")
			.create(), 
		"load"
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
	), 
	OUTPUT_LDIFS_PATH
	(
		new DefaultOptionBuilder()
			.withDescription("Path to the output directory into which to write parsed LDIF files.")
			.withLongName("ol")
			.withLongName("outldifs")
			.withArgument(new ArgumentBuilder()
				.withName("path")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.create(), 
		"outputLdifsPath"
	), ;
	
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