package gov.hhs.onc.dcdt.web.service;

import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.dns.DnsService;
import gov.hhs.onc.dcdt.service.ldap.LdapService;
import gov.hhs.onc.dcdt.service.mail.MailService;

public enum ToolServiceType {
    DNS(DnsService.class), LDAP(LdapService.class), MAIL(MailService.class);

    private final Class<? extends ToolService> serviceClass;

    private ToolServiceType(Class<? extends ToolService> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Class<? extends ToolService> getServiceClass() {
        return this.serviceClass;
    }
}
