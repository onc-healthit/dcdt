package gov.hhs.onc.dcdt.dns.lookup;


import gov.hhs.onc.dcdt.dns.ToolDnsException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Cache;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.Section;
import org.xbill.DNS.Type;

public class ToolDnsLookupService {
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

    private static Map<Class<? extends Record>, Integer> RECORD_CLASS_TYPE_MAP = new HashMap<>();

    @Autowired
    protected Cache dnsCache;

    protected ThreadPoolTaskExecutor dnsLookupTaskExecutor;
    protected List<Resolver> dnsResolvers;

    static {
        RECORD_CLASS_TYPE_MAP.put(ARecord.class, Type.A);
    }

    public ToolDnsLookupService() {
        this(Arrays.asList(new Resolver[0]));
    }

    public ToolDnsLookupService(List<Resolver> dnsResolvers) {
        this.dnsResolvers = dnsResolvers;
    }

    public ARecord lookupAddress(Name dnsName) {
        List<ARecord> addresses = this.lookupAddresses(dnsName);

        return !addresses.isEmpty() ? addresses.get(0) : null;
    }

    public List<ARecord> lookupAddresses(Name dnsName) {
        return this.lookup(ARecord.class, dnsName);
    }

    public Map<Name, List<ARecord>> lookupAddresses(List<Name> dnsNames) {
        return this.lookup(ARecord.class, dnsNames);
    }

    public <T extends Record> List<T> lookup(Class<T> recordClass, Name dnsName) {
        return this.lookup(recordClass, Arrays.asList(dnsName)).get(dnsName);
    }

    public <T extends Record> Map<Name, List<T>> lookup(Class<T> recordClass, List<Name> dnsNames) {
        Map<Name, List<T>> answersMap = new LinkedHashMap<>(dnsNames.size());
        List<T> answers;
        Lookup dnsLookup;

        for (Name dnsName : dnsNames) {
            answers = new ArrayList<>();

            try {
                dnsLookup = this.createLookup(dnsName, RECORD_CLASS_TYPE_MAP.get(recordClass));
                this.processLookup(dnsLookup);

                if (dnsLookup.getResult() == Lookup.SUCCESSFUL) {
                    for (Record answer : dnsLookup.getAnswers()) {
                        if (recordClass.isAssignableFrom(answer.getClass())) {
                            answers.add(recordClass.cast(answer));
                        }
                    }
                }

            } catch (Throwable th) {
                LOGGER.error("Unable to perform DNS lookup.", th);
            }

            answersMap.put(dnsName, answers);
        }

        return answersMap;
    }

    public Message lookup(Message inMsg) {
        Record dnsQuestion = inMsg.getQuestion();

        Message outMsg = new Message(inMsg.getHeader().getID());
        outMsg.addRecord(dnsQuestion, Section.QUESTION);

        try {
            Lookup dnsLookup = this.createLookup(dnsQuestion.getName(), Type.A);

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

    protected Lookup createLookup(Name dnsName, int recordType) throws UnknownHostException {
        Lookup dnsLookup = new Lookup(dnsName, recordType);
        dnsLookup.setCache(this.dnsCache);
        dnsLookup.setResolver(new ExtendedResolver(this.dnsResolvers.toArray(new Resolver[this.dnsResolvers.size()])));

        return dnsLookup;
    }
    
    @Required
    @Resource(name = "toolDnsLookupTaskExecutor")
    protected void setDnsLookupTaskExecutor(ThreadPoolTaskExecutor dnsLookupTaskExecutor)
    {
        this.dnsLookupTaskExecutor = dnsLookupTaskExecutor;
    }

    public Cache getDnsCache() {
        return this.dnsCache;
    }

    public void setDnsCache(Cache dnsCache) {
        this.dnsCache = dnsCache;
    }

    public List<Resolver> getDnsResolvers() {
        return this.dnsResolvers;
    }

    public void setDnsResolvers(List<Resolver> dnsResolvers) {
        this.dnsResolvers = dnsResolvers;
    }
}
