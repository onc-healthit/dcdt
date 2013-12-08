package gov.hhs.onc.dcdt.service.dns.wrapper;

import gov.hhs.onc.dcdt.service.dns.DnsService;
import gov.hhs.onc.dcdt.service.wrapper.ToolServiceWrapper;

public class DnsServiceWrapper extends ToolServiceWrapper<DnsService> {
    public DnsServiceWrapper() {
        super();
    }

    public DnsServiceWrapper(String ... args) {
        super(args);
    }

    public static void main(String ... args) {
        new DnsServiceWrapper(args).start();
    }

    @Override
    protected DnsService createService() {
        return new DnsService();
    }
}
