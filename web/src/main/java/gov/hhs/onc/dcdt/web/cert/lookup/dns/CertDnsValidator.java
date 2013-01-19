package gov.hhs.onc.dcdt.web.cert.lookup.dns;

import gov.hhs.onc.dcdt.web.cert.lookup.CertLookUpException;
import gov.hhs.onc.dcdt.web.cert.lookup.CertLookUpFactory;
import gov.hhs.onc.dcdt.web.cert.lookup.CertificateInfo;

import java.util.StringTokenizer;

import org.xbill.DNS.Record;


public class CertDnsValidator implements CertLookUpFactory{

	public CertificateInfo execute(CertificateInfo certInfo)
			throws CertLookUpException {
		
		if(certInfo.getDnsRecord() == null)
			throw new CertLookUpException("Fail: Certificate not found in DNS for "
					+ certInfo.getDomain() + ".", new Throwable());
		else validateCertType(certInfo);
		
		return certInfo;
	}
	
	private void validateCertType(CertificateInfo certInfo) throws CertLookUpException{
		
		Record rec = certInfo.getDnsRecord()[0];
		String dnsRecord = rec.rdataToString();
		StringTokenizer st = new StringTokenizer(dnsRecord);
		
		int certType = Integer.parseInt(st.nextToken());
		
		if(certType==1 || certType==4){
			certInfo.setResult("Success: Certificate found at DNS for " + certInfo.getOrigAddr());
		}else throw new CertLookUpException("Fail: Certificate Type Incorrect.  Value: " 
			+ certType + ".", new Throwable());
		
	}
	
}
