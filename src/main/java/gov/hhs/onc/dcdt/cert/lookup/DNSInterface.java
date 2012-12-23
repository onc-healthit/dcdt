/**
 * 
 */
package gov.hhs.onc.dcdt.cert.lookup;

import java.io.IOException;
import java.net.UnknownHostException;

import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Message;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

/**
 * @author joelamy
 * Real implementation of the IDNSInterface interface.
 * Intent is that this class will contain external calls only and no additional logic.
 * Add any new logic to clients of this class.
 */
public class DNSInterface implements IDNSInterface {

	/* (non-Javadoc)
	 * @see com.nitor.group.cert.IDNSInterface#sendQueryViaResolver(org.xbill.DNS.Message)
	 */
	public Message sendQueryViaResolver(Message query)
			throws UnknownHostException, IOException {
		Resolver res = new ExtendedResolver();
		return res.send(query);
	}
	
	/* (non-Javadoc)
	 * @see com.nitor.group.cert.IDNSInterface#queryForSrvViaLookup(java.lang.String)
	 */
	public Record[] queryForSrvViaLookup(String query)
			throws TextParseException {
		Record[] answers = new Lookup(query, Type.SRV).run();
		return answers;
	}

}
