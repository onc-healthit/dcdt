package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.service.mail.james.ToolDomainList;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.HierarchicalConfiguration.Node;
import org.apache.james.domainlist.api.DomainListException;
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

    private final static String HELLO_NAME_AUTODETECT_CONFIG_ATTR_NAME = "autodetect";

    private ToolDomainList domainList;

    public ToolSmtpServer(ToolDomainList domainList) {
        this.domainList = domainList;
    }

    @Override
    protected void configureHelloName(Configuration handlerConfig) throws ConfigurationException {
        try {
            Node helloNameConfigNode = new Node(HELLO_NAME, this.domainList.getDefaultDomain());
            helloNameConfigNode.addAttribute(new Node(HELLO_NAME_AUTODETECT_CONFIG_ATTR_NAME, false));

            ((HierarchicalConfiguration) handlerConfig).getRootNode().addChild(helloNameConfigNode);
        } catch (DomainListException e) {
            throw new ConfigurationException("Unable to configure SMTP server hello name.", e);
        }

        super.configureHelloName(handlerConfig);
    }

    @Override
    protected Class<? extends HandlersPackage> getJMXHandlersPackage() {
        return ToolJMXHandlersLoader.class;
    }
}
