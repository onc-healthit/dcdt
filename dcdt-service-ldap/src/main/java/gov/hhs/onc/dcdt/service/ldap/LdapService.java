package gov.hhs.onc.dcdt.service.ldap;

public class LdapService {
}

// @formatter:off
/*
public class LdapService extends AbstractToolService {
    private final static List<String> CONTEXT_CONFIG_LOCS_LDAP = ToolResourceUtils.getOverrideableResourceLocation("spring/spring-service-ldap.xml",
        "spring/spring-service-ldap*.xml");

    private final static Logger LOGGER = LoggerFactory.getLogger(LdapService.class);

    @Autowired
    private DirectoryServiceConfig dirServiceConfig;

    private DirectoryServiceFactory dirServiceFactory;
    private DirectoryService dirService;
    private List<LdapServer> ldapServers;

    public LdapService() {
        super();
    }

    public LdapService(ApplicationContext parentContext) {
        super(parentContext);
    }

    @Override
    protected void startService() {
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

        super.startService();
    }

    @Override
    protected void stopService() {
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
*/
// @formatter:on
