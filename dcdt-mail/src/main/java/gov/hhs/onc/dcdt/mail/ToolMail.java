package gov.hhs.onc.dcdt.mail;


import gov.hhs.onc.dcdt.mail.james.ToolJamesServerApplicationContext;
import gov.hhs.onc.dcdt.standalone.ToolWrapper;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.support.AbstractApplicationContext;

public class ToolMail extends ToolWrapper<ToolJamesServerApplicationContext> {
    private final static List<String> CONTEXT_CONFIG_LOCS_MAIL = ToolResourceUtils.getOverrideableResourceLocations(Arrays.asList("spring/spring-mail*.xml",
            "mail/james/META-INF/org/apache/james/spring-server.xml"));

    public ToolMail(String ... args) {
        super(args);
    }

    public ToolMail(AbstractApplicationContext parentContext) {
        super(parentContext);
    }

    public static void main(String ... args) {
        new ToolMail(args).start();
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
