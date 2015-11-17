package gov.hhs.onc.dcdt.mail.sender;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.dns.lookup.DnsNameService;
import gov.hhs.onc.dcdt.mail.MailEncoding;
import javax.annotation.Nonnegative;

public interface MailSenderService extends ToolBean {
    @Nonnegative
    public int getConnectTimeout();

    public void setConnectTimeout(@Nonnegative int connTimeout);

    public DnsNameService getDnsNameService();

    public void setDnsNameService(DnsNameService dnsNameService);

    public MailEncoding getEncoding();

    public void setEncoding(MailEncoding enc);

    @Nonnegative
    public int getReadTimeout();

    public void setReadTimeout(@Nonnegative int readTimeout);
}
