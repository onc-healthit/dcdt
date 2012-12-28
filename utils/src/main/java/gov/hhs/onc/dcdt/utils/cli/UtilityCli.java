package gov.hhs.onc.dcdt.utils.cli;

import java.util.EnumSet;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang.StringUtils;

public class UtilityCli
{
	private Class<?> optionClass;
	private boolean parsed;
	private Options options;
	private String[] args;
	private CommandLine cmdLine;
	
	public UtilityCli(Class<?> optionClass)
	{
		this.optionClass = optionClass;
	}
	
	public void parse(String ... args) throws UtilityCliException
	{
		this.parsed = false;
		
		try
		{
			this.options = createOptions(this.optionClass);
			this.args = args;
			this.cmdLine = new PosixParser().parse(options, args);
			
			this.parsed = true;
		}
		catch (ParseException e)
		{
			throw new UtilityCliException("Unable to parse command line options: args=[" + StringUtils.join(args, ", ") + "]", e);
		}
	}
	
	public String getOptionValue(Option option)
	{
		return this.hasOption(option) ? this.getCmdLine().getOptionValue(option.getOpt()) : null;
	}
	
	public <T> T getOptionValue(Class<T> valueClass, Option option) throws UtilityCliException
	{
		return this.hasOption(option) ? createOptionValue(valueClass, this.getCmdLine().getOptionValue(option.getOpt())) : null;
	}
	
	public boolean hasOption(Option option)
	{
		return this.isParsed() && this.getCmdLine().hasOption(option.getOpt());
	}
	
	public boolean hasOptions()
	{
		return this.isParsed() && (this.getCmdLine().getOptions().length > 0);
	}
	
	private static <T> T createOptionValue(Class<T> valueClass, String valueStr) throws UtilityCliException
	{
		try
		{
			return valueClass.cast(TypeHandler.createValue(valueStr, valueClass));
		}
		catch (ParseException e)
		{
			throw new UtilityCliException("Unable to create value: valueClass=" + valueClass.getName() + ", valueStr=" + valueStr, e);
		}
	}

	private static <U extends Enum<U>> Options createOptions(Class<?> optionClass)
	{
		Options options = new Options();
		
		for (Class optionClassItem : new Class[]{ UtilityCliOption.class, optionClass })
		{
			for (U option : EnumSet.allOf((Class<U>)optionClassItem))
			{
				options.addOption(((CliOption)option).getOption());
			}
		}
		
		return options;
	}
	
	public String[] getArgs()
	{
		return this.args;
	}

	public CommandLine getCmdLine()
	{
		return this.cmdLine;
	}

	public Options getOptions()
	{
		return this.options;
	}
	
	public boolean isParsed()
	{
		return this.parsed;
	}
}