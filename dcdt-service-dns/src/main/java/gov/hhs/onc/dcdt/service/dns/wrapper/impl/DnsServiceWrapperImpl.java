package gov.hhs.onc.dcdt.service.dns.wrapper.impl;

import gov.hhs.onc.dcdt.dns.DnsTransportProtocol;
import gov.hhs.onc.dcdt.service.dns.DnsService;
import gov.hhs.onc.dcdt.service.dns.config.DnsServerConfig;
import gov.hhs.onc.dcdt.service.dns.impl.DnsServiceImpl;
import gov.hhs.onc.dcdt.service.dns.server.DnsServer;
import gov.hhs.onc.dcdt.service.dns.wrapper.DnsServiceWrapper;
import gov.hhs.onc.dcdt.service.wrapper.impl.AbstractToolServiceWrapper;

public class DnsServiceWrapperImpl extends AbstractToolServiceWrapper<DnsTransportProtocol, DnsServerConfig, DnsServer, DnsService> implements
    DnsServiceWrapper {
    public DnsServiceWrapperImpl(String ... args) {
        super(DnsService.class, DnsServiceImpl.class, args);
    }

    public static void main(String ... args) {
        new DnsServiceWrapperImpl(args).start();
    }
}
