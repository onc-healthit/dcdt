package gov.hhs.onc.dcdt.utils.beans.dns;

import gov.hhs.onc.dcdt.utils.annotations.ConfigBean;

@ConfigBean("dns/soa")
public class SoaRecord extends DnsRecord
{
	private String nameServer;
	private String hostMaster;
	private int expire;
	private int minimum;
	private int refresh;
	private int serial;
	private int retry;
}