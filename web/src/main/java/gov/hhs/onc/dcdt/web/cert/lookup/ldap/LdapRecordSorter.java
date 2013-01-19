package gov.hhs.onc.dcdt.web.cert.lookup.ldap;

import gov.hhs.onc.dcdt.web.cert.lookup.CertLookUpException;
import gov.hhs.onc.dcdt.web.cert.lookup.CertLookUpFactory;
import gov.hhs.onc.dcdt.web.cert.lookup.CertificateInfo;

import java.util.HashMap;
import java.util.Vector;

import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;


public class LdapRecordSorter implements CertLookUpFactory{

	
	public CertificateInfo execute(CertificateInfo certInfo)
			throws CertLookUpException {
		
		HashMap<Integer,Vector<SRVRecord>> srvPrioritized = new HashMap<Integer,Vector<SRVRecord>>();
		
		for(int i = 0; i<certInfo.getDnsRecord().length; i++){
			Record record = certInfo.getDnsRecord()[i];
			SRVRecord srv = (SRVRecord)record;
			int priority = srv.getPriority();
			if(srvPrioritized.containsKey(priority)){
				Vector<SRVRecord> tempRecords = srvPrioritized.get(priority);
				tempRecords.add(srv);
			}else{
				Vector<SRVRecord> newRecords = new Vector<SRVRecord>();
				newRecords.add(srv);
				srvPrioritized.put(priority, newRecords);
			}
		}
		
		certInfo.setSortedSRVs(srvPrioritized);
		
		return certInfo;
	}
	

}
