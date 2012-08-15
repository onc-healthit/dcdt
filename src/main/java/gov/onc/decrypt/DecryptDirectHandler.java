package gov.onc.decrypt;

/**
 * Interface for all decrypt handlers to act on the email message to be
 * decrypted; using the execute method.
 * @author jasonsmith
 *
 */
public interface DecryptDirectHandler {

	EmailBean execute(EmailBean emailInfo) throws Exception;

}
