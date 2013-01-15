package gov.hhs.onc.dcdt.utils.data.loader;

import gov.hhs.onc.dcdt.utils.cli.CliOption;
import gov.hhs.onc.dcdt.utils.cli.validation.NotBlankValidator;
import gov.hhs.onc.dcdt.utils.cli.validation.PositiveIntegerValidator;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;

public enum DataLoaderCliOption implements CliOption
{
	DOMAIN
	(
		new DefaultOptionBuilder()
			.withDescription("Domain name to load data for.")
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
	DOMAIN_IP
	(
		new DefaultOptionBuilder()
			.withDescription("Domain IPv4 address to load data for.")
			.withLongName("dip")
			.withLongName("domainip")
			.withArgument(new ArgumentBuilder()
				.withName("address")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.withRequired(true)
			.create(), 
		"domainIp"
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
	WEB_SERVICE_HOST
	(
		new DefaultOptionBuilder()
			.withDescription("Web service (config-service) host.")
			.withLongName("wsh")
			.withLongName("wshost")
			.withArgument(new ArgumentBuilder()
				.withName("name")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.create(), 
		"wsHost"
	),
	WEB_SERVICE_PATH
	(
		new DefaultOptionBuilder()
			.withDescription("Web service (config-service) path.")
			.withLongName("wspd")
			.withLongName("wspath")
			.withArgument(new ArgumentBuilder()
				.withName("path")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.create(), 
		"wsPath"
	),
	WEB_SERVICE_PORT
	(
		new DefaultOptionBuilder()
			.withDescription("Web service (config-service) port number.")
			.withLongName("wsp")
			.withLongName("wsport")
			.withArgument(new ArgumentBuilder()
				.withName("num")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(PositiveIntegerValidator.INSTANCE)
				.create())
			.create(), 
		"wsPort"
	);
	
	private Option option;
	private String attribName;
	
	DataLoaderCliOption(Option option)
	{
		this(option, null);
	}
	
	DataLoaderCliOption(Option option, String attribName)
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