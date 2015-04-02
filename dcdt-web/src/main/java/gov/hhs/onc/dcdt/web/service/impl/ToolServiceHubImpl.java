package gov.hhs.onc.dcdt.web.service.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolLifecycleBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.context.AutoStartup;
import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.web.service.ToolServiceHub;
import gov.hhs.onc.dcdt.web.service.ToolServiceType;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

@AutoStartup
@Component("toolServiceHubImpl")
public class ToolServiceHubImpl extends AbstractToolLifecycleBean implements ToolServiceHub {
    private final static Logger LOGGER = LoggerFactory.getLogger(ToolServiceHubImpl.class);

    private AbstractApplicationContext appContext;
    private Map<ToolServiceType, ToolService> serviceMap = new EnumMap<>(ToolServiceType.class);
    private Map<ToolServiceType, List<String>> serviceMsgsMap = new EnumMap<>(ToolServiceType.class);

    @Override
    protected void stopInternal() throws Exception {
        this.serviceMsgsMap.clear();

        ToolService service;

        for (ToolServiceType serviceType : EnumSet.allOf(ToolServiceType.class)) {
            if (!this.serviceMap.containsKey(serviceType) || ((service = this.serviceMap.get(serviceType)) == null)) {
                continue;
            }

            try {
                // noinspection ConstantConditions
                service.stop();
            } catch (Exception e) {
                // noinspection ConstantConditions
                this.serviceMsgsMap.put(serviceType, ToolArrayUtils.asList(String.format("Unable to stop service (class=%s, type=%s, status=%s): %s",
                    ToolClassUtils.getName(service), serviceType.name(), service.getLifecycleStatus().name(), e.getMessage())));

                LOGGER.error(String.format("Unable to stop service (class=%s, type=%s, status=%s).", ToolClassUtils.getName(service), serviceType.name(),
                    service.getLifecycleStatus().name()), e);
            }
        }
    }

    @Override
    protected void startInternal() throws Exception {
        ToolService service;

        for (ToolServiceType serviceType : EnumSet.allOf(ToolServiceType.class)) {
            this.serviceMap.put(serviceType, (service = ToolBeanFactoryUtils.getBeanOfType(this.appContext, serviceType.getServiceClass())));

            try {
                // noinspection ConstantConditions
                service.start();
            } catch (Exception e) {
                // noinspection ConstantConditions
                this.serviceMsgsMap.put(serviceType, ToolArrayUtils.asList(String.format("Unable to start service (class=%s, type=%s, status=%s): %s",
                    ToolClassUtils.getName(service), serviceType.name(), service.getLifecycleStatus().name(), e.getMessage())));

                LOGGER.error(String.format("Unable to start service (class=%s, type=%s, status=%s).", ToolClassUtils.getName(service), serviceType.name(),
                    service.getLifecycleStatus().name()), e);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }

    @Override
    public Map<ToolServiceType, ToolService> getServiceMap() {
        return this.serviceMap;
    }

    @Override
    public Map<ToolServiceType, List<String>> getServiceMessagesMap() {
        return this.serviceMsgsMap;
    }
}
