package gov.hhs.onc.dcdt.context.impl;

import gov.hhs.onc.dcdt.context.ToolInitializer;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

public abstract class AbstractToolInitializer<T extends AbstractRefreshableConfigApplicationContext> implements ToolInitializer<T> {
    protected T appContext;

    protected AbstractToolInitializer(T appContext) {
        this.appContext = appContext;
    }
}
