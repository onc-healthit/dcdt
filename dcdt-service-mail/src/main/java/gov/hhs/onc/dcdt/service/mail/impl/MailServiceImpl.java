package gov.hhs.onc.dcdt.service.mail.impl;

import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.impl.AbstractToolService;
import gov.hhs.onc.dcdt.service.mail.MailService;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.james.protocols.lib.netty.AbstractConfigurableAsyncServer;
import org.apache.james.protocols.lib.netty.AbstractServerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.stereotype.Component;

@AutoStartup(false)
@Component("mailServiceImpl")
@Phase(Phase.PHASE_PRECEDENCE_HIGHEST + 3)
@ServiceContextConfiguration({ "spring/spring-service-mail.xml", "spring/spring-service-mail-*.xml" })
public class MailServiceImpl extends AbstractToolService implements MailService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired(required = false)
    private List<AbstractConfigurableAsyncServer> servers;

    @Override
    protected synchronized void stopInternal() throws Exception {
        if (this.hasServers()) {
            for (AbstractConfigurableAsyncServer server : this.servers) {
                if (server.isStarted()) {
                    server.stop();
                }
            }
        }
    }

    @Override
    protected synchronized void startInternal() throws Exception {
        List<AbstractServerFactory> serverFactories = ToolBeanFactoryUtils.createBeansOfType(this.appContext, AbstractServerFactory.class);

        if (!serverFactories.isEmpty()) {
            this.servers = new ArrayList<>(serverFactories.size());

            for (AbstractServerFactory serverFactory : serverFactories) {
                for (AbstractConfigurableAsyncServer server : serverFactory.getServers()) {
                    this.servers.add(server);

                    if (!server.isStarted()) {
                        server.start();
                    }

                    if (server.isStarted()) {
                        LOGGER.debug(String.format("Started mail service James server (class=%s, serviceType=%s, socketType=%s, boundAddrs=[%s]).",
                            ToolClassUtils.getName(server), StringUtils.split(server.getServiceType())[0], server.getSocketType(),
                            ToolStringUtils.joinDelimit(server.getBoundAddresses(), ", ")));
                    }
                }
            }
        }
    }

    @Override
    public boolean hasServers() {
        return !CollectionUtils.isEmpty(this.servers);
    }

    @Nullable
    @Override
    public List<AbstractConfigurableAsyncServer> getServers() {
        return this.servers;
    }

    @Override
    @Resource(name = "taskExecServiceMail")
    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
