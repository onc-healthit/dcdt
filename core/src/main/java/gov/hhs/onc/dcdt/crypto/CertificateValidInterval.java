package gov.hhs.onc.dcdt.crypto;

import java.util.Date;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;

public class CertificateValidInterval
{
	private Date start;
	private Date end;
	
	private CertificateValidInterval(Date start, Date end)
	{
		this.start = start;
		this.end = end;
	}
	
	public static CertificateValidInterval forSeconds(int seconds)
	{
		return forSeconds(null, seconds);
	}
	
	public static CertificateValidInterval forSeconds(Date start, int seconds)
	{
		start = ObjectUtils.defaultIfNull(start, new Date());
		
		return new CertificateValidInterval(start, DateUtils.addSeconds(start, seconds));
	}
	
	public static CertificateValidInterval forMinutes(int minutes)
	{
		return forMinutes(null, minutes);
	}
	
	public static CertificateValidInterval forMinutes(Date start, int minutes)
	{
		start = ObjectUtils.defaultIfNull(start, new Date());
		
		return new CertificateValidInterval(start, DateUtils.addMinutes(start, minutes));
	}
	
	public static CertificateValidInterval forHours(int hours)
	{
		return forHours(null, hours);
	}
	
	public static CertificateValidInterval forHours(Date start, int hours)
	{
		start = ObjectUtils.defaultIfNull(start, new Date());
		
		return new CertificateValidInterval(start, DateUtils.addHours(start, hours));
	}

	public static CertificateValidInterval forDays(int days)
	{
		return forDays(null, days);
	}
	
	public static CertificateValidInterval forDays(Date start, int days)
	{
		start = ObjectUtils.defaultIfNull(start, new Date());
		
		return new CertificateValidInterval(start, DateUtils.addDays(start, days));
	}
	
	public static CertificateValidInterval forWeeks(int weeks)
	{
		return forWeeks(null, weeks);
	}
	
	public static CertificateValidInterval forWeeks(Date start, int weeks)
	{
		start = ObjectUtils.defaultIfNull(start, new Date());
		
		return new CertificateValidInterval(start, DateUtils.addWeeks(start, weeks));
	}
	
	public static CertificateValidInterval forMonths(int months)
	{
		return forMonths(null, months);
	}
	
	public static CertificateValidInterval forMonths(Date start, int months)
	{
		start = ObjectUtils.defaultIfNull(start, new Date());
		
		return new CertificateValidInterval(start, DateUtils.addMonths(start, months));
	}
	
	public static CertificateValidInterval forYears(int years)
	{
		return forYears(null, years);
	}
	
	public static CertificateValidInterval forYears(Date start, int years)
	{
		start = ObjectUtils.defaultIfNull(start, new Date());
		
		return forInterval(start, DateUtils.addYears(start, years));
	}
	
	public static CertificateValidInterval expired()
	{
		return forInterval(null, null);
	}
	
	public static CertificateValidInterval forInterval(Date start, Date end)
	{
		Date now = new Date();
		
		return new CertificateValidInterval(ObjectUtils.defaultIfNull(start, now), 
			ObjectUtils.defaultIfNull(end, now));
	}
	
	//<editor-fold desc="Accessor methods">
	public Date getEnd()
	{
		return this.end;
	}

	public Date getStart()
	{
		return this.start;
	}
	//</editor-fold>
}