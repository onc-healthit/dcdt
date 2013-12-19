package gov.hhs.onc.dcdt.service.dns;


import gov.hhs.onc.dcdt.service.dns.conf.DnsServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.xbill.DNS.Message;

import java.io.Closeable;
import java.io.IOException;

public abstract class DnsSocketServer<T extends Closeable, U extends Closeable, V> implements Runnable {
    //@Autowired
    //protected DnsLookupService dnsLookupService;

    protected DnsServerConfig dnsServerConfig;
    protected T socketServer;

    private final static Logger LOGGER = LoggerFactory.getLogger(DnsSocketServer.class);

    protected DnsSocketServer() {
    }

    protected DnsSocketServer(DnsServerConfig dnsServerConfig) {
        this.dnsServerConfig = dnsServerConfig;
    }

    @Override
    public void run() {
        this.socketServer = this.createSocketServer();

        this.start();
    }

    public abstract void start();

    public abstract void stop();

    @Async("dnsQueryTaskExecutor")
    protected void processMessage(U socket, V inMsgObj, byte[] inMsgData) {
        try (U socketCloseable = socket) {
            Message inMsg = new Message(inMsgData);

            LOGGER.debug("Incoming DNS message: " + inMsg);

            //Message outMsg = this.dnsLookupService.lookup(inMsg);

            //LOGGER.debug("Outgoing DNS message: " + outMsg);

            //this.sendMessage(socketCloseable, inMsgObj, inMsg, outMsg);
        } catch (Throwable th) {
            LOGGER.error("Unable to process DNS message.", th);
        }
    }

    protected abstract void sendMessage(U socket, V inMsgObj, Message inMsg, Message outMsg) throws IOException;

    protected abstract T createSocketServer();

    public void setDnsServerConfig(DnsServerConfig dnsServerConfig) {
        this.dnsServerConfig = dnsServerConfig;
    }
}
