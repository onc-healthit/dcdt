package gov.hhs.onc.dcdt.context.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.dcdt.context.ToolContextLoader;
import gov.hhs.onc.dcdt.context.utils.ToolContextUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

public abstract class AbstractToolContextLoader<T extends AbstractRefreshableConfigApplicationContext> implements ToolContextLoader<T> {
    protected T appContext;

    @Override
    public T loadContext(String ... configLocs) throws Exception {
        this.buildContext(configLocs);
        this.initializeContext();

        this.appContext.refresh();
        this.appContext.registerShutdownHook();
        this.appContext.start();

        return this.appContext;
    }

    @Override
    public String[] processLocations(String ... configLocs) {
        return this.processLocations(this.getClass(), configLocs);
    }

    @Override
    public String[] processLocations(Class<?> clazz, String ... configLocs) {
        return ToolCollectionUtils.toArray(ToolResourceUtils.getOverrideableLocations(ToolArrayUtils.asList(configLocs)), String.class);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected void initializeContext() throws Exception {
        // noinspection NullArgumentToVariableArgMethod
        for (Object appContextInit : ToolContextUtils.buildComponents(ApplicationContextInitializer.class)) {
            ((ApplicationContextInitializer<AbstractRefreshableConfigApplicationContext>) appContextInit).initialize(this.appContext);
        }
    }

    protected abstract void buildContext(String ... configLocs) throws Exception;
}
