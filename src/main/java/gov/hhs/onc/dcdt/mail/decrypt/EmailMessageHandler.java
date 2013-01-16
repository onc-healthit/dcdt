package gov.hhs.onc.dcdt.mail.decrypt;

import gov.hhs.onc.dcdt.startup.ConfigInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;



public class EmailMessageHandler implements DecryptDirectHandler{

	private static final String SMTP_HOST_NAME = ConfigInfo.getConfigProperty("smtpHostName");
	private static final String SMTP_PORT = ConfigInfo.getConfigProperty("smtpPort");
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
	private Logger log = Logger.getLogger("emailMessageLogger");
	
	public EmailBean execute(EmailBean emailInfo) throws Exception {
		
		final String TO_ADDR = getReturnMessage(emailInfo.getFromAddress());
		if (TO_ADDR == null) {
			throw new IllegalArgumentException("No results email address found for sender " +
					emailInfo.getFromAddress());
		}
		final String FROM_ADDR = ConfigInfo.getConfigProperty("emailAddress");
		final String PASSWORD = ConfigInfo.getConfigProperty("emailPassword");
		final String SUBJECT = "Results for Direct Certificate Discovery Test (Do not Reply)";
		
		
		StringBuffer body = new StringBuffer("Test case " + emailInfo.getThisTest().getTestCaseName() + " results:");
		
//		add test case name header information here - "X-test_case_name"
		
		
		
		if(emailInfo.getPasses()){
//			add pass header information here - use "X-result"
			
			body.append(" passes.\nCongratulations!");
		}else{
			body.append(" fails.\nTry Again.");
//		add fail header information here - use "X-result"
		
		}
		body.append("\nDetails: " + emailInfo.getResults());
//		add details header information here - "X-details"
		
		
		
		logMailInfo(TO_ADDR, FROM_ADDR, SUBJECT, body.toString());
		
		try {
			sendSSLMessage(TO_ADDR, FROM_ADDR,
					SUBJECT, body.toString(), PASSWORD, emailInfo);
		} catch (Exception ex) {
			log.error(ex.getMessage() + " caused by: " + ex.getCause()
					+ ex.getStackTrace());
			throw ex;
		}
		
		return emailInfo;
	}
	
	private String getReturnMessage(String key){
		return ConfigInfo.getEmailProperty(key);
	}
	
	// JML: Says it throws, but catches all exceptions.
	private void sendSSLMessage(final String toAddr, final String fromAddr, final String subjectTxt,
			final String msgContent, final String password, EmailBean emailInfo ) throws MessagingException{
		boolean debug = true;

	
		
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(fromAddr,
								password);
					}
				});
		
		session.setDebug(debug);
		
		MailOutputStream mos = new MailOutputStream();

		session.setDebugOut(new PrintStream(mos));
		
		Message msg = new MimeMessage(session);
		InternetAddress internetFrom = new InternetAddress(fromAddr);
		msg.setFrom(internetFrom);
		
		InternetAddress[] addressTo = { new InternetAddress(toAddr) };
		msg.setRecipients(Message.RecipientType.TO, addressTo);
	
//		add test case name header information here - "X-test_case_name"  
		msg.addHeader("X-test_case_name", emailInfo.getThisTest().getTestCaseName());
		
		if(emailInfo.getPasses()){
//			add pass header information here - use "X-result"
			 msg.addHeader("X-result", "passes");
		 }else{
//		   add fail header information here - use "X-result"
		     msg.addHeader("X-result", "fails");
		}
		
//		add details header information here - "X-details"
		  msg.addHeader("X-details", emailInfo.getResults());
	
		msg.setSubject(subjectTxt);
		msg.setContent(msgContent, "text/plain");
		Transport.send(msg);
	}
	
	private void logMailInfo(String to, String from, String subject, String body){
		log.info("SMTP_HOST_NAME: " + SMTP_HOST_NAME);
		log.info("SMTP_PORT: "+ SMTP_PORT);
		log.info("TO:  " + to);
		log.info("FROM:  " + from);
		log.info("SUBJECT:  " + subject);
		log.info("Body:  " + body);
	}
	
	class MailOutputStream extends OutputStream{

		String mem;
		
		@Override
		public void write(int b) throws IOException {
			byte[] bytes = new byte[1];
	        bytes[0] = (byte) (b & 0xff);
	        mem = mem + new String(bytes);

	        if (mem.endsWith ("\n")) {
	            mem = mem.substring (0, mem.length () - 1);
	            flush ();
	        }
		}
		
		@Override
		public void flush(){
			log.info(mem);
			mem = "";
		}
	}
}
