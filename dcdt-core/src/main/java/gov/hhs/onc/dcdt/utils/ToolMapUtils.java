package gov.hhs.onc.dcdt.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.lang3.ArrayUtils;

public abstract class ToolMapUtils {
    public static class ToolMultiValueMap<T, U> extends MultiValueMap<T, U> {
        private final static long serialVersionUID = 0L;

        public ToolMultiValueMap() {
            this(new HashMap<>());
        }

        public ToolMultiValueMap(Map<T, ? super Collection<U>> map) {
            this(map, ArrayList::new);
        }

        public ToolMultiValueMap(Supplier<Collection<U>> collSupplier) {
            this(new HashMap<>(), collSupplier);
        }

        public ToolMultiValueMap(Map<T, ? super Collection<U>> map, Supplier<Collection<U>> collSupplier) {
            super(map, collSupplier::get);
        }

        public boolean isEmpty(T key) {
            return (this.size(key) == 0);
        }

        public boolean totalIsEmpty() {
            return (this.totalSize() == 0);
        }
    }

    public static Map<?, ?> toMap(@Nullable Iterable<?> items) {
        return toMap(IteratorUtils.toArray(IteratorUtils.getIterator(items)));
    }

    public static Map<?, ?> toMap(@Nullable Object ... items) {
        return toMap(Object.class, Object.class, items);
    }

    public static <T, U> Map<T, U> toMap(Class<T> keyClass, Class<U> valueClass, @Nullable Iterable<?> items) {
        return toMap(keyClass, valueClass, IteratorUtils.toArray(IteratorUtils.getIterator(items)));
    }

    public static <T, U> Map<T, U> toMap(Class<T> keyClass, Class<U> valueClass, @Nullable Object ... items) {
        return putAll(new HashMap<T, U>(ArrayUtils.getLength(items)), items);
    }

    @Nullable
    public static <T, U, V extends Map<T, U>> V putAll(@Nullable V map, @Nullable Iterable<?> items) {
        return putAll(map, IteratorUtils.toArray(IteratorUtils.getIterator(items)));
    }

    @Nullable
    @SuppressWarnings({ "unchecked" })
    public static <T, U, V extends Map<T, U>> V putAll(@Nullable V map, @Nullable Object ... items) {
        return (map != null) ? (V) MapUtils.putAll(map, items) : null;
    }
}
