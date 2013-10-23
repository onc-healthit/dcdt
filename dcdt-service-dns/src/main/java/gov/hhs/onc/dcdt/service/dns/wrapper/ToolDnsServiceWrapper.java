package gov.hhs.onc.dcdt.service.dns.wrapper;


import gov.hhs.onc.dcdt.service.dns.ToolDnsService;
import gov.hhs.onc.dcdt.service.wrapper.ToolServiceWrapper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ToolDnsServiceWrapper extends ToolServiceWrapper<ClassPathXmlApplicationContext, ToolDnsService> {
    public ToolDnsServiceWrapper() {
        super();
    }

    public ToolDnsServiceWrapper(String ... args) {
        super(args);
    }

    public static void main(String ... args) {
        new ToolDnsServiceWrapper(args).start();
    }

    @Override
    protected ToolDnsService createService() {
        return new ToolDnsService();
    }
}
