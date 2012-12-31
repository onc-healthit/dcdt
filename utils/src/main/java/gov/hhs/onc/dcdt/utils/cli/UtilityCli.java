package gov.hhs.onc.dcdt.utils.cli;

import java.util.EnumSet;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class UtilityCli<T extends Enum<T> & CliOption>
{
	private Class<T> utilOptionClass;
	private boolean parsed;
	private String[] args;
	private Group optionsGroup;
	private Parser parser;
	private CommandLine cmdLine;
	
	public UtilityCli(Class<T> utilOptionClass)
	{
		this.utilOptionClass = utilOptionClass;
	}
	
	public void parse(String ... args) throws UtilityCliException
	{
		this.parsed = false;
		
		try
		{
			this.args = args;
			this.optionsGroup = this.createOptionsGroup();
			
			this.parser = new Parser();
			this.parser.setGroup(this.optionsGroup);
			this.parser.setHelpOption(UtilityCliOption.HELP.getOption());
			
			this.cmdLine = this.parser.parse(args);
			
			this.parsed = true;
		}
		catch (OptionException e)
		{
			throw new UtilityCliException("Unable to parse command line options: args=[" + StringUtils.join(args, ", ") + "]", e);
		}
	}
	
	public String getOptionValue(T option)
	{
		return ObjectUtils.toString(this.getOptionValue(Object.class, option), null);
	}
	
	public <U> U getOptionValue(Class<U> valueClass, T option)
	{
		return this.hasOption(option) ? valueClass.cast(this.getCmdLine().getValue(option.getOption())) : null;
	}
	
	public boolean hasOption(T option)
	{
		return this.isParsed() && this.getCmdLine().hasOption(option.getOption());
	}
	
	public boolean hasOptions()
	{
		return this.isParsed() && !this.getCmdLine().getOptions().isEmpty();
	}
	
	private Group createOptionsGroup()
	{
		GroupBuilder optionsGroupBuilder = new GroupBuilder().withOption(UtilityCliOption.HELP.getOption());
		
		for (T utilOption : EnumSet.allOf(this.utilOptionClass))
		{
			optionsGroupBuilder = optionsGroupBuilder.withOption(utilOption.getOption());
		}
		
		return optionsGroupBuilder.create();
	}
	public String[] getArgs()
	{
		return this.args;
	}

	public CommandLine getCmdLine()
	{
		return this.cmdLine;
	}

	public Group getOptionsGroup()
	{
		return optionsGroup;
	}

	public boolean isParsed()
	{
		return this.parsed;
	}
	
	public Parser getParser()
	{
		return parser;
	}
}