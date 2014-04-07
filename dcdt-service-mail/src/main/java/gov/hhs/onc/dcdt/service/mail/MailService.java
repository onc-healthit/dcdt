package gov.hhs.onc.dcdt.service.mail;

import gov.hhs.onc.dcdt.service.ToolService;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.james.protocols.lib.netty.AbstractConfigurableAsyncServer;

public interface MailService extends ToolService {
    public boolean hasServers();

    @Nullable
    public List<AbstractConfigurableAsyncServer> getServers();
}
