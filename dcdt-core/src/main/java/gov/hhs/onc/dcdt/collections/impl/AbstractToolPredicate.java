package gov.hhs.onc.dcdt.collections.impl;

import gov.hhs.onc.dcdt.collections.PredicateRuntimeException;
import gov.hhs.onc.dcdt.collections.ToolPredicate;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import javax.annotation.Nullable;

public abstract class AbstractToolPredicate<T> implements ToolPredicate<T> {
    @Override
    public boolean evaluate(@Nullable T obj) {
        try {
            return this.evaluateInternal(obj);
        } catch (Exception e) {
            throw new PredicateRuntimeException(String.format("Unable to evaluate predicate (class=%s) for object (class=%s): %s",
                ToolClassUtils.getName(this), ToolClassUtils.getName(obj), obj), e);
        }
    }

    protected abstract boolean evaluateInternal(@Nullable T obj) throws Exception;
}
