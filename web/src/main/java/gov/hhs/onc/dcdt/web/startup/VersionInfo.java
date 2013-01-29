package gov.hhs.onc.dcdt.web.startup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public abstract class VersionInfo
{
	private final static String VERSION_PROP_KEY = "dcdt.version";
	private final static String VERSION_SVN_AUTHOR_PROP_KEY = "dcdt.version.svn.author";
	private final static String VERSION_SVN_DATE_PROP_KEY = "dcdt.version.svn.date";
	private final static String VERSION_SVN_HEAD_URL_PROP_KEY = "dcdt.version.svn.head.url";
	private final static String VERSION_SVN_REVISION_PROP_KEY = "dcdt.version.svn.revision";
	
	private final static Pattern VERSION_PATTERN = Pattern.compile("^\\s*(.+)\\s*$");
	private final static Pattern VERSION_SVN_AUTHOR_PATTERN = 
		Pattern.compile("^\\s*\\$Author:\\s+(.+)\\s+\\$\\s*$");
	private final static Pattern VERSION_SVN_DATE_PATTERN = 
		Pattern.compile("^\\s*\\$Date:\\s+(\\d{4}\\-\\d{2}\\-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}\\s+\\-?\\d{4}).+$");
	private final static Pattern VERSION_SVN_HEAD_URL_PATTERN = 
		Pattern.compile("^\\s*\\$HeadURL:\\s+.+/svn/(.+)/pom\\.properties\\s+\\$\\s*$");
	private final static Pattern VERSION_SVN_REVISION_PATTERN = 
		Pattern.compile("^\\s*\\$Revision:\\s+(\\d+)\\s+\\$\\s*$");
	
	private final static DateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
	private final static DateFormat SVN_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
	
	private final static Logger LOGGER = Logger.getLogger(VersionInfo.class);
	
	public static String getVersion()
	{
		return getValue(VERSION_PROP_KEY, VERSION_PATTERN);
	}
	
	public static String getSvnAuthor()
	{
		return getValue(VERSION_SVN_AUTHOR_PROP_KEY, VERSION_SVN_AUTHOR_PATTERN);
	}
	
	public static String getSvnDate()
	{
		String dateStr = getValue(VERSION_SVN_DATE_PROP_KEY, VERSION_SVN_DATE_PATTERN);
		
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
	
	public static String getSvnHeadUrl()
	{
		return getValue(VERSION_SVN_HEAD_URL_PROP_KEY, VERSION_SVN_HEAD_URL_PATTERN);
	}
	
	public static String getSvnRevision()
	{
		return getValue(VERSION_SVN_REVISION_PROP_KEY, VERSION_SVN_REVISION_PATTERN);
	}
	
	private static String getValue(String propKey, Pattern pattern)
	{
		String propValue = ConfigInfo.getVersionProperty(propKey);
		Matcher matcher;
		
		return (propValue != null) && (matcher = pattern.matcher(propValue)).matches() ? 
			matcher.group(1) : propValue;
	}
}