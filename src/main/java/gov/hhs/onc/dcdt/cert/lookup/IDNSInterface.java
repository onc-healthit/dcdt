package gov.hhs.onc.dcdt.cert.lookup;

import java.io.IOException;
import java.net.UnknownHostException;

import org.xbill.DNS.Message;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;

/**
 * @author joelamy
 * This interface exists to remove external dependencies on dnsjava calls,
 * so that the surrounding code can be more easily tested.
 * The intent is that the "real" implementation class will contain the external calls
 * only and no additional logic. Stub/mock classes for testing can then be used.
 */
public interface IDNSInterface {

	public abstract Message sendQueryViaResolver(Message query)
			throws UnknownHostException, IOException;

	public abstract Record[] queryForSrvViaLookup(String query)
			throws TextParseException;

}