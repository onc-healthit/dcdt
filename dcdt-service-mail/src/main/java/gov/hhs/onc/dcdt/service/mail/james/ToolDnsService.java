package gov.hhs.onc.dcdt.service.mail.james;

import gov.hhs.onc.dcdt.service.mail.james.config.BeanConfigurable;
import gov.hhs.onc.dcdt.service.mail.james.config.DnsServiceConfigBean;
import org.apache.james.dnsservice.api.DNSService;
import org.springframework.beans.factory.InitializingBean;

public interface ToolDnsService extends BeanConfigurable<DnsServiceConfigBean>, DNSService, InitializingBean {
}
