package gov.hhs.onc.dcdt.web.mail.decrypt;

import gov.hhs.onc.dcdt.web.startup.ConfigInfo;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * DecryptDirect handler.
 * Decrypts email message with openSSL command line tool using given certificate
 * files.  Checks certificates for possible failure reasons.
 * @author joelamy
 *
 */
public class Decryptor implements DecryptDirectHandler {

	private final static Logger LOGGER = Logger.getLogger(Decryptor.class);

	/**
	 * Attempts to decrypt message with openSSL.  On failure, cycles through
	 * possible failure certificate options for explanation.  Adds result to
	 * bean class.
	 * Implements DecryptDirectHandler execute method.
	 * @param emailInfo
	 * @return EmailBean
	 * @throws Exception
	 */
	public EmailBean execute(EmailBean emailInfo) throws Exception {
		LOGGER.debug("before decrypt for message.");
		boolean result = false;

		LookupTestCertInfo goodCertInfo =
				emailInfo.getThisTest().getGoodCertInfo();
		StringBuffer resultFromGoodCertString = new StringBuffer("");
		if (goodCertInfo != null) {
			result =
				decryptEmail(emailInfo, goodCertInfo, resultFromGoodCertString);

			if (result) {
				emailInfo.setPasses(true);
				emailInfo.setResults(resultFromGoodCertString.toString());
				return emailInfo;
			}
		}

		for (LookupTestCertInfo badPathInfo
			: emailInfo.getThisTest().getBadCertInfoList()) {

			StringBuffer resultString = new StringBuffer("");
			result = decryptEmail(emailInfo, badPathInfo, resultString);

			if (result) {
				emailInfo.setPasses(false);
				emailInfo.setResults(resultString.toString());
				return emailInfo;
			}
		}

		emailInfo.setPasses(false);
		emailInfo.setResults(resultFromGoodCertString.toString());
		return emailInfo;
	}

	/**
	 * Decpypts email using openSSL command line tool.
	 * @param emailInfo
	 * @param testPathInfo
	 * @param resultString
	 * @return boolean
	 */
	private boolean decryptEmail(EmailBean emailInfo,
		LookupTestCertInfo testPathInfo, StringBuffer resultString) {

		boolean result = false;
		final String CERT_PATH = ConfigInfo.getConfigProperty("CertLocation");
		final String EMAIL_PATH = ConfigInfo.getConfigProperty("EmlLocation");
		final String PUBLIC_CERT = testPathInfo.getCertFilename();
		final String PRIVATE_CERT = testPathInfo.getPrivateKeyFilename();

		LOGGER.debug("Cert path: " + CERT_PATH);
		LOGGER.debug("Public cert: " + PUBLIC_CERT);
		LOGGER.debug("Private cert: " + PRIVATE_CERT);

		LOGGER.debug("CERT PATH: " + CERT_PATH + "\n"
				+ "PUBLIC CERT: " + PUBLIC_CERT + "\n"
				+ "PRIVATE CERT: " + PRIVATE_CERT);

        String[] decryptCommand = new String[] {
        		"openssl", "cms", "-decrypt", "-in",
        		new File(EMAIL_PATH, emailInfo.getFileLocation()).toString(),
        		"-recip",
        		new File(CERT_PATH, PUBLIC_CERT).toString(),
        		"-inkey",
        		new File(CERT_PATH, PRIVATE_CERT).toString()
        };

        LOGGER.info("Decrypt Command Line Statement: " + StringUtils.join(decryptCommand, " "));

		try {
			StringBuffer errorString = new StringBuffer("");
			result = runCommand(decryptCommand, errorString);

			if (result) {
				resultString.append(testPathInfo.getResultStringIfThisDecrypts());
			} else {
				resultString.append(errorString.toString());
			}

		} catch (Exception e) {
			// TODO We will probably want to better format this result string
			resultString.append(e.toString());
			LOGGER.error("SSL Command Exception.", e);
		}

		LOGGER.info("Decrypt Command Result: " + resultString.toString());

		return result;
	}

	/**
	 * Runs the openSSL on command line using StreamGobbler to process and
	 * flush the err and input streams.  Returns boolean on success /
	 * failure of the command.  Logs error.
	 * @param command
	 * @param errorString
	 * @return
	 * @throws Exception
	 */
	private boolean runCommand(String[] command, StringBuffer errorString)
			throws Exception {

		Runtime rt = Runtime.getRuntime();
		Process commandProcess = rt.exec(command);

        StreamGobbler errorGobbler = new
            StreamGobbler(commandProcess.getErrorStream(), "ERROR", false);

        StreamGobbler outputGobbler = new
            StreamGobbler(commandProcess.getInputStream(), "OUTPUT", false);

        errorGobbler.start();
        outputGobbler.start();

        int result = commandProcess.waitFor();
        errorGobbler.join();
        outputGobbler.join();

        if (result == 0) {
        	return true;
        } else {
        	errorString.append("The command: ");

            for (int i = 0; i < command.length; i++) {
            	errorString.append(" " + command[i]);
            }

            errorString.append("\nfailed with result code: " + result);
        	errorString.append("\nError details: "
        		+ errorGobbler.getBufferedOutput().toString());

        	LOGGER.error(errorString);
        	return false;
        }
	}

	/**
	 * Handles input and error stream for calling command line executable.
	 *
	 */
	class StreamGobbler extends Thread {

		private InputStream is;
	    private String type;
	    private boolean print;
	    private StringBuffer buffer = new StringBuffer("");

	    public StreamGobbler(InputStream is, String type, boolean print) {
	        this.is = is;
	        this.type = type;
	        this.print = print;
	    }

	    @Override
	    public void run() {
	        InputStreamReader isr = new InputStreamReader(is);
	        BufferedReader br = new BufferedReader(isr);
	    	
	        try {
	            String line = null;
	            while ((line = br.readLine()) != null) {
	            	buffer.append(type + ">" + line + "\n");
	            	if (print) {
	            		LOGGER.debug(type + ">" + line);
	            	}
	            }
            }catch (IOException e) {
            	LOGGER.error("Error with openSSL stream gobbler.", e);
            }finally {
            	closeStream(br);
            	closeStream(isr);
            	closeStream(this.is);	
            }
	    }

	    public StringBuffer getBufferedOutput() {
			return buffer;
		}
	    
	    private void closeStream(Closeable stream){
	    	try{
	    		stream.close();
	    	}catch(IOException e){
	    		LOGGER.error(e);
	    	}
	    }
	}

}

