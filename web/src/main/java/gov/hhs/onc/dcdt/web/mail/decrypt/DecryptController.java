package gov.hhs.onc.dcdt.web.mail.decrypt;

import gov.hhs.onc.dcdt.web.mail.decrypt.result.ResultMailMessageHandler;
import gov.hhs.onc.dcdt.web.mail.decrypt.result.session.ResultSessionMessageHandler;
import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Takes in email file and extracts header, decrypts the message, and mails out
 * response message using applicable handlers.
 * @author jasonsmith
 *
 */
public class DecryptController {

	private EmailBean messageInfo;
	private Queue<DecryptDirectHandler> handlers;

	/**
	 * Constructor - initializes decrypt handler queue and pushes
	 * the handlers to the queue.
	 * @param file email file
	 */
	public DecryptController(File file) {
		this.messageInfo = new EmailBean(file);
		this.handlers = new LinkedList<>();
		this.setMessageHandling();
	}

	/**
	 * Processes one message with the decrypter handlers.
	 * @throws Exception
	 */
	public void processMessage() throws Exception
	{
		this.messageInfo.setReceived(new Date());
		
		for (DecryptDirectHandler handler : this.handlers) {
			handler.execute(this.messageInfo);
		}
	}

	private void setMessageHandling() {
		this.handlers.add(new EmailHeaderExtractor());
		this.handlers.add(new Decryptor());
		this.handlers.add(new ResultMailMessageHandler());
		this.handlers.add(new ResultSessionMessageHandler());
	}

	public EmailBean getMessageInfo()
	{
		return this.messageInfo;
	}
}
