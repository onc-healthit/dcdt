package gov.hhs.onc.dcdt.service.mail;

import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.mail.james.ToolJamesServerApplicationContext;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.ApplicationContext;

public class ToolMailService extends ToolService<ToolJamesServerApplicationContext> {
    private final static List<String> CONTEXT_CONFIG_LOCS_MAIL = ToolResourceUtils.getOverrideableResourceLocations(Arrays.asList(
            "spring/spring-service-mail*.xml", "service/mail/james/META-INF/org/apache/james/spring-server.xml"));

    public ToolMailService() {
        super();
    }

    public ToolMailService(ApplicationContext parentContext) {
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
