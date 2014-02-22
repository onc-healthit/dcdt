package gov.hhs.onc.dcdt.service.mail;

public class MailService {
}

// @formatter:off
/*
public class MailService extends AbstractToolService {
    private final static List<String> CONTEXT_CONFIG_LOCS_MAIL = ToolResourceUtils.getOverrideableResourceLocations(ToolArrayUtils.asList(
        "spring/spring-service-mail.xml", "spring/spring-service-mail*.xml", "service/mail/james/META-INF/org/apache/james/spring-server.xml"));

    public MailService() {
        super();
    }

    public MailService(ApplicationContext parentContext) {
        super(parentContext);
    }

    @Override
    protected ToolJamesServerApplicationContext createContext() {
        return new ToolJamesServerApplicationContext(this.contextConfigLocs, this.parentContext);
    }

    @Override
    protected void initializeContext() {
        this.contextConfigLocs.addAll(CONTEXT_CONFIG_LOCS_MAIL);

        super.initializeContext();
    }
}
*/
// @formatter:on
