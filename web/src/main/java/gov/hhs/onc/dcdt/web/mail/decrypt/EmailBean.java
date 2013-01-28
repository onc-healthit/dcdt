package gov.hhs.onc.dcdt.web.mail.decrypt;

import gov.hhs.onc.dcdt.beans.testcases.TestcaseResultStatus;
import gov.hhs.onc.dcdt.beans.testcases.discovery.DiscoveryTestcase;
import java.io.Serializable;

/**
 * Bean for email information including to/from addresses, file location of
 * email, the test case value, and the results of the email decryption.
 * 
 * @author jasonsmith
 * @author michal.kotelba@esacinc.com
 */
public class EmailBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fileLocation;
	private String toAddress;
	private String fromAddress;
	private DiscoveryTestcase testcase;
	private TestcaseResultStatus status;
	private String results;

	public EmailBean()
	{
		this(null);
	}
	
	public EmailBean(String fileLocation)
	{
		this.fileLocation = fileLocation;
	}
	
	public String getFileLocation()
	{
		return this.fileLocation;
	}

	public void setFileLocation(String fileLocation)
	{
		this.fileLocation = fileLocation;
	}

	public String getFromAddress()
	{
		return this.fromAddress;
	}

	public void setFromAddress(String fromAddress)
	{
		this.fromAddress = fromAddress;
	}

	public String getResults()
	{
		return this.results;
	}

	public void setResults(String results)
	{
		this.results = results;
	}

	public TestcaseResultStatus getStatus()
	{
		return this.status;
	}

	public void setStatus(TestcaseResultStatus status)
	{
		this.status = status;
	}

	public DiscoveryTestcase getTestcase()
	{
		return this.testcase;
	}

	public void setTestcase(DiscoveryTestcase testcase)
	{
		this.testcase = testcase;
	}

	public String getToAddress()
	{
		return this.toAddress;
	}

	public void setToAddress(String toAddress)
	{
		this.toAddress = toAddress;
	}
}