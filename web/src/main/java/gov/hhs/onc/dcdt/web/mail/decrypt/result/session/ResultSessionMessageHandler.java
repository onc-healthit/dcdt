package gov.hhs.onc.dcdt.web.mail.decrypt.result.session;

import gov.hhs.onc.dcdt.web.mail.decrypt.DecryptDirectHandler;
import gov.hhs.onc.dcdt.web.mail.decrypt.EmailBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class ResultSessionMessageHandler implements DecryptDirectHandler
{
	private final static Map<String, String> SESSION_MAP = new HashMap<>();
	
	private final static Map<String, List<EmailBean>> RESULTS_MAP = new HashMap<>();

	private final static Logger LOGGER = Logger.getLogger(ResultSessionMessageHandler.class);
	
	public synchronized static String getSessionAddress(String sessionId)
	{
		return SESSION_MAP.get(sessionId);
	}
	
	public synchronized static boolean isSessionRegistered(String sessionId)
	{
		return SESSION_MAP.containsKey(sessionId);
	}
	
	public synchronized static void registerSessionAddress(String sessionId, String directMailAddr)
	{
		if (!SESSION_MAP.containsKey(sessionId) || 
			!StringUtils.equalsIgnoreCase(directMailAddr, getSessionAddress(sessionId)))
		{
			clearResults(directMailAddr);
			
			SESSION_MAP.put(sessionId, directMailAddr);
			
			LOGGER.trace("Registered session (id=" + sessionId + "): " + directMailAddr);
		}
	}
	
	public synchronized static void addResult(String directMailAddr, EmailBean emailInfo)
	{
		List<EmailBean> results = RESULTS_MAP.get(directMailAddr);
		
		if (results == null)
		{
			results = new ArrayList<>();
			
			RESULTS_MAP.put(directMailAddr, results);
		}
		
		RESULTS_MAP.get(directMailAddr).add(emailInfo);
		
		LOGGER.trace("Added result (status=" + emailInfo.getResultStatus() + ", msg=" + emailInfo.getResultMsg() + 
			") for Direct mail address: " + directMailAddr);
	}
	
	public synchronized static boolean hasResults(String directMailAddr)
	{
		return !CollectionUtils.isEmpty(RESULTS_MAP.get(directMailAddr));
	}
	
	public synchronized static List<EmailBean> getResults(String directMailAddr)
	{
		return RESULTS_MAP.get(directMailAddr);
	}
	
	public synchronized static void clearResults(String directMailAddr)
	{
		if (hasResults(directMailAddr))
		{
			getResults(directMailAddr).clear();
		}
	}
	
	@Override
	public EmailBean execute(EmailBean emailInfo) throws Exception
	{
		addResult(emailInfo.getFromAddress(), emailInfo);
		
		return emailInfo;
	}
}