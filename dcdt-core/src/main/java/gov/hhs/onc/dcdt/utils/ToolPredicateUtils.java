package gov.hhs.onc.dcdt.utils;

import gov.hhs.onc.dcdt.ToolRuntimeException;
import javax.annotation.Nullable;
import org.apache.commons.collections4.Predicate;

public abstract class ToolPredicateUtils {
    public static abstract class ToolPredicate<T> implements Predicate<T> {
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

    public static class PredicateRuntimeException extends ToolRuntimeException {
        private final static long serialVersionUID = 0L;

        public PredicateRuntimeException() {
            super();
        }

        public PredicateRuntimeException(String msg) {
            super(msg);
        }

        public PredicateRuntimeException(Throwable cause) {
            super(cause);
        }

        public PredicateRuntimeException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }
}
