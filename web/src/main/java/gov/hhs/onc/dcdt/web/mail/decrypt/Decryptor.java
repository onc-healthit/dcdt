package gov.hhs.onc.dcdt.web.mail.decrypt;

import gov.hhs.onc.dcdt.beans.entry.Entry;
import gov.hhs.onc.dcdt.beans.testcases.TestcaseResultStatus;
import gov.hhs.onc.dcdt.beans.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.beans.testcases.discovery.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.web.startup.ConfigInfo;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.EnumSet;
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
	public EmailBean execute(EmailBean emailInfo) throws Exception
	{
		DiscoveryTestcase testcase = emailInfo.getTestcase();
		StringBuilder msgBuilder = new StringBuilder();
		
		for (TestcaseResultStatus status : EnumSet.allOf(TestcaseResultStatus.class))
		{
			if (testcase.hasResults(status))
			{
				for (DiscoveryTestcaseResult result : testcase.getResults(status))
				{
					msgBuilder.setLength(0);
					
					if (this.decryptEmail(emailInfo, result, msgBuilder))
					{
						emailInfo.setResult(result);
						emailInfo.setResultMsg(result.getMsg());
						emailInfo.setResultStatus(result.getStatus());
						
						return emailInfo;
					}
				}
			}
		}
		
		emailInfo.setResultStatus(TestcaseResultStatus.PASS);
		
		return emailInfo;
	}

	/**
	 * Decpypts email using openSSL command line tool.
	 * @param emailInfo
	 * @param testcaseResult
	 * @param msgBuilder
	 * @return boolean
	 */
	private boolean decryptEmail(EmailBean emailInfo, DiscoveryTestcaseResult testcaseResult, StringBuilder msgBuilder) {

		boolean result = false;
		File mailFile = emailInfo.getFile();
		
		String certsDirPath = ConfigInfo.getConfigProperty("CertLocation");
		String entryProp = testcaseResult.getEntryProperty();
		String certFilePath = Entry.getCertPemFilePath(ConfigInfo.getConfigProperty(entryProp), certsDirPath);
		String keyFilePath = Entry.getKeyPemFilePath(ConfigInfo.getConfigProperty(entryProp), certsDirPath);

		LOGGER.debug("Decrypting mail file (path=" + mailFile + "): cert=" + certFilePath + 
			", privateKey=" + keyFilePath);

        String[] decryptCommand = new String[] {
        		"openssl", "cms", "-decrypt", "-in",
        		mailFile.getAbsolutePath(),
        		"-recip",
        		certFilePath,
        		"-inkey",
        		keyFilePath
        };

        LOGGER.info("Decrypt command line: " + StringUtils.join(decryptCommand, " "));

		try {
			StringBuffer errorString = new StringBuffer("");
			result = runCommand(decryptCommand, errorString);

			if (result) {
				msgBuilder.append(testcaseResult.getMsg());
			} else {
				msgBuilder.append(errorString);
			}

		} catch (Exception e) {
			// TODO We will probably want to better format this result string
			msgBuilder.append(e);
			LOGGER.error("SSL Command Exception.", e);
		}

		LOGGER.info("Decrypt Command Result: " + msgBuilder);

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

