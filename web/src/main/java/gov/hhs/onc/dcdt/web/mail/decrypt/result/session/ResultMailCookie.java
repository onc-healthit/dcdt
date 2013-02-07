package gov.hhs.onc.dcdt.web.mail.decrypt.result.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class ResultMailCookie extends Cookie
{
	private final static String COOKIE_NAME = "directMail";
	private final static String COOKIE_COMMENT = 
		"Direct Certificate Discovery Tool (DCDT) Discovery testcase results Direct mail address.";

	private final static Logger LOGGER = Logger.getLogger(ResultMailCookie.class);
	
	public ResultMailCookie(Cookie cookie)
	{
		super(cookie.getName(), cookie.getValue());
		
		this.setComment(cookie.getComment());
	}
	
	public ResultMailCookie(String directMailAddr)
	{
		super(COOKIE_NAME, directMailAddr);
		
		this.setComment(COOKIE_COMMENT);
	}
	
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String directMailAddr)
	{
		ResultMailCookie cookie = getCookie(request);
		
		if (cookie == null)
		{
			cookie = new ResultMailCookie(directMailAddr);
		}
		else
		{
			cookie.setValue(directMailAddr);
		}
		
		response.addCookie(cookie);
		
		LOGGER.trace("Set result mail cookie: name=" + cookie.getName() + ", value=" + cookie.getValue());
	}
	
	public static boolean hasCookie(HttpServletRequest request)
	{
		return getCookie(request) != null;
	}
	
	public static ResultMailCookie getCookie(HttpServletRequest request)
	{
		for (Cookie cookie : request.getCookies())
		{
			if (cookie.getName().equals(COOKIE_NAME))
			{
				ResultMailCookie resultMailCookie = ResultMailCookie.class.isAssignableFrom(cookie.getClass()) ? 
					(ResultMailCookie)cookie : new ResultMailCookie(cookie);
				
				LOGGER.trace("Found result mail cookie: name=" + resultMailCookie.getName() + ", value=" + resultMailCookie);
				
				return resultMailCookie;
			}
		}
		
		return null;
	}
}