package gov.hhs.onc.dcdt.context.impl;

import gov.hhs.onc.dcdt.context.ToolInitializer;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

public abstract class AbstractToolInitializer implements ToolInitializer {
    protected AbstractRefreshableConfigApplicationContext appContext;

    protected AbstractToolInitializer(AbstractRefreshableConfigApplicationContext appContext) {
        this.appContext = appContext;
    }
}
