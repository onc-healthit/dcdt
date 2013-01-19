package gov.hhs.onc.dcdt.web.cert.lookup.ldap;

import gov.hhs.onc.dcdt.web.cert.lookup.CertLookUpException;
import gov.hhs.onc.dcdt.web.cert.lookup.CertLookUpFactory;
import gov.hhs.onc.dcdt.web.cert.lookup.CertificateInfo;
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
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xbill.DNS.SRVRecord;


/**
 * @author daviddegroot
 *
 */
public class LdapCertLookUp implements CertLookUpFactory{

	private String base;
	private String filter;
	private String directoryFilter;
	private String directoryBase;
	private Properties env;
	private SearchControls sc1;
	private SearchControls sc2;
	private Boolean success;
	
	private Logger log = Logger.getLogger("certDiscoveryLogger");
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
				 log.debug("Unable to find certificate at LDAP for: " 
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
		directoryBase = "";
		directoryFilter = "objectClass=*";
		if(!certInfo.getIsDomainTest())
			filter = "mail=" + certInfo.getOrigAddr();
		else{
			String mailDom = certInfo.getDomain();
			filter = "mail=" + mailDom.substring(0,mailDom.length()-1);
		}
		
		env = new Properties();
		env.put(DirContext.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(DirContext.REFERRAL, "follow");
		env.put(DirContext.SECURITY_AUTHENTICATION, "none");
		env.put("java.naming.ldap.attributes.binary", "userCertificate, usercertificate");
		sc1 = new SearchControls();
		// set the scope of the first directory search
		sc1.setSearchScope(SearchControls.OBJECT_SCOPE);
		sc1.setReturningAttributes(new String[] { "namingContexts" });
		sc1.setReturningObjFlag(true);
		sc2 = new SearchControls();
		// set the scope of the search on each found directory
		sc2.setSearchScope(SearchControls.SUBTREE_SCOPE);
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
		String targetDomain = StringUtils.removeEnd(srvRec.getTarget().toString(), ".");
		
		try {
			   env.put(DirContext.PROVIDER_URL, "ldap://" + targetDomain + ":" + srvRec.getPort());
			
			
			log.debug("ldap://" + targetDomain + ":" + srvRec.getPort());
			
			DirContext dc = new InitialDirContext(env);
			NamingEnumeration directoryNE = null;
			
			// Here we set the search for the root directory, searching for any objectClass matches, and the scope is set using the sc1 variable (see above)
			directoryNE= dc.search("", "objectClass=*", sc1);
			
			log.debug("SC1 :" + sc1);
						
			while (directoryNE.hasMore()){
                SearchResult result1 = (SearchResult) directoryNE.next();
				NamingEnumeration<?> result1Attrs = result1.getAttributes().get("namingContexts").getAll();
				String result1BaseDn;
				
				while (result1Attrs.hasMore())
				{
					result1BaseDn = ObjectUtils.toString(result1Attrs.next());
					
					// print DN of each directory found.
					log.debug("result1BaseDn: " + result1BaseDn);
	
					Attribute foundMail = findMailAttribute(result1BaseDn); 
					
					if(foundMail != null){
						return foundMail;
					}
				}
			}		
			dc.close();	
	} catch (NamingException e) {
		log.debug("No Results for: " + targetDomain, e);
	} return null;

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
	
	private Attribute findMailAttribute(String directoryName) throws NamingException{
		Attribute localAttribute = null;
		DirContext dc = new InitialDirContext(env);
		NamingEnumeration e = dc.search(directoryName, filter, sc2);
		
		while (e.hasMore()) {

            SearchResult nodeResult = (SearchResult) e.next();
    		log.debug("Search Result: " + nodeResult.toString() + "\n\n");
            Attributes localAttributes = ((SearchResult) nodeResult).getAttributes();
          
            localAttribute = (Attribute)ObjectUtils.defaultIfNull(localAttributes.get("userCertificate;binary"), 
				localAttributes.get("userCertificate"));
			
            log.debug("localAttribute: " + localAttribute);
   
            if(localAttribute != null)
            	return localAttribute;
        }
		
		return null;
	}
	
	public static
	<T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  return list;
	}


	
}

