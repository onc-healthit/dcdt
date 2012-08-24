package gov.onc.certLookup.ldap;

import gov.onc.certLookup.CertLookUpException;
import gov.onc.certLookup.CertLookUpFactory;
import gov.onc.certLookup.CertificateInfo;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.xbill.DNS.SRVRecord;


public class LdapCertLookUp implements CertLookUpFactory{

	private String base;
	private String filter;
	private Properties env;
	private SearchControls sc;
	private Boolean success;
	
	public CertificateInfo execute(CertificateInfo certInfo)
			throws CertLookUpException {
		
		initialize(certInfo);
		
		Set<Integer> prior = certInfo.getSortedSRVs().keySet();
		List<Integer> priorities = asSortedList(prior);

		for(int i = 0; i<priorities.size(); i++){
			int priority = priorities.get(i);
			Vector<SRVRecord> srvRecords = certInfo.getSortedSRVs().get(priority);
			int sum = weightSum(srvRecords);
			int randomNum = (int)(Math.random()*sum);
			SRVRecord srvRec = getSRVforWeight(srvRecords, randomNum);
			Attribute userCert = getCertFromLdap(srvRec, certInfo);
			
			if(userCert != null){
				
				 if (certInfo.getTestCase() == 7) {
					 
					 certInfo.setResult("Fail: Certificate found at LDAP for " + certInfo.getOrigAddr());
					 
				 }else {
					 
			         certInfo.setResult("Success: Certificate found at LDAP for " + certInfo.getOrigAddr());
				 }
				 
			    this.success = true;
				String certResult = getCertResult(userCert);
				System.out.println("Service Record: " + srvRec.toString());
				if(certResult == null)
					certInfo.setCertOutput("Problem Loading Certificate");
				else certInfo.setCertOutput(certResult);
				
				break;
			}else {
				System.out.println("Unable to find certificate at LDAP for: " 
						+ certInfo.getOrigAddr() + "\n"
						+ srvRec.toString());
			}
		}
		
		if(success == false) {
			
			  if (certInfo.getTestCase() == 7) {
					  

				    throw new CertLookUpException("Success: Unable to find Certificate for " + certInfo.getOrigAddr()
					    	+ " at LDAP.", new Throwable());
					  
				  }
				
			
				throw new CertLookUpException("Fail: Unable to find Certificate for " + certInfo.getOrigAddr()
						+ " at LDAP.", new Throwable());
				
			}
		
		
		return certInfo;
	}
	
	private void initialize(CertificateInfo certInfo){
		base = "ou=system";
		
		if(!certInfo.getIsDomainTest())
			filter = "mail=" + certInfo.getOrigAddr();
		else{
			String mailDom = certInfo.getDomain();
			filter = "mail=" + mailDom.substring(0,mailDom.length()-1);
		}
		
		env = new Properties();
		env.put(DirContext.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		sc = new SearchControls();
		sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
		success = false;
	}
	
	private int weightSum(Vector<SRVRecord> srvRecords){
		int sum = 0;
		
		for(SRVRecord rec : srvRecords){
			sum += rec.getWeight();
		}
		return sum;
	}
	
	private SRVRecord getSRVforWeight(Vector<SRVRecord> records, int weight){
		
		for(int i = 0; i<records.size(); i++){
			SRVRecord rec = records.get(i);
			if(rec.getWeight() >= weight){
				records.remove(i);
				return rec;
			}				
		}
		return getSRVforWeight(records, (int)Math.random() * weight);
	}
	
	private Attribute getCertFromLdap(SRVRecord srvRec, CertificateInfo certInfo) throws CertLookUpException{

		String targetDomain = srvRec.getTarget().toString();
		try {
			
			env.put(DirContext.PROVIDER_URL, "ldap://" + targetDomain + ":" + srvRec.getPort());
			DirContext dc = new InitialDirContext(env);
			
			NamingEnumeration ne = null;
			ne = dc.search(base, filter, sc);
			SearchResult searchResult;
			
			dc.close();		
			
			if (ne.hasMore()) {;
				searchResult = (SearchResult)ne.next();
				
				System.out.println("Search Result: " + searchResult.toString() + "\n\n");
				Attributes localAttributes = ((SearchResult) searchResult).getAttributes();
				Attribute localAttribute = localAttributes.get("userCertificate");
				return localAttribute;
			}else return null;
		} catch (NamingException e) {
			System.out.println("No Results for: " + targetDomain + "\nProblem: " + e.getLocalizedMessage() + "  " + e.getCause());
		}return null;
	}
	
	private String getCertResult(Attribute attr) throws CertLookUpException{
		
		ByteArrayInputStream bais = null;
		try {
		
			X509Certificate localX509Certificate = null;
			byte[] byteArray = (byte[])attr.get();
		
			CertificateFactory localCertificateFactory = CertificateFactory.getInstance("X.509");

			bais = new ByteArrayInputStream(byteArray);
			
			while (bais.available() > 0) {
				localX509Certificate = (X509Certificate)localCertificateFactory.generateCertificate(bais);
			}
      
			if(localX509Certificate!=null)
				return localX509Certificate.toString();
			else return null;
		
		} catch (NamingException e) {
			throw new CertLookUpException(e.getMessage(), e.getCause());
		} catch (CertificateException e) {
			throw new CertLookUpException(e.getMessage(), e.getCause());
		}finally{
			closeStreamQuietly(bais);
		}
	}
	
	private void closeStreamQuietly(Closeable stream){
		try{
			if(stream != null)
				stream.close();
		}catch(IOException e){
			//TODO do nothing for now (add logging functionality later)
		}
	}
	
	public static
	<T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  return list;
	}


	
}
