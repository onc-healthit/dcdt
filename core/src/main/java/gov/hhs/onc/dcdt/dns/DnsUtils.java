package gov.hhs.onc.dcdt.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.xbill.DNS.Address;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.Options;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;

public abstract class DnsUtils
{
	private final static String DNSJAVA_OPTION_VERBOSE = "verbose";
	
	private final static Logger LOGGER = Logger.getLogger(DnsUtils.class);
	
	static
	{
		Options.set(DNSJAVA_OPTION_VERBOSE, Boolean.toString(true));
	}
	
	public static Map<String, InetAddress> lookupAddressAll(String ... names) throws DnsException
	{
		Map<String, InetAddress> addresses = new LinkedHashMap<>(names.length);
		
		for (String name : names)
		{
			addresses.put(name, lookupAddress(parseName(name)));
		}
		
		return addresses;
	}
	
	public static Map<Name, InetAddress> lookupAddressAll(Name ... names)
	{
		Map<Name, InetAddress> addresses = new LinkedHashMap<>(names.length);
		
		for (Name name : names)
		{
			addresses.put(name, lookupAddress(name));
		}
		
		return addresses;
	}
	
	public static InetAddress lookupAddress(String name) throws DnsException
	{
		return lookupAddress(parseName(name));
	}
	
	public static InetAddress lookupAddress(Name name)
	{
		try
		{
			return Address.getByName(name.toString());
		}
		catch (UnknownHostException e)
		{
			LOGGER.trace("Unable to lookup address for name: " + name, e);
			
			return null;
		}
	}
	
	public static <T extends Record, U extends RecordType<T>> List<LookupResult<T>> lookupAll(U recordType, String ... names)
		throws DnsException
	{
		List<LookupResult<T>> results = new ArrayList<>(names.length);
		
		for (String name : names)
		{
			results.add(lookup(recordType, name));
		}
		
		return results;
	}
	
	public static <T extends Record, U extends RecordType<T>> List<LookupResult<T>> lookupAll(U recordType, Name ... names)
	{
		List<LookupResult<T>> results = new ArrayList<>(names.length);
		
		for (Name name : names)
		{
			results.add(lookup(recordType, name));
		}
		
		return results;
	}
	
	public static <T extends Record, U extends RecordType<T>> LookupResult<T> lookup(U recordType, String name)
		throws DnsException
	{
		return lookup(recordType, parseName(name));
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Record, U extends RecordType<T>> LookupResult<T> lookup(U recordType, Name name)
	{
		Lookup lookup = new Lookup(name, recordType.getType());
		lookup.run();
		
		return new LookupResult<>(name, lookup, recordType.getRecordClass());
	}
	
	public static Name parseName(String name) throws DnsException
	{
		try
		{
			return Name.fromString(name);
		}
		catch (TextParseException e)
		{
			throw new DnsException("Unable to parse DNS name: " + name, e);
		}
	}
}