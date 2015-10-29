package gov.hhs.onc.dcdt.context.impl;

import gov.hhs.onc.dcdt.context.ToolApplicationContextException;
import gov.hhs.onc.dcdt.context.ToolApplicationContextInitializer;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

public abstract class AbstractToolApplicationContextInitializer implements ToolApplicationContextInitializer {
    @Override
    public void initialize(AbstractRefreshableConfigApplicationContext appContext) {
        try {
            this.initializeInternal(appContext);
        } catch (Exception e) {
            throw new ToolApplicationContextException(String.format("Unable to initialize Spring application context (class=%s).", appContext.getClass()
                .getName()), e);
        }
    }

    protected abstract void initializeInternal(AbstractRefreshableConfigApplicationContext appContext) throws Exception;
}
