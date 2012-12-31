package gov.hhs.onc.dcdt.utils.cli;

import org.apache.commons.cli2.Option;

public interface CliOption
{
	public Option getOption();
	public String getAttribName();
}