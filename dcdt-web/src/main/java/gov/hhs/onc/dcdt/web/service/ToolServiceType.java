package gov.hhs.onc.dcdt.web.service;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;
import gov.hhs.onc.dcdt.beans.ToolTypeIdentifier;
import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.dns.DnsService;
import gov.hhs.onc.dcdt.service.http.HttpService;
import gov.hhs.onc.dcdt.service.ldap.LdapService;
import gov.hhs.onc.dcdt.service.mail.MailService;

public enum ToolServiceType implements ToolIdentifier, ToolTypeIdentifier {
    DNS(DnsService.class), HTTP(HttpService.class), LDAP(LdapService.class), MAIL(MailService.class);

    private final String id;
    private final Class<? extends ToolService<?, ?>> type;

    private ToolServiceType(Class<? extends ToolService<?, ?>> type) {
        this.id = this.name();
        this.type = type;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Class<? extends ToolService<?, ?>> getType() {
        return this.type;
    }
}
