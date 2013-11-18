package gov.hhs.onc.dcdt.service.mail;


import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.mail.james.ToolJamesServerApplicationContext;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.util.List;
import org.springframework.context.ApplicationContext;

public class MailService extends ToolService {
    private final static List<String> CONTEXT_CONFIG_LOCS_MAIL = ToolResourceUtils.getOverrideableResourceLocations(ToolArrayUtils.asList(
        "spring/spring-service-mail*.xml", "service/mail/james/META-INF/org/apache/james/spring-server.xml"));

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
