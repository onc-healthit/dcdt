package gov.hhs.onc.dcdt.collections;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;

public interface ToolPredicate<T> {
    public static <T> java.util.function.Predicate<T> wrap(Predicate<T> predicate) {
        return t -> {
            try {
                return predicate.test(t);
            } catch (Exception e) {
                throw new PredicateRuntimeException(String.format("Unable to evaluate predicate for object (class=%s): %s", ToolClassUtils.getName(t), t), e);
            }
        };
    }
}
