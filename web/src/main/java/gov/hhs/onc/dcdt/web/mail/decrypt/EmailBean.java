package gov.hhs.onc.dcdt.web.mail.decrypt;

import java.io.Serializable;

/**
 * Bean for email information including to/from addresses, file location of
 * email, the test case value, and the results of the email decryption.
 * @author jasonsmith
 *
 */
public class EmailBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fileLocation;
	private String toAddress;
	private String fromAddress;
	private LookupTest thisTest;
	private Boolean passes;
	private String results;

	public EmailBean() {
		super();
	}

	public EmailBean(String fileLocation) {
		super();
		this.fileLocation = fileLocation;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public LookupTest getThisTest() {
		return thisTest;
	}

	public void setThisTest(LookupTest thisTest) {
		this.thisTest = thisTest;
	}

	public Boolean getPasses() {
		return passes;
	}

	public void setPasses(Boolean passes) {
		this.passes = passes;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

}
