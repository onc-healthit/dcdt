package gov.hhs.onc.dcdt.dns.config;

import org.xbill.DNS.Name;
import org.xbill.DNS.SOARecord;

public interface SoaRecordConfig extends DnsRecordConfig<SOARecord> {
    public Name getAdmin();

    public void setAdmin(Name admin);

    public long getExpire();

    public void setExpire(long expire);

    public Name getHost();

    public void setHost(Name host);

    public long getMinimum();

    public void setMinimum(long min);

    public long getRefresh();

    public void setRefresh(long refresh);

    public long getRetry();

    public void setRetry(long retry);

    public long getSerial();

    public void setSerial(long serial);
}
