package gov.hhs.onc.dcdt.service.wrapper.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.context.ToolContextLoader;
import gov.hhs.onc.dcdt.context.impl.ToolContextLoaderImpl;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.ToolServiceRuntimeException;
import gov.hhs.onc.dcdt.service.wrapper.ToolServiceWrapper;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolIteratorUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.tanukisoftware.wrapper.WrapperManager;
import org.tanukisoftware.wrapper.event.WrapperControlEvent;
import org.tanukisoftware.wrapper.event.WrapperEvent;
import org.tanukisoftware.wrapper.event.WrapperEventListener;

public abstract class AbstractToolServiceWrapper<T extends ToolService> implements ToolServiceWrapper<T> {
    protected class ToolServiceWrapperShutdownHook implements Runnable {
        @Override
        public void run() {
            AbstractToolServiceWrapper.this.stop();

            try {
                Runtime.getRuntime().removeShutdownHook(AbstractToolServiceWrapper.this.shutdownHookThread);
            } catch (IllegalStateException ignored) {
            }
        }
    }

    protected AbstractApplicationContext appContext;
    protected Class<T> serviceClass;
    protected Class<? extends T> serviceImplClass;
    protected String[] args;
    protected Thread shutdownHookThread;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolServiceWrapper.class);

    protected AbstractToolServiceWrapper(Class<T> serviceClass, Class<? extends T> serviceImplClass, String ... args) {
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
                    Thread.sleep(DateUtils.MILLIS_PER_SECOND);
                } catch (InterruptedException e) {
                    LOGGER.debug(String.format("Service (class=%s, implClass=%s) wrapper (class=%s) start thread interrupted.",
                        ToolClassUtils.getName(this.serviceClass), ToolClassUtils.getName(this.serviceImplClass), ToolClassUtils.getName(this)));

                    break;
                }
            }
        } else {
            LOGGER.warn(String.format("Attempted to start service (class=%s, implClass=%s) wrapper (class=%s) that is already running.",
                ToolClassUtils.getName(this.serviceClass), ToolClassUtils.getName(this.serviceImplClass), ToolClassUtils.getName(this)));
        }
    }

    @Override
    public void run() {
        this.start();
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

    @SuppressWarnings({ "ConstantConditions" })
    protected void startInternal() {
        WrapperManager.addWrapperEventListener(this, WrapperEventListener.EVENT_FLAG_CONTROL);

        Runtime.getRuntime().addShutdownHook((this.shutdownHookThread = new Thread(new ToolServiceWrapperShutdownHook())));

        ToolContextLoader contextLoader = new ToolContextLoaderImpl();
        List<String> contextConfigLocs =
            contextLoader.processLocations(ToolCollectionUtils.addAll(new ArrayList<String>(), IteratorUtils.asIterable(ToolIteratorUtils
                .chainedArrayIterator(ToolAnnotationUtils.getValues(ServiceContextConfiguration.class, String[].class,
                    ToolListUtils.reverse(new ArrayList<>(ToolClassUtils.getHierarchy(this.serviceImplClass))))))));

        try {
            this.appContext = (AbstractApplicationContext) contextLoader.loadContext(contextConfigLocs);

            LOGGER.info(String.format("Loaded service (class=%s, implClass=%s) wrapper (class=%s) Spring application context (class=%s): [%s]",
                ToolClassUtils.getName(this.serviceClass), ToolClassUtils.getName(this.serviceImplClass), ToolClassUtils.getName(this),
                ToolClassUtils.getName(this.appContext), ToolStringUtils.joinDelimit(contextConfigLocs, ", ")));
        } catch (Exception e) {
            throw new ToolServiceRuntimeException(String.format(
                "Unable to load service (class=%s, implClass=%s) wrapper (class=%s) Spring application context (class=%s): [%s]",
                ToolClassUtils.getName(this.serviceClass), ToolClassUtils.getName(this.serviceImplClass), ToolClassUtils.getName(this),
                ToolClassUtils.getName(this.appContext), ToolStringUtils.joinDelimit(contextConfigLocs, ", ")), e);
        }

        ToolBeanFactoryUtils.getBeanOfType(this.appContext, this.serviceClass).start();
    }
}
