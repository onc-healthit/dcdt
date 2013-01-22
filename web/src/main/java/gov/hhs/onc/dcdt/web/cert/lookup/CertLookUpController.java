package gov.hhs.onc.dcdt.web.cert.lookup;

import gov.hhs.onc.dcdt.web.cert.lookup.dns.CertDecrypt;
import gov.hhs.onc.dcdt.web.cert.lookup.dns.CertDnsValidator;
import gov.hhs.onc.dcdt.web.cert.lookup.ldap.LdapCertLookUp;
import gov.hhs.onc.dcdt.web.cert.lookup.ldap.LdapRecordSorter;

import java.util.LinkedList;
import java.util.Queue;
import org.apache.log4j.Logger;

public class CertLookUpController {
	private final static Logger LOGGER = Logger.getLogger(CertLookUpController.class);
	
	private CertificateInfo certificateInfo;
	private Queue<CertLookUpFactory> nodes;
	
	public CertLookUpController(CertificateInfo certificateInfo){
		super();
		this.certificateInfo = certificateInfo;
		nodes = new LinkedList<CertLookUpFactory>();
	}
	
	public void delegate(){
		if(!certificateInfo.getIsLDAPTest()){
			nodes.add(new CertDomainParser());
			nodes.add(new CertMessenger());
			nodes.add(new CertDnsValidator());
			nodes.add(new CertDecrypt());
		}else{
			nodes.add(new CertDomainParser());
			nodes.add(new CertMessenger());
			nodes.add(new LdapRecordSorter());
			nodes.add(new LdapCertLookUp());
		}
	}
	
	public CertificateInfo run(){
		try {
			setTestTypes();
			delegate();
			while(!nodes.isEmpty())
				nodes.remove().execute(this.certificateInfo);
		} catch (CertLookUpException e) {
			this.certificateInfo.setResult(e.getMessage());
			this.certificateInfo.setCertOutput("");
		}
		return this.certificateInfo;
	}
	
	private void setTestTypes() throws CertLookUpException{
		switch(certificateInfo.getTestCase()){
			case(1):
				certificateInfo.setIsDomainTest(false);
				certificateInfo.setIsLDAPTest(false);
				break;
			case(2):
				certificateInfo.setIsDomainTest(true);
				certificateInfo.setIsLDAPTest(false);
				break;
			case(3):
				certificateInfo.setIsDomainTest(false);
				certificateInfo.setIsLDAPTest(false);
				break;
			case(4):
				certificateInfo.setIsDomainTest(false);
				certificateInfo.setIsLDAPTest(true);
				break;
			case(5):
				certificateInfo.setIsDomainTest(false);
				certificateInfo.setIsLDAPTest(true);
				break;
			case(6):
				certificateInfo.setIsDomainTest(true);
				certificateInfo.setIsLDAPTest(true);
				break;
			case(7):
				certificateInfo.setIsDomainTest(false);
				certificateInfo.setIsLDAPTest(true);
				break;
			default:
				throw new CertLookUpException("Error:  Incorrect Test Case.");
		}
	}
}
