package gov.hhs.onc.dcdt.ldap;


import gov.hhs.onc.dcdt.ldap.conf.DirectoryServiceConfig;
import gov.hhs.onc.dcdt.ldap.conf.LdapServerConfig;
import gov.hhs.onc.dcdt.ldap.conf.TransportConfig;
import gov.hhs.onc.dcdt.standalone.ToolWrapper;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.directory.server.annotations.TransportType;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.factory.DirectoryServiceFactory;
import org.apache.directory.server.factory.LdapServerFactory;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.apache.directory.server.protocol.shared.transport.Transport;
import org.apache.directory.server.protocol.shared.transport.UdpTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ToolLdap extends ToolWrapper<ClassPathXmlApplicationContext> {
    private final static List<String> CONTEXT_CONFIG_LOCS_LDAP = ToolResourceUtils.getOverrideableResourceLocation("spring/spring-ldap*.xml");

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolLdap.class);

    @Autowired
    private DirectoryServiceConfig dirServiceConfig;

    private DirectoryServiceFactory dirServiceFactory;
    private DirectoryService dirService;
    private List<LdapServer> ldapServers;

    public ToolLdap() {
        super();
    }

    public ToolLdap(AbstractApplicationContext parentContext) {
        super(parentContext);
    }

    private ToolLdap(String ... args) {
        super(args);
    }

    public static void main(String ... args) {
        new ToolLdap(args).start();
    }

    @Override
    protected void startWrapper() {
        try {
            System.setProperty("workingDirectory", this.dirServiceConfig.getInstanceDir().getAbsolutePath());

            this.dirServiceFactory = this.beanFactory.createBean(this.dirServiceConfig.getFactoryClass());
            this.beanFactory.autowireBean(this.dirServiceFactory);
            dirServiceFactory.init(this.dirServiceConfig.getName());

            this.dirService = this.dirServiceFactory.getDirectoryService();

            LdapServerConfig[] ldapServerConfigs = this.dirServiceConfig.getLdapServers();
            this.ldapServers = new ArrayList<>(ldapServerConfigs.length);

            LdapServerFactory ldapServerFactory;
            LdapServer ldapServer;
            Transport transport;

            for (LdapServerConfig ldapServerConfig : ldapServerConfigs) {
                ldapServerFactory = ldapServerConfig.getFactoryClass().newInstance();
                ldapServerFactory.init();

                ldapServer = ldapServerFactory.getLdapServer();
                ldapServer.setDirectoryService(this.dirService);

                for (TransportConfig transportConfig : ldapServerConfig.getTransports()) {
                    transport = (transportConfig.getType() == TransportType.TCP) ? new TcpTransport(transportConfig.getPort()) : new UdpTransport(
                            transportConfig.getPort());
                    ldapServer.addTransports(transport);
                }

                this.ldapServers.add(ldapServer);

                ldapServer.start();
            }
        } catch (Throwable th) {
            LOGGER.error("Unable to start tool ApacheDS instance.", th);
        }
    }

    @Override
    protected void stopWrapper() {
        try {
            this.dirService.shutdown();
        } catch (Throwable th) {
            LOGGER.error("Unable to stop tool ApacheDS instance.", th);
        }
    }

    @Override
    protected void initializeContext() {
        this.contextConfigLocs.addAll(CONTEXT_CONFIG_LOCS_LDAP);

        super.initializeContext();
    }
}
