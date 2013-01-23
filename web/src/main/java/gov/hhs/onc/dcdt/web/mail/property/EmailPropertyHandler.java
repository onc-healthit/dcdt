package gov.hhs.onc.dcdt.web.mail.property;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsUtils;
import gov.hhs.onc.dcdt.dns.LookupResult;
import gov.hhs.onc.dcdt.dns.LookupResultType;
import gov.hhs.onc.dcdt.dns.RecordType;
import gov.hhs.onc.dcdt.web.startup.ConfigInfo;
import java.net.InetAddress;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Name;

/**
 * Adds in properties to the ConfigInfo email properties with checks on
 * email addresses validity.
 *
 * @author jasonsmith
 * @author michal.kotelba@esacinc.com
 */
public class EmailPropertyHandler
{
	private transient final static Logger LOGGER = Logger.getLogger(EmailPropertyHandler.class);

	/**
	 * Sets email property with Direct and non-Direct email in ConfigInfo
	 */
	public void setProperty(String directE, String resultE)
	{
		ConfigInfo.setEmailProperty(directE, resultE);
	}

	/**
	 * Gets non-Direct email from ConfigInfo with Direct email value.
	 */
	public String getProperty(String directE)
	{
		return ConfigInfo.getEmailProperty(directE);
	}

	/** Validates if email address given corresponds to a mail server. */
	public boolean isEmailValid(String domain) throws MailPropertyException
	{
		try
		{
			LookupResult<MXRecord> mxLookupResult = DnsUtils.lookup(RecordType.MX, domain);
			
			if (!mxLookupResult.hasRecords())
			{
				LookupResultType mxLookupResultType = mxLookupResult.getResultType();
				
				if (mxLookupResultType.isSuccess())
				{
					throw new MailPropertyException("No DNS MX records found for email domain: " + domain);
				}
				else
				{
					throw new MailPropertyException("DNS MX record(s) lookup for email domain (" + domain + 
						") failed: status=" + mxLookupResultType.getName() + ", error=" + 
						mxLookupResult.getErrorString());
				}
			}

			List<MXRecord> mxRecords = mxLookupResult.getRecords();
			Name mxRecordTarget;
			InetAddress mxRecordTargetAddr;
			
			LOGGER.debug("Found " + mxRecords.size() + " DNS MX records for email domain (" + domain + ").");
			
			for (MXRecord mxRecord : mxRecords)
			{
				mxRecordTarget = mxRecord.getTarget();
				
				if ((mxRecordTargetAddr = DnsUtils.lookupAddress(mxRecordTarget)) != null)
				{
					LOGGER.debug("Resolved DNS MX record target (" + mxRecordTarget + ") address for email domain (" + 
						domain + "): " + mxRecordTargetAddr.getHostAddress());
					
					return true;
				}
				else
				{
					LOGGER.debug("Unable to resolve DNS MX record target (" + mxRecordTarget + 
						") address for email domain (" + domain + ").");
				}
			}
		}
		catch (DnsException e)
		{
			throw new MailPropertyException("Email domain DNS MX record lookup failed.", e);
		}
		
		return false;
	}

	/**
	 * Strips domain from email address, validates whether it is in the correct
	 * format.  Returns the domain.
	 */
	public String stripDomain(String emailAddr) throws Exception
	{
		LOGGER.debug("before domain stripped from email address: " + emailAddr);

		if (emailAddr.contains("@"))
		{
			String domainName = null;
			StringTokenizer tok = new StringTokenizer(emailAddr, "@");

			tok.nextToken();
			while (tok.hasMoreTokens())
			{
				domainName = tok.nextToken();
			}
			if (domainName != null && domainName != "")
			{
				LOGGER.debug("domain successfully stripped with domain: " + domainName);
				return domainName;
			}
			else
			{
				throw new Exception("Domain Name is incorrect.");
			}
		}
		else
		{
			throw new Exception("Email Address doesn't contain @");
		}
	}
}