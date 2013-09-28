package gov.hhs.onc.dcdt.dns;


import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.xbill.DNS.Cache;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.Section;

public abstract class ToolDnsLookupService {
    @DnsResolver(DnsResolverType.LOCAL)
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    @Service("toolDnsLookupServiceLocal")
    private static class ToolDnsLookupServiceLocal extends ToolDnsLookupService {
        @Autowired
        @DnsResolver(DnsResolverType.LOCAL)
        protected void setDnsResolvers(List<Resolver> dnsResolvers) {
            super.setDnsResolvers(dnsResolvers);
        }
    }

    @DnsResolver(DnsResolverType.EXTERNAL)
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    @Service("toolDnsLookupServiceExternal")
    private static class ToolDnsLookupServiceExternal extends ToolDnsLookupService {
        @Autowired
        @DnsResolver(DnsResolverType.EXTERNAL)
        protected void setDnsResolvers(List<Resolver> dnsResolvers) {
            super.setDnsResolvers(dnsResolvers);
        }
    }

    private static class ToolDnsLookupTask implements Runnable {
        private Lookup dnsLookup;

        public ToolDnsLookupTask(Lookup dnsLookup) {
            this.dnsLookup = dnsLookup;
        }

        @Override
        public void run() {
            this.dnsLookup.run();
        }
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolDnsLookupService.class);

    @Autowired
    protected ThreadPoolTaskExecutor dnsLookupTaskExecutor;

    @Autowired
    protected Cache dnsCache;

    protected List<Resolver> dnsResolvers;

    public Message lookup(Message inMsg) {
        Record dnsQuestion = inMsg.getQuestion();

        Message outMsg = new Message(inMsg.getHeader().getID());
        outMsg.addRecord(dnsQuestion, Section.QUESTION);

        try {
            Lookup dnsLookup = this.createLookup(dnsQuestion.getName());

            this.processLookup(dnsLookup);

            if (dnsLookup.getResult() == Lookup.SUCCESSFUL) {
                for (Record dnsAnswer : dnsLookup.getAnswers()) {
                    outMsg.addRecord(dnsAnswer, Section.ANSWER);
                }

                return outMsg;
            } else {
                throw new ToolDnsException("Unable to perform DNS lookup: " + dnsLookup.getErrorString());
            }
        } catch (Throwable th) {
            LOGGER.error("Unable to perform DNS lookup.", th);
        }

        return outMsg;
    }

    protected void processLookup(Lookup dnsLookup) throws InterruptedException, ExecutionException {
        this.dnsLookupTaskExecutor.submit(new ToolDnsLookupTask(dnsLookup)).get();
    }

    protected Lookup createLookup(Name dnsName) throws UnknownHostException {
        Lookup dnsLookup = new Lookup(dnsName);
        dnsLookup.setCache(this.dnsCache);
        dnsLookup.setResolver(new ExtendedResolver(this.dnsResolvers.toArray(new Resolver[this.dnsResolvers.size()])));

        return dnsLookup;
    }

    protected void setDnsResolvers(List<Resolver> dnsResolvers) {
        this.dnsResolvers = dnsResolvers;
    }
}
