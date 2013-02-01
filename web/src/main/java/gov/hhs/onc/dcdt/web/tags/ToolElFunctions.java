package gov.hhs.onc.dcdt.web.tags;

import gov.hhs.onc.dcdt.beans.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.beans.testcases.hosting.HostingTestcase;
import gov.hhs.onc.dcdt.config.ToolVersion;
import gov.hhs.onc.dcdt.web.config.WebConfig;
import gov.hhs.onc.dcdt.web.startup.ConfigInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.struts.Globals;

public abstract class ToolElFunctions
{
	private final static String PATH_PART_DELIM = "/";
	private final static String STATIC_PATH_PART = "static";
	private final static String STATIC_IMAGES_PATH_PART = "images";
	private final static String STATIC_SCRIPTS_PATH_PART = "scripts";
	private final static String STATIC_STYLES_PATH_PART = "styles";
	
	private final static String REQUEST_METHOD_POST = "POST";
	
	public static boolean isRequestPost(PageContext pageContext)
	{
		return getRequest(pageContext).getMethod().equalsIgnoreCase(REQUEST_METHOD_POST);
	}
	
	public static String getExceptionContent(PageContext pageContext)
	{
		StrBuilder exceptionContentBuilder = new StrBuilder();
		Throwable throwable = (Throwable)getRequest(pageContext).getAttribute(Globals.EXCEPTION_KEY);
		
		if (throwable != null)
		{
			exceptionContentBuilder.append(ExceptionUtils.getMessage(throwable));
			exceptionContentBuilder.appendSeparator('\n');
			exceptionContentBuilder.append(ExceptionUtils.getStackTrace(throwable));
		}
		
		return exceptionContentBuilder.toString();
	}
	
	public static Map<String, HostingTestcase> getHostingTestcases()
	{
		return ConfigInfo.getHostingTestcases();
	}
	
	public static Map<String, DiscoveryTestcase> getDiscoveryTestcases()
	{
		return ConfigInfo.getDiscoveryTestcases();
	}
	
	public static ToolVersion getModuleVersion(String moduleName)
	{
		return getModuleVersions().get(moduleName);
	}
	
	public static Map<String, ToolVersion> getModuleVersions()
	{
		return getConfig().getModuleVersions();
	}
	
	public static WebConfig getConfig()
	{
		return ConfigInfo.getConfig();
	}
	
	public static String getStaticImagesPath(PageContext pageContext, String path)
	{
		return getStaticPath(pageContext, STATIC_IMAGES_PATH_PART, path);
	}
	
	public static String getStaticScriptsPath(PageContext pageContext, String path)
	{
		return getStaticPath(pageContext, STATIC_SCRIPTS_PATH_PART, path);
	}
	
	public static String getStaticStylesPath(PageContext pageContext, String path)
	{
		return getStaticPath(pageContext, STATIC_STYLES_PATH_PART, path);
	}
	
	public static String getStaticPath(PageContext pageContext, String ... pathParts)
	{
		List<String> pathPartsList = new ArrayList<>(pathParts.length + 2);
		pathPartsList.add(getContextPath(pageContext));
		pathPartsList.add(STATIC_PATH_PART);
		pathPartsList.addAll(Arrays.asList(pathParts));
		
		return StringUtils.join(pathPartsList, PATH_PART_DELIM);
	}
	
	private static String getContextPath(PageContext pageContext)
	{
		return getRequest(pageContext).getContextPath();
	}
	
	private static HttpServletRequest getRequest(PageContext pageContext)
	{
		return (HttpServletRequest)pageContext.getRequest();
	}
}