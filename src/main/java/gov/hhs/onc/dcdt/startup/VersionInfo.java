package gov.hhs.onc.dcdt.startup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public abstract class VersionInfo
{
	private final static String AUTHOR_PROP_NAME = "version.svn.author";
	private final static String DATE_PROP_NAME = "version.svn.date";
	private final static String DISPLAY_PROP_NAME = "version.display";
	private final static String HEAD_URL_PROP_NAME = "version.svn.head.url";
	private final static String REVISION_PROP_NAME = "version.svn.revision";
	
	private final static Pattern AUTHOR_PATTERN = Pattern.compile("^\\s*\\$Author:\\s+(.+)\\s+\\$\\s*$");
	private final static Pattern DATE_PATTERN = Pattern.compile("^\\s*\\$Date:\\s+(\\d{4}\\-\\d{2}\\-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}\\s+\\-?\\d{4}).+$");
	private final static Pattern DISPLAY_PATTERN = Pattern.compile("^\\s*(.+)\\s*$");
	private final static Pattern HEAD_URL_PATTERN = Pattern.compile("^\\s*\\$HeadURL:\\s+.+/svn/(.+)/pom\\.properties\\s+\\$\\s*$");
	private final static Pattern REVISION_PATTERN = Pattern.compile("^\\s*\\$Revision:\\s+(\\d+)\\s+\\$\\s*$");
	
	private final static DateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
	private final static DateFormat SVN_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
	
	private final static Logger LOGGER = Logger.getLogger("certDiscoveryLogger");
	
	public static String getAuthor()
	{
		return getValue(AUTHOR_PROP_NAME, AUTHOR_PATTERN);
	}
	
	public static String getDate()
	{
		String dateStr = getValue(DATE_PROP_NAME, DATE_PATTERN);
		
		if (dateStr != null)
		{
			try
			{
				dateStr = DISPLAY_DATE_FORMAT.format(SVN_DATE_FORMAT.parse(dateStr));
			}
			catch (ParseException e)
			{
				LOGGER.error("Unable to parse version SVN date: " + dateStr, e);
			}
		}
		
		return dateStr;
	}
	
	public static String getDisplay()
	{
		return getValue(DISPLAY_PROP_NAME, DISPLAY_PATTERN);
	}
	
	public static String getHeadUrl()
	{
		return getValue(HEAD_URL_PROP_NAME, HEAD_URL_PATTERN);
	}
	
	public static String getRevision()
	{
		return getValue(REVISION_PROP_NAME, REVISION_PATTERN);
	}
	
	private static String getValue(String propName, Pattern pattern)
	{
		String propValue = ConfigInfo.getVersionProperty(propName);
		Matcher matcher;
		
		return (propValue != null) && (matcher = pattern.matcher(propValue)).matches() ? 
			matcher.group(1) : propValue;
	}
}