package gov.hhs.onc.dcdt.collections.impl;

import gov.hhs.onc.dcdt.collections.ToolTransformer;
import gov.hhs.onc.dcdt.collections.TransformerRuntimeException;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import javax.annotation.Nullable;

public abstract class AbstractToolTransformer<T, U> implements ToolTransformer<T, U> {
    @Nullable
    @Override
    public U transform(@Nullable T input) {
        try {
            return this.transformInternal(input);
        } catch (Exception e) {
            throw new TransformerRuntimeException(String.format("Unable to transform (class=%s) input object (class=%s): %s", ToolClassUtils.getName(this),
                ToolClassUtils.getName(input), input), e);
        }
    }

    @Nullable
    protected abstract U transformInternal(@Nullable T input) throws Exception;
}
