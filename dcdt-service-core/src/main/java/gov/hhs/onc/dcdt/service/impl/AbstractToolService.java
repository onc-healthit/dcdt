package gov.hhs.onc.dcdt.service.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolLifecycleBean;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.ServiceStatusType;
import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

@ServiceContextConfiguration({ "spring/spring-core.xml", "spring/spring-core*.xml", "spring/spring-service.xml", "spring/spring-service-embedded.xml",
    "spring/spring-service-standalone.xml" })
public abstract class AbstractToolService extends AbstractToolLifecycleBean implements ToolService {
    protected AbstractApplicationContext appContext;
    protected ServiceStatusType statusType = ServiceStatusType.STOPPED;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolService.class);

    @Override
    public synchronized void stop() {
        if (this.isRunning()) {
            this.statusType = ServiceStatusType.STOPPING;

            LOGGER.info(String.format("Stopping service (beanName=%s, class=%s).", this.beanName, ToolClassUtils.getName(this)));

            this.stopInternal();

            this.statusType = ServiceStatusType.STOPPED;

            LOGGER.info(String.format("Stopped service (beanName=%s, class=%s).", this.beanName, ToolClassUtils.getName(this)));
        }
    }

    @Override
    public synchronized void start() {
        if (!this.isRunning()) {
            this.statusType = ServiceStatusType.STARTING;

            LOGGER.info(String.format("Starting service (beanName=%s, class=%s).", this.beanName, ToolClassUtils.getName(this)));

            this.startInternal();

            this.statusType = ServiceStatusType.STARTED;

            LOGGER.info(String.format("Started service (beanName=%s, class=%s).", this.beanName, ToolClassUtils.getName(this)));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.appContext.registerShutdownHook();
    }

    protected synchronized void stopInternal() {
    }

    protected synchronized void startInternal() {
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }

    @Override
    public boolean isRunning() {
        return this.statusType.isRunning();
    }

    @Override
    public ServiceStatusType getStatusType() {
        return this.statusType;
    }
}
