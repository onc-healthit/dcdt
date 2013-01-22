package gov.hhs.onc.dcdt.web.mail.decrypt.result;

import gov.hhs.onc.dcdt.web.mail.decrypt.DecryptDirectHandler;
import gov.hhs.onc.dcdt.web.mail.decrypt.EmailBean;
import gov.hhs.onc.dcdt.web.startup.ConfigInfo;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class ResultMailMessageHandler implements DecryptDirectHandler
{
	private static class ResultMailOutputStream extends OutputStream
	{	
		@Override
		public void write(int data) throws IOException
		{
		}
	}
	
	private static class ResultMailPrintStream extends PrintStream
	{
		public final static ResultMailPrintStream INSTANCE = new ResultMailPrintStream();
		
		private ResultMailPrintStream()
		{
			super(new ResultMailOutputStream(), true);
		}

		@Override
		public void println(String str)
		{
			LOGGER.trace(str);
		}
	}
	
	private static class ResultMailAuthenticator extends Authenticator
	{
		private String addr;
		private String pass;
		
		public ResultMailAuthenticator(String addr, String pass)
		{
			this.addr = addr;
			this.pass = pass;
		}
		
		@Override
		protected PasswordAuthentication getPasswordAuthentication()
		{
			return new PasswordAuthentication(this.addr, this.pass);
		}
	}
	
	private final static String RESULT_MAIL_HOST_NAME = ConfigInfo.getConfigProperty("smtpHostName");
	private final static int RESULT_MAIL_PORT = Integer.parseInt(ConfigInfo.getConfigProperty("smtpPort"));
	private final static boolean RESULT_MAIL_USE_SSL = BooleanUtils.isNotFalse(BooleanUtils.toBooleanObject(
		ConfigInfo.getConfigProperty("smtpUseSSL", true)));
	private final static String RESULT_MAIL_FROM_ADDR = ConfigInfo.getConfigProperty("emailAddress");
	private final static String RESULT_MAIL_PASS = ConfigInfo.getConfigProperty("emailPassword");
	private final static String RESULT_MAIL_SUBJECT = "Results for Direct Certificate Discovery Test (Do not Reply)";
	private final static String RESULT_MAIL_CONTENT_TYPE = "text/plain";
	private final static String RESULT_MAIL_HEADER_TESTCASE_NAME = "X-DCDT-Testcase";
	private final static String RESULT_MAIL_HEADER_TESTCASE_RESULT = "X-DCDT-Testcase-Result";
	private final static String RESULT_MAIL_HEADER_TESTCASE_RESULT_DETAILS = "X-DCDT-Testcase-Result-Details";
	
	private final static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
	private final static Logger LOGGER = Logger.getLogger(ResultMailMessageHandler.class);

	@Override
	public EmailBean execute(EmailBean emailInfo) throws Exception
	{
		String senderAddr = emailInfo.getFromAddress(), resultAddr = ConfigInfo.getEmailProperty(senderAddr, true);
		
		if (StringUtils.isBlank(resultAddr))
		{
			throw new IllegalArgumentException("No matching result mail address found for sender mail address: " + 
				senderAddr);
		}
		
		Map<String, Object> headerMap = new HashMap<>();
		StringBuilder bodyBuilder = new StringBuilder("Test case ");
		bodyBuilder.append(emailInfo.getThisTest().getTestCaseName());
		bodyBuilder.append(" results:");

		// add test case name header information
		headerMap.put(RESULT_MAIL_HEADER_TESTCASE_NAME, emailInfo.getThisTest().getTestCaseName());
		
		if (emailInfo.getPasses())
		{
			// add pass header information
			headerMap.put(RESULT_MAIL_HEADER_TESTCASE_RESULT, "passes");
			
			bodyBuilder.append(" passes.\nCongratulations!");
		}
		else
		{
			// add fail header information
			headerMap.put(RESULT_MAIL_HEADER_TESTCASE_RESULT, "fails");
			
			bodyBuilder.append(" fails.\nTry Again.");
		}
		
		// add details header information
		headerMap.put(RESULT_MAIL_HEADER_TESTCASE_RESULT_DETAILS, emailInfo.getResults());
		
		bodyBuilder.append("\nDetails: ");
		bodyBuilder.append(emailInfo.getResults());
		
		try
		{
			sendMessage(headerMap, resultAddr, bodyBuilder.toString());
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			
			throw e;
		}
		
		return emailInfo;
	}
	
	private static void sendMessage(Map<String, Object> headerMap, String resultAddr, String body) throws MessagingException
	{
		Message msg = getMessage(headerMap, resultAddr, body);
		
		LOGGER.debug("Sending result mail message (addr=" + resultAddr + "): headers=" + StringUtils.join(headerMap, ", ") + 
			"body=\n" + body);
		
		Transport.send(msg);
	}
	
	private static Message getMessage(Map<String, Object> headerMap, String resultAddr, String body) throws MessagingException
	{
		Message msg = new MimeMessage(getMailSession(RESULT_MAIL_HOST_NAME, RESULT_MAIL_PORT, RESULT_MAIL_USE_SSL, 
			RESULT_MAIL_FROM_ADDR, RESULT_MAIL_PASS));
		
		for (String headerName : headerMap.keySet())
		{
			msg.setHeader(headerName, ObjectUtils.toString(headerMap.get(headerName)));
		}
		
		msg.setFrom(new InternetAddress(RESULT_MAIL_FROM_ADDR));
		msg.setRecipient(RecipientType.TO, new InternetAddress(resultAddr));
		msg.setSubject(RESULT_MAIL_SUBJECT);
		msg.setContent(body, RESULT_MAIL_CONTENT_TYPE);
		
		return msg;
	}
	
	private static Session getMailSession(String hostName, int port, boolean useSsl, String addr, String pass)
	{
		Session session = Session.getInstance(getMailProperties(hostName, port, useSsl), 
			new ResultMailAuthenticator(addr, pass));
		session.setDebug(true);
		session.setDebugOut(ResultMailPrintStream.INSTANCE);
		
		return session;
	}
	
	private static Properties getMailProperties(String hostName, int port, boolean useSsl)
	{
		Properties props = new Properties();
		props.put("mail.debug", true);
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.host", hostName);
		props.put("mail.smtp.port", port);
		
		if (useSsl)
		{
			props.put("mail.smtp.starttls.enable", true);
			props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
			props.put("mail.smtp.socketFactory.fallback", true);
			props.put("mail.smtp.socketFactory.port", port);
		}
		
		return props;
	}
}