package gov.hhs.onc.dcdt.service.wrapper.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.context.ToolApplicationContextException;
import gov.hhs.onc.dcdt.context.impl.ClassPathContextLoader;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import gov.hhs.onc.dcdt.service.server.ToolServer;
import gov.hhs.onc.dcdt.service.wrapper.ToolServiceWrapper;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolDateUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.util.ArrayList;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.tanukisoftware.wrapper.WrapperManager;
import org.tanukisoftware.wrapper.event.WrapperControlEvent;
import org.tanukisoftware.wrapper.event.WrapperEvent;
import org.tanukisoftware.wrapper.event.WrapperEventListener;

public abstract class AbstractToolServiceWrapper<T extends ToolServerConfig, U extends ToolServer<T>, V extends ToolService<T, U>> implements
    ToolServiceWrapper<T, U, V> {
    protected AbstractApplicationContext appContext;
    protected Class<V> serviceClass;
    protected Class<? extends V> serviceImplClass;
    protected String[] args;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolServiceWrapper.class);

    protected AbstractToolServiceWrapper(Class<V> serviceClass, Class<? extends V> serviceImplClass, String ... args) {
        this.serviceClass = serviceClass;
        this.serviceImplClass = serviceImplClass;
        this.args = args;
    }

    @Override
    public void stop() {
        if (this.appContext != null) {
            this.stopInternal();

            LOGGER.info(String.format("Service (class=%s, implClass=%s) wrapper (class=%s) stopped.", ToolClassUtils.getName(this.serviceClass),
                ToolClassUtils.getName(this.serviceImplClass), ToolClassUtils.getName(this)));
        } else {
            LOGGER.warn(String.format("Attempted to stop service (class=%s, implClass=%s) wrapper (class=%s) that is not running.",
                ToolClassUtils.getName(this.serviceClass), ToolClassUtils.getName(this.serviceImplClass), ToolClassUtils.getName(this)));
        }
    }

    @Override
    public void start() {
        if (this.appContext == null) {
            this.startInternal();

            LOGGER.info(String.format("Service (class=%s, implClass=%s) wrapper (class=%s) started.", ToolClassUtils.getName(this.serviceClass),
                ToolClassUtils.getName(this.serviceImplClass), ToolClassUtils.getName(this)));

            while (this.appContext.isRunning()) {
                try {
                    Thread.sleep(ToolDateUtils.MS_IN_SEC);
                } catch (InterruptedException ignored) {
                    break;
                }
            }
        } else {
            LOGGER.warn(String.format("Attempted to start service (class=%s, implClass=%s) wrapper (class=%s) that is already running.",
                ToolClassUtils.getName(this.serviceClass), ToolClassUtils.getName(this.serviceImplClass), ToolClassUtils.getName(this)));
        }
    }

    @Override
    public void fired(WrapperEvent wrapperEvent) {
        if (ToolClassUtils.isAssignable(wrapperEvent.getClass(), WrapperControlEvent.class)) {
            WrapperControlEvent wrapperControlEvent = (WrapperControlEvent) wrapperEvent;

            LOGGER.info(String.format("Service (class=%s, implClass=%s) wrapper (class=%s) control event (name=%s).",
                ToolClassUtils.getName(this.serviceClass), ToolClassUtils.getName(this.serviceImplClass), ToolClassUtils.getName(this),
                wrapperControlEvent.getControlEventName()));

            if ((wrapperControlEvent.getControlEvent() != WrapperManager.WRAPPER_CTRL_LOGOFF_EVENT) || WrapperManager.isLaunchedAsService()) {
                this.stop();
            }
        }
    }

    protected void stopInternal() {
        this.appContext.stop();
    }

    protected void startInternal() {
        WrapperManager.addWrapperEventListener(this, WrapperEventListener.EVENT_FLAG_CONTROL);

        ClassPathContextLoader contextLoader = new ClassPathContextLoader();
        // noinspection ConstantConditions
        String[] contextConfigLocs =
            contextLoader.processLocations(ToolListUtils
                .reverse(new ArrayList<>(ToolClassUtils.getHierarchy(this.serviceImplClass)))
                .stream()
                .flatMap(
                    serviceHierarchyClass -> ToolAnnotationUtils.findAnnotations(ServiceContextConfiguration.class).stream()
                        .flatMap(serviceContextConfigAnno -> Stream.of(serviceContextConfigAnno.value()))).toArray(String[]::new));

        try {
            try {
                while (!(this.appContext = contextLoader.loadContext(contextConfigLocs)).isActive()) {
                    Thread.sleep(ToolDateUtils.MS_IN_SEC);
                }

                LOGGER.info(String.format("Loaded service (class=%s, implClass=%s) wrapper (class=%s) Spring application context (class=%s): [%s]",
                    ToolClassUtils.getName(this.serviceClass), ToolClassUtils.getName(this.serviceImplClass), ToolClassUtils.getName(this),
                    ToolClassUtils.getName(this.appContext), ToolStringUtils.joinDelimit(contextConfigLocs, ", ")));

                // noinspection ConstantConditions
                ToolBeanFactoryUtils.getBeanOfType(this.appContext, this.serviceClass).start();
            } catch (InterruptedException ignored) {
            }
        } catch (Exception e) {
            throw new ToolApplicationContextException(String.format(
                "Unable to load service (class=%s, implClass=%s) wrapper (class=%s) Spring application context (class=%s): [%s]",
                ToolClassUtils.getName(this.serviceClass), ToolClassUtils.getName(this.serviceImplClass), ToolClassUtils.getName(this),
                ToolClassUtils.getName(this.appContext), ToolStringUtils.joinDelimit(contextConfigLocs, ", ")), e);
        }
    }
}
