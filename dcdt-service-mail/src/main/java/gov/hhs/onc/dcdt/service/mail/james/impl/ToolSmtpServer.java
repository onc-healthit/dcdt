package gov.hhs.onc.dcdt.service.mail.james.impl;

import org.apache.james.protocols.lib.handler.HandlersPackage;
import org.apache.james.smtpserver.jmx.JMXHandlersLoader;
import org.apache.james.smtpserver.netty.SMTPServer;

public class ToolSmtpServer extends SMTPServer {
    public static class ToolJMXHandlersLoader extends JMXHandlersLoader {
        public ToolJMXHandlersLoader() {
            super();

            this.getHandlers().clear();
        }
    }

    @Override
    protected Class<? extends HandlersPackage> getJMXHandlersPackage() {
        return ToolJMXHandlersLoader.class;
    }
}
