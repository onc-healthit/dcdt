package gov.hhs.onc.dcdt.collections;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;

public interface ToolTransformer<T, U> {
    public static <T, U> java.util.function.Function<T, U> wrap(Function<T, U> transformer) {
        return t -> {
            try {
                return transformer.apply(t);
            } catch (Exception e) {
                throw new TransformerRuntimeException(String.format("Unable to transform input object (class=%s): %s", ToolClassUtils.getName(t), t), e);
            }
        };
    }
}
