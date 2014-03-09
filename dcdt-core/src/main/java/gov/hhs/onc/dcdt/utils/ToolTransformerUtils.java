package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.ToolRuntimeException;
import javax.annotation.Nullable;
import org.apache.commons.collections4.Transformer;

public abstract class ToolTransformerUtils {
    public static abstract class ToolTransformer<T, U> implements Transformer<T, U> {
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

    public static class TransformerRuntimeException extends ToolRuntimeException {
        private final static long serialVersionUID = 0L;

        public TransformerRuntimeException() {
            super();
        }

        public TransformerRuntimeException(String msg) {
            super(msg);
        }

        public TransformerRuntimeException(Throwable cause) {
            super(cause);
        }

        public TransformerRuntimeException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}
