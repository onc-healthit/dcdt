package gov.hhs.onc.dcdt.web.cert.lookup;

import gov.hhs.onc.dcdt.beans.testcases.TestcaseResultStatus;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;

/**
 * @author Jason Smith
 *
 */
public class CertificateInfo implements Serializable{

	private String origAddr;
	private String testCase;
	private String domLdap;
	private String domain;
	private TestcaseResultStatus status;
	private String result;
	private String certOutput;
	private Boolean isDomainTest;
	private Boolean isLDAPTest;
	private Record[] dnsRecord;
	private Record ldapRecord;
	private HashMap<Integer,Vector<SRVRecord>> sortedSRVs;
	
	
	public CertificateInfo() {
		super();
	}
	
	
	public CertificateInfo(String testCase, String domain) {
		super();
		this.origAddr = domain;
		this.testCase = testCase;
		this.domain = domain;
		this.isDomainTest = null;
		this.isLDAPTest = null;
		result = null;
		certOutput = null;
		dnsRecord = null;
		ldapRecord = null;
		domLdap = null;
		sortedSRVs = null;
	}

	
	public String getOrigAddr() {
		return origAddr;
	}

	public void setOrigAddr(String origAddr) {
		this.origAddr = origAddr;
	}

	public HashMap<Integer, Vector<SRVRecord>> getSortedSRVs() {
		return sortedSRVs;
	}

	public void setSortedSRVs(HashMap<Integer, Vector<SRVRecord>> sortedSRVs) {
		this.sortedSRVs = sortedSRVs;
	}

	public String getTestCase() {
		return testCase;
	}
	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}

	public TestcaseResultStatus getStatus()
	{
		return this.status;
	}

	public void setStatus(TestcaseResultStatus status)
	{
		this.status = status;
	}

	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getCertOutput() {
		return certOutput;
	}
	public void setCertOutput(String certOutput) {
		this.certOutput = certOutput;
	}
	public Boolean getIsDomainTest() {
		return isDomainTest;
	}
	public void setIsDomainTest(Boolean isDomainTest) {
		this.isDomainTest = isDomainTest;
	}
	public Boolean getIsLDAPTest() {
		return isLDAPTest;
	}
	public void setIsLDAPTest(Boolean isLDAPTest) {
		this.isLDAPTest = isLDAPTest;
	}
	public Record[] getDnsRecord() {
		return dnsRecord;
	}
	public void setDnsRecord(Record[] dnsRecord) {
		this.dnsRecord = dnsRecord;
	}
	public Record getLdapRecord() {
		return ldapRecord;
	}
	public void setLdapRecord(Record ldapRecord) {
		this.ldapRecord = ldapRecord;
	}
	
	public String getDomLdap() {
		return domLdap;
	}

	public void setDomLdap(String address) {
		this.domLdap = address;
	}
	
	
}
