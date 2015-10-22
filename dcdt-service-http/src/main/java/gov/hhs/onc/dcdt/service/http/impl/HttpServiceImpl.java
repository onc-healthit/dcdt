package gov.hhs.onc.dcdt.service.http.impl;

import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.http.HttpService;
import gov.hhs.onc.dcdt.service.http.server.HttpServer;
import gov.hhs.onc.dcdt.service.impl.AbstractToolService;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.Lifecycle;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.stereotype.Component;

@AutoStartup(false)
@Component("httpServiceImpl")
@Phase(Phase.PHASE_PRECEDENCE_HIGHEST + 1)
@ServiceContextConfiguration({ "spring/spring-service-http.xml", "spring/spring-service-http-*.xml" })
public class HttpServiceImpl extends AbstractToolService implements HttpService {
    private List<HttpServer> servers;

    @Override
    protected void stopInternal() throws Exception {
        if (this.hasServers()) {
            this.servers.forEach(Lifecycle::stop);
        }
    }

    @Override
    protected void startInternal() throws Exception {
        this.servers = ToolBeanFactoryUtils.getBeansOfType(this.appContext, HttpServer.class);

        if (this.hasServers()) {
            this.servers.forEach(Lifecycle::start);
        }
    }

    @Override
    public boolean hasServers() {
        return !CollectionUtils.isEmpty(this.servers);
    }

    @Nullable
    @Override
    public List<HttpServer> getServers() {
        return this.servers;
    }

    @Override
    @Resource(name = "taskExecServiceHttp")
    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        super.setTaskExecutor(taskExec);
    }
}
