package gov.hhs.onc.dcdt.email.property;

import gov.hhs.onc.dcdt.startup.ConfigInfo;

import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;


/**
 * Adds in properties to the ConfigInfo email properties with checks on 
 * email addresses validity.
 * @author jasonsmith
 *
 */
public class EmailPropertyHandler {

	private Logger log = Logger.getLogger("emailMessageLogger");
	
	/**
	 * Sets email property with Direct and non-Direct email in ConfigInfo
	 * @param directE
	 * @param resultE
	 */
	public void setProperty(String directE, String resultE) {
		
		ConfigInfo.setEmailProperty(directE, resultE);
	}
	
	/**
	 * Gets non-Direct email from ConfigInfo with Direct email value.
	 * @param directE
	 * @return
	 */
	public String getProperty(String directE){
				
		return ConfigInfo.getEmailProperty(directE);
	}
	
	/**
	 * Validates if email address given corresponds to a mail server.
	 * @param domain
	 * @return
	 */
	public Boolean isEmailValid(String domain){
		log.debug("before email address validity check for: " + domain);
		try{
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

			DirContext ictx = new InitialDirContext(env);

			Attributes attrs = ictx.getAttributes(domain, new String[] {"MX"});
			Attribute attr = attrs.get("MX");
			
			ictx.close();
			
			if (attr == null){
				log.debug("Invalid email domain: " + domain);
				return false;
			}
			else{
				if(attr.size() > 0){
					log.debug("Valid email domain: " + domain);
					return true;
				}
				else {
					log.debug("Invalid email domain: " + domain);
					return false;
				}
			}
		}catch(NamingException e){			
			log.error("Error in email lookup.  Email: " + domain + " not saved.");
			return false;
		}
	}
	
	/**
	 * Strips domain from email address, validates whether it is in the correct
	 * format.  Returns the domain.
	 * @param emailAddr
	 * @return
	 * @throws Exception
	 */
	public String stripDomain(String emailAddr) throws Exception{
		log.debug("before domain stripped from email address: " + emailAddr);
		if(emailAddr.contains("@")){
			String domainName = null;
			StringTokenizer tok = new StringTokenizer(emailAddr,"@");
			
			tok.nextToken();
			while(tok.hasMoreTokens()){
				domainName = tok.nextToken();
			}
			if(domainName != null && domainName != ""){
				log.debug("domain successfully stripped with domain: " + domainName);
				return domainName;
			}
			else throw new Exception("Domain Name is incorrect.",new Throwable());
		}else throw new Exception("Email Address doesn't contain @", new Throwable());
	}

}
