package gov.hhs.onc.dcdt.web.cert.lookup;

import java.io.IOException;
import java.net.UnknownHostException;

import org.xbill.DNS.DClass;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.Section;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

public class CertMessenger implements CertLookUpFactory{

	private IDNSInterface dnsInterface;

	/**
	 * Default uses "real" DNS interface implementation
	 */
	public CertMessenger() {
		super();
		this.dnsInterface = new DNSInterface();
	}

	/**
	 * @param dnsInterface
	 * Use this for providing stub/mock
	 */
	public CertMessenger(IDNSInterface dnsInterface) {
		super();
		this.dnsInterface = dnsInterface;
	}

	public CertificateInfo execute(CertificateInfo certInfo)
			throws CertLookUpException {
		
		try {
			int dnsType;
			
			if(certInfo.getIsLDAPTest()){
				dnsType = Type.SRV;
				certInfo.setDnsRecord(srvLookUp(certInfo));
			}	
			else{
				dnsType = Type.CERT;
				Record outbound;
				outbound = Record.newRecord(new Name(certInfo.getDomain()), dnsType, DClass.IN);
				certInfo.setDnsRecord(sendMessage(outbound));
			}
		} catch (TextParseException e) {
			throw new CertLookUpException(e.getMessage(), e.getCause());
		}
		
		return certInfo;
	}
	
	private Record[] sendMessage(Record outbound) throws CertLookUpException{
		
		try {
			Resolver res;
			Message query = Message.newQuery(outbound);
			Message response = dnsInterface.sendQueryViaResolver(query);
			Record[] answers = response.getSectionArray(Section.ANSWER);
			if(answers.length > 0)
				return answers;
			else throw new CertLookUpException("Fail no answer.", new Throwable());
		
		} catch (UnknownHostException e) {
			throw new CertLookUpException(e.getMessage(),e.getCause());
		} catch (IOException e) {
			throw new CertLookUpException(e.getMessage(),e.getCause());
		}
			
	}

	private Record[] srvLookUp(CertificateInfo certInfo) throws CertLookUpException{
		try{
			String query;
			if(certInfo.getIsDomainTest())
				query = "_ldap._tcp." + certInfo.getDomain();
			else query = "_ldap._tcp." + certInfo.getDomLdap();
			
			Record[] answers = dnsInterface.queryForSrvViaLookup(query); 
			if(answers != null){
				return answers;
			}else throw new CertLookUpException("Fail: null answer.", new Throwable());
        }catch (TextParseException e) {
				// TODO Auto-generated catch block
				throw new CertLookUpException(e.getMessage(),e.getCause());
		}
	}

}
