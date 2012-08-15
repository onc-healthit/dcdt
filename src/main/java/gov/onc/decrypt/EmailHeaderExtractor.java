package gov.onc.decrypt;

import gov.onc.startup.ConfigInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;


/**
 * Email Decrypt Handler that implements the DecryptDirectHandler interface.
 * Extracts email header information and populates the email message bean.
 * @author joelamy
 *
 */
public class EmailHeaderExtractor implements DecryptDirectHandler {

	private Message message;

	private Logger log = Logger.getLogger("emailMessageLogger");

	/**
	 * Extracts email header info and does a test case look up for valid
	 * test case (throws exception on invalid test case).
	 * Implements DecryptDirectHandler method.
	 * @param emailInfo
	 * @throws Exception
	 */
	public EmailBean execute(EmailBean emailInfo) throws Exception {
		initializeMessage(emailInfo);
		emailInfo.setFromAddress(getFrom());
		emailInfo.setToAddress(getTo());

		log.info("Inbound Email - FROM - " + getFrom()
			+ " - TO - " + getTo());

		LookupTest thisTest = LookupTest.findLookupTest(getTo());
		if (thisTest == null) {
			log.error("Inbound Email: To address " + getTo()
					+ " not associated with a test case.");
			throw new IllegalArgumentException("To address "
					+ getTo()
					+ " not associated with a test case.");
		}
		emailInfo.setThisTest(thisTest);
		log.info("Inbound Email: TEST CASE =" +
			emailInfo.getThisTest());

		return emailInfo;
	}

	/**
	 * Initializes email message object.
	 * Check for the right content type.
	 * isMimeType compares it correctly, stripping out any parameters on the line.
	 * (if this isn't an email at all it may just say it's text/plain)
	 * @param emailInfo
	 * @throws Exception
	 */
	private void initializeMessage(EmailBean emailInfo) throws Exception {

		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		Session mailSession = Session.getDefaultInstance(props, null);
		String emailPath = ConfigInfo.getConfigProperty("EmlLocation");
		String email = emailPath + File.separatorChar
			+ emailInfo.getFileLocation();

		InputStream source = new FileInputStream(email);
		this.message = new MimeMessage(mailSession, source);

		source.close();

		log.info("Email File Path: " + email);
		log.info("Content type: " + this.message.getContentType());

		if (!(this.message.isMimeType("application/pkcs7-mime"))) {
			log.error("Email Message FROM - " + emailInfo.getFromAddress()
					+ " - TO - " + emailInfo.getToAddress() + " - SUBJECT - " + message.getSubject()
					+ "\n\n Invalid Content-type.  Was " + this.message.getContentType()
					+ ", expected application/pkcs7-mime");
			throw new IllegalArgumentException("Invalid Content-type. Was " + this.message.getContentType()
					+ ", expected application/pkcs7-mime");
		}
	}

	/**
	 * @return To address as sent in email. May be simple address: addr@domain.com
	 * or name & address: "Daniel Simpson" <djs@yahoo.co.uk>
	 * @throws Exception
	 */
	private String getTo() throws Exception {

		// Only handles a single recipient
		InternetAddress[] toAddrList =
				(InternetAddress[]) message.getAllRecipients();
		return toAddrList[0].getAddress().toString();
	}

	/**
	 * @return From address as sent in email. May be simple address: addr@domain.com
	 * or name & address: "Daniel Simpson" <djs@yahoo.co.uk>
	 * @throws Exception
	 */
	private String getFrom() throws Exception {

		InternetAddress[] fromAddrList =
			(InternetAddress[]) message.getFrom();
		return fromAddrList[0].getAddress().toString();
	}

	/**
	 * Compares an address to the first recipient (i.e. To address). Will handle different formats,
	 * for example, "Daniel Simpson" <djs@yahoo.co.uk> will be equal to djs@yahoo.co.uk.
	 * @param addr Address to compare
	 * @return true if the address matches the first recipient
	 * @throws Exception
	 */
	public boolean equalsTo(Address addr) throws Exception {

		// Only handles a single recipient
		Address[] toAddrList = message.getAllRecipients();
		return toAddrList[0].equals(addr);
	}

	/**
	 * Compares an address to the first From address. Will handle different formats,
	 * for example, "Daniel Simpson" <djs@yahoo.co.uk> will be equal to djs@yahoo.co.uk.
	 * @param addr Address to compare
	 * @return true if the address matches the first From address
	 * @throws Exception
	 */
	public boolean equalsFrom(Address addr) throws Exception {

		// Only handles a single recipient
		Address[] fromAddrList = message.getFrom();
		return fromAddrList[0].equals(addr);
	}

}

