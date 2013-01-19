package gov.hhs.onc.dcdt.web.mail.decrypt;

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
	 * @param fileName Location of email file
	 */
	public DecryptController(String fileName) {
		messageInfo = new EmailBean(fileName);
		handlers = new LinkedList<DecryptDirectHandler>();
		setMessageHandling();
	}

	/**
	 * Processes one message with the decrypter handlers.
	 * @throws Exception
	 */
	public void processMessage() throws Exception {

		for (DecryptDirectHandler handler : handlers) {
			handler.execute(this.messageInfo);
		}
	}

	private void setMessageHandling() {
		handlers.add(new EmailHeaderExtractor());
		handlers.add(new Decryptor());
		handlers.add(new EmailMessageHandler());
	}

}
